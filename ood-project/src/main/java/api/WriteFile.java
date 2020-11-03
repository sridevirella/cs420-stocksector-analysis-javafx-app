package api;

import domain.YearName;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class WriteFile {

    private final List<String> symbols;
    private final Map<String, String> fullFilePath;
    private final HttpClient httpConnInst;
    private static BufferedWriter bw;

    public WriteFile(List<String> fileName) throws IOException {

        this.fullFilePath = createFile( fileName );
        this.httpConnInst = new HttpClient();
        this.symbols = fileName;
    }

    public void getApiDataAndWriteToFile() throws IOException, InterruptedException {

        for( int i = 0; i < symbols.size(); i++ ) {
            if( i == 5 || i == 9 ) {           // API call frequency is 5 calls per minute
                Thread.sleep(60000);
            }
            writeData(symbols.get(i));
        }
    }

    private void writeData( String symbol ) throws IOException {

        bw = new BufferedWriter( new FileWriter( fullFilePath.get(symbol), true) );
        InputStream is   = httpConnInst.getApiData( symbol );
        parseApiData( is );
        bw.flush();
        bw.close();
    }

    public Map<String, String> createFile(List<String> fileName ) throws IOException {

        if(!Files.exists(getPath())) {
            Files.createDirectories(getPath());
        }
        return getFullPath( fileName );
    }

    private Map<String, String> getFullPath(List<String> fileName ) throws IOException {

        Map<String, String> fileNameMap = new HashMap<>();
        getEachFilePath(fileName, getPath(), fileNameMap);
        return fileNameMap;
    }

    private void getEachFilePath(List<String> fileName, Path filePath, Map<String, String> fileNameMap) {

        fileName.forEach(fName -> {
                fileNameMap.put( fName, Path.of(filePath.toString(), fName+".txt").toString() );
           });
    }

    private static Path getPath() {

        return Path.of("build", "resources", "apidatafiles").toAbsolutePath();
    }

    private void parseApiData( InputStream is ) throws IOException {

        parseJson( writeDataToBuffer( is ) );
    }

    private List<String> writeDataToBuffer(InputStream inputStream ) throws IOException {

       try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
           return Collections.singletonList(bufferedReader.lines().collect(Collectors.joining()));
       }
    }

    private void parseJson( List<String> apiData ) throws IOException {

       List<JSONObject> jsonObjectList =  apiData.stream()
                                                 .map(line -> new JSONObject(line).getJSONObject("Monthly Time Series"))
                                                 .limit(1)
                                                 .collect(Collectors.toList());
       getYearRangeData(jsonObjectList.get(0));
    }

    private void getYearRangeData(JSONObject monthlyData) {

        List<String> yearRangeDataList = new ArrayList<>();

        monthlyData.keySet().stream()
                            .distinct()
                            .forEach( keyStr -> filterData(monthlyData, yearRangeDataList, keyStr));
        writeToFile(yearRangeDataList);
    }

    private void filterData(JSONObject monthlyData, List<String> yearRangeDataList, String keyStr) {

        int year = Integer.parseInt( keyStr.substring(0,4) );
        if( year >= YearName.YEAR_2008.getYear() && year <= YearName.YEAR_2020.getYear())
            yearRangeDataList.add("{" + "\"" + keyStr + "\"" + " : " + monthlyData.get(keyStr) + "}");
    }

    private void writeToFile( List<String> yearRangeData ) {

        yearRangeData.forEach(yearData -> {
            try {
                bw.append( yearData );
                bw.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
