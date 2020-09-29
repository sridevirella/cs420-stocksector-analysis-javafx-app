package util;

import api.ReadFile;
import domain.MonthlyData;
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
    private static final String[] yearsArr = {"2008", "2009", "2010", "2011", "2012", "2013",
                                              "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

    public static List<MonthlyData> getDataFromFile(String fileName ) throws IOException, ParseException {

         List<MonthlyData> monthlyDataList = new ArrayList<>();
         BufferedReader br = saveToBuffer( fileName );
         getEachLine( br, monthlyDataList );
         br.close();
        return monthlyDataList;
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

    public static Map<String, List<MonthlyData>> getYearlyDataMap( List<MonthlyData> monthlyDataList ) {

        Map<String, List<MonthlyData>> yearlyDataMap = new HashMap<>();

        for( MonthlyData md: monthlyDataList ){
            String year =  md.getDate().substring(0,4);
            categorise( year, md, yearlyDataMap);
            md.setDate( Month.of( Integer.parseInt(md.getDate().substring(5,7))).name() ) ;
        }
        return yearlyDataMap;
    }

    private static void categorise( String year, MonthlyData md, Map<String,List<MonthlyData>> yearlyDataMap ){

        List<String> yearsList = new ArrayList<>();
        addToList( yearsList);

        if( yearsList.contains(year)){
            addToMap( year, md, yearlyDataMap);
        }
    }

    private static void addToList( List<String> list){

        list.addAll(Arrays.asList(yearsArr));
    }

    private static void addToMap(String year, MonthlyData md, Map<String, List<MonthlyData>> map){

        if(!map.containsKey(year))
            map.put( year, new ArrayList<>(Collections.singletonList(md)) );
        else
            map.get(year).add(md);
    }
}
