package api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private final static int connectTimeout = 3000;
    private final static String apiKey = System.getenv("API_KEY");
    private HttpURLConnection connection;

    public HttpClient(String sectorSymbol) throws IOException {

        this.connection = getHttpConn(sectorSymbol);
    }

    public InputStream getApiData() throws IOException {

        return connection.getResponseCode() == 200 ? connection.getInputStream() : null;
    }

    private HttpURLConnection getHttpConn( String sectorSymbol ) throws IOException {

        String queryParams = "function=TIME_SERIES_MONTHLY&symbol=";
        String apiUrlString = "https://www.alphavantage.co/query";

        final URL requestUrl = new URL(apiUrlString + "?" + queryParams + sectorSymbol + "&apikey=" + apiKey);
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout( connectTimeout );
        return connection;
    }
}
