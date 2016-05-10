package cornell.cs5412;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper functions for network operations
 * Created by Pablo on 3/10/2016.
 */
public class NetworkUtil {
    public static final String NETWORK_DEBUG_TAG = "NETWORK_DEBUG_TAG";

    public static boolean checkConnection(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    // Based off Android developer training "Connecting to the Network"
    private static HttpResponse httpCall(String myurl, String method, String content) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 5000;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Host", "ec2-54-175-7-114.compute-1.amazonaws.com");
            if ((method.equals("POST") || method.equals("PUT")) && content != null && content.length() > 0) {
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                OutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(content.getBytes("UTF-8"));
                out.flush();
                out.close();
            } else {
                conn.setDoInput(true);
            }

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(NETWORK_DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return new HttpResponse(contentAsString, response);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
            if (conn != null) conn.disconnect();
        }
    }

    public static HttpResponse httpGet(String myurl, String content) throws IOException {
        return httpCall(myurl, "GET", content);
    }

    public static HttpResponse httpPut(String myurl, String content) throws IOException {
        return httpCall(myurl, "PUT", content);
    }

    public static HttpResponse httpPost(String myurl, String content) throws IOException {
        return httpCall(myurl, "POST", content);
    }

    // Reads an InputStream and converts it to a String of length len.
    // From Android developer training "Connecting to the Network"
    public static String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
