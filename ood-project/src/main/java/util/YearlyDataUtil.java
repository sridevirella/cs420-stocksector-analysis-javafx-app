package util;

import api.ReadFile;
import domain.MonthlyData;
import domain.YearName;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Month;
import java.util.*;

public class YearlyDataUtil {

    private YearlyDataUtil() {}

    public static Map<YearName, List<MonthlyData>> getYearlyDataMap(String fileName ) throws IOException, ParseException {

         List<MonthlyData> monthlyDataList = new ArrayList<>();
         BufferedReader br = saveToBuffer( fileName );
         getEachLine( br, monthlyDataList );
         br.close();
        return createYearlyDataMap( monthlyDataList );
    }

    private static BufferedReader saveToBuffer( String fileName ) throws IOException {

        return new BufferedReader( new InputStreamReader(ReadFile.getDataStream( fileName )) );
    }

    private static void getEachLine( BufferedReader br, List<MonthlyData> monthlyList ) throws IOException, ParseException {

        String line;
        while ((line = br.readLine()) != null) {
            convertToJson("{" + line + "}", monthlyList);
        }
    }

    private static void convertToJson(String line, List<MonthlyData> mdList) throws ParseException {

        JSONObject obj = (JSONObject) new JSONParser().parse(line);

        for (Object key : obj.keySet()) {
            JSONObject innerObj = (JSONObject) obj.get(key);
            convertToDomainObj( innerObj, mdList, key.toString() );
        }
    }

    private static void convertToDomainObj( JSONObject innerObj, List<MonthlyData> mdList, String key ){

        double open = Double.parseDouble((String)innerObj.get("1. open"));
        double high = Double.parseDouble((String)innerObj.get("2. high"));
        double low = Double.parseDouble((String)innerObj.get("3. low"));
        double close = Double.parseDouble((String)innerObj.get("4. close"));
        long volume = Long.parseLong((String)innerObj.get("5. volume"));
        mdList.add(new MonthlyData( key, open, high, low, close, volume ));
    }

    public static Map<YearName, List<MonthlyData>> createYearlyDataMap(List<MonthlyData> monthlyDataList ) {

        Map<YearName, List<MonthlyData>> yearlyDataMap = new HashMap<>();

        for( MonthlyData md: monthlyDataList ){
            String year =  md.getDate().substring(0,4);
            categorise( year, md, yearlyDataMap);
            md.setDate( Month.of( Integer.parseInt(md.getDate().substring(5,7))).name() ) ;
        }
        return yearlyDataMap;
    }

    private static void categorise( String year, MonthlyData md, Map<YearName,List<MonthlyData>> yearlyDataMap ){

        for( YearName yearCategory : YearName.values()){

            if( yearCategory.getYear() == (Integer.parseInt(year)) )
                addToMap( yearCategory, md, yearlyDataMap );
        }
    }

    private static void addToMap(YearName yearName, MonthlyData md, Map<YearName, List<MonthlyData>> map){

        if(!map.containsKey(yearName))
            map.put( yearName, new ArrayList<>(Collections.singletonList(md)) );
        else
            map.get(yearName).add(md);
    }

}
