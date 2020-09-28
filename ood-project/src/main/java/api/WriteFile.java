package api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;

public class WriteFile {

    private final String[] symbols;
    private final String[] fullFilePath;
    private final HttpClientConnection httpConnInst;
    private static BufferedWriter bw;

    public WriteFile(String[] fileName) throws URISyntaxException {

        this.fullFilePath = createFile( fileName );
        this.httpConnInst = new HttpClientConnection();
        this.symbols = getSectorNames( fileName );
    }

    public void getApiDataAndWriteToFile() throws IOException, ParseException, InterruptedException {

        for( int i = 0; i < symbols.length; i++ ) {
            if( i == 5 || i == 9 )                 // API call frequency is 5 calls per minute
               Thread.sleep( 60000);
            writeData(i);
        }
    }

    private void writeData( int index ) throws IOException, ParseException {

        bw = new BufferedWriter( new FileWriter( fullFilePath[index], true) );
        InputStream is   = httpConnInst.getApiData( symbols[index] );
        parseApiData( is );
        bw.flush();
        bw.close();
    }


    public String[] createFile( String[] fileName ) throws URISyntaxException {

        File directories = new File( getPath() );
        isDirExist(directories);
        return getFullPath( fileName );
    }

    private String[] getFullPath( String[] fileName ) throws URISyntaxException {

        String[] fullPath = new String[ fileName.length ];

        for( int i = 0; i < fileName.length; i++ )
            fullPath[i] = getPath() + fileName[i];

        return fullPath;
    }

    private void isDirExist(File directories) {

        if ( !directories.exists() )
            directories.mkdirs();
    }

    private static String getPath() throws URISyntaxException {

        String DirPath = ClassLoader.getSystemClassLoader().getResource("").toURI().getPath();
        DirPath = DirPath.substring( 0, DirPath.indexOf("classes") );
        DirPath += "resources" + File.separator + "apidatafiles" + File.separator;
        return DirPath;
    }

    private void parseApiData( InputStream is ) throws IOException, ParseException {

        parseJson( writeDataToBuffer( is ) );
    }

    private String writeDataToBuffer( InputStream inputStream ) throws IOException {

        String apiData = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                apiData += line;
        }
        return apiData;
    }

    private void parseJson( String apiData ) throws ParseException, IOException {

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse( apiData );
        JSONObject monthlyData = (JSONObject) responseJson.get("Monthly Time Series");
        getYearRangeData( monthlyData);
    }

    private void getYearRangeData(JSONObject monthlyData) throws IOException {

        for (Object key : monthlyData.keySet()) {
            String keyStr = (String) key;
            int year = Integer.parseInt( keyStr.substring(0,4) );

            if( year >= 2008 && year <= 2020) {
                Object value = monthlyData.get(keyStr);
                writeToFile( "\"" + keyStr + "\"" + " : " + value );
            }
        }
    }

    private void writeToFile( String yearRangeData ) throws IOException {

        bw.append( yearRangeData );
        bw.newLine();
    }

    private String[] getSectorNames( String[] fileName ){

        String[] sectorNames = new String[ fileName.length ];

        for( int i = 0; i < fileName.length; i++ )
            sectorNames[i] = fileName[i].substring(0,(fileName[i].lastIndexOf(".")));
        return sectorNames;
    }
}
