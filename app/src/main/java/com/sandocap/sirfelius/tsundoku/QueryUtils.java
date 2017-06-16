package com.sandocap.sirfelius.tsundoku;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to querying the Google Books API and parsing the response JSON data.
 */

class QueryUtils {

    /** Log String */
    private static final String LOG_TAG = QueryUtils.class.getName();

    /** private constructor, {@link QueryUtils} should never be instantiated */
    private QueryUtils() {}

    /**
     * Query the Google Books API and return a {@link List<Book>} to represent a list of books.
     **/
    static List<Book> fetchBookData(String requestUrl) {
        // Create URL from requestUrl String
        URL url = createUrl(requestUrl);

        // TODO: Perform HTTP request to the URL and receive a JSON response back.

        // TODO: Return a real list instead.
        return new ArrayList<>();
    }


    /**
     * Returns an {@link URL} from a String
     * */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL: ", e);
        }

        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as response
     **/
    private String makeHttpRequest(URL url) throws IOException {
        String response = "";

        if (url == null) {
            return response;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the query results: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the response from the server.
     **/
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                reader.readLine();
            }
        }

        return output.toString();
    }
}
