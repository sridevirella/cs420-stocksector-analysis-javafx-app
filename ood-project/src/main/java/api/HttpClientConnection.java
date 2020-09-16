package api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientConnection {

    final static int connectTimeout = 3000;
    private final static String apiKey = System.getenv("API_KEY");

    public InputStream getApiData( String sectorSymbol ) throws IOException {

        String queryParams = "function=TIME_SERIES_MONTHLY&symbol=";
        HttpURLConnection connection = getHttpConn(queryParams + sectorSymbol + "&apikey=" + apiKey);
        return ConnStatus( connection, connection.getResponseCode() );
    }

    private InputStream ConnStatus( HttpURLConnection connection, int status ) throws IOException {
        InputStream is = null;

        if ( status == 200 )
             is = connection.getInputStream();
        else
            System.out.println( "Request failed with response code, "+connection.getResponseCode() );

        return is;
    }

    private HttpURLConnection getHttpConn( String queryParams ) throws IOException {

        String apiUrlString = "https://www.alphavantage.co/query";

        final URL requestUrl = new URL(apiUrlString + "?" + queryParams );
        HttpURLConnection httpConnection = (HttpURLConnection) requestUrl.openConnection();
        httpConnection.setConnectTimeout( connectTimeout );

        return httpConnection;
    }
}
