package util;

import api.ReadFile;
import domain.MonthlyData;
import domain.YearName;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class YearlyDataUtil {

    private static List<MonthlyData> monthlyDataList;
    private YearlyDataUtil() {}

    public static Map<YearName, List<MonthlyData>> getYearlyDataMap(String fileName ) throws IOException {

         monthlyDataList = new ArrayList<>();
         BufferedReader br = saveToBuffer( fileName );
         getAllLines( br, monthlyDataList );
         br.close();
        return createYearlyDataMap( monthlyDataList );
    }

    private static BufferedReader saveToBuffer( String fileName ) throws IOException {

        return new BufferedReader( new InputStreamReader(ReadFile.getDataStream( fileName )) );
    }

    private static void getAllLines( BufferedReader br, List<MonthlyData> monthlyList ) {

        br.lines().map(JSONObject::new)
                  .forEach( jsonObj ->  {
                   getInnerObject(monthlyList, jsonObj, jsonObj.keySet().stream()
                                                                        .limit(1)
                                                                        .collect(Collectors.toList()));
                });
    }

    private static void getInnerObject(List<MonthlyData> monthlyList, JSONObject jsonObject, List<String> keyList) {

        convertToDomainObj(jsonObject.getJSONObject(keyList.get(0)), monthlyList, String.valueOf(keyList.get(0)) );
    }

    private static void convertToDomainObj( JSONObject innerObj, List<MonthlyData> mdList, String key ) {

        double open = innerObj.getDouble("1. open");
        double high = innerObj.getDouble("2. high");
        double low = innerObj.getDouble("3. low");
        double close = innerObj.getDouble("4. close");
        long volume = innerObj.getLong("5. volume");
        mdList.add(new MonthlyData( key, open, high, low, close, volume ));
    }

    public static Map<YearName, List<MonthlyData>> createYearlyDataMap( List<MonthlyData> monthlyDataList ) {

        Map<YearName, List<MonthlyData>> yearlyDataMap = new HashMap<>();

        monthlyDataList.forEach(monthlyData -> {

            String year =  monthlyData.getDate().substring(0,4);
            categorise( year, monthlyData, yearlyDataMap);
            monthlyData.setDate( year + "," + Month.of( Integer.parseInt(monthlyData.getDate().substring(5,7))).name() ) ;
        });

        return yearlyDataMap;
    }

    private static void categorise( String year, MonthlyData md, Map<YearName,List<MonthlyData>> yearlyDataMap ) {

        Arrays.asList(YearName.values()).forEach( yearCategory -> {
            if( yearCategory.getYear() == (Integer.parseInt(year)) )
               addToMap( yearCategory, md, yearlyDataMap );
        });
    }

    private static void addToMap( YearName yearName, MonthlyData md, Map<YearName, List<MonthlyData>> map) {

        if(!map.containsKey(yearName))
            map.put( yearName, new ArrayList<>(Collections.singletonList(md)) );
        else
            map.get(yearName).add(md);
    }

    public static List<MonthlyData> getMonthlyDataList() {
        return monthlyDataList;
    }
}
