package com.sandocap.sirfelius.tsundoku;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import javax.net.ssl.HttpsURLConnection;

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
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Return the List<Book>
        return extractItemFromJson(jsonResponse);
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
     * Make an HTTPs request to the given URL and return a String as response
     **/
    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";

        if (url == null) {
            return response;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
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
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     **/
    private static List<Book> extractItemFromJson(String jsonResponse) {
        // Create an empty ArrayList to hold Book objects.
        List<Book> books = new ArrayList<>();

        try {
            // Convert jsonResponse into a JSONObject.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            // Extract "items" JSONArray
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            // Loop through each item in the array.
            for (int i = 0; i < itemsArray.length(); i++) {

                // Get book JSONObject at position i
                JSONObject currentBook = itemsArray.getJSONObject(i);

                // Extract "volumeInfo" JSONObject
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract "title" for title
                String title = volumeInfo.getString("title");

                // Extract "authors" JSONArray
                JSONArray authors = volumeInfo.getJSONArray("authors");

                // Extract first author in authors as author
                // TODO: Deal with multiple authors.
                String author = authors.getString(0);


                // Extract "accessInfo" JSONObject
                JSONObject accessInfo = currentBook.getJSONObject("accessInfo");

                // Extract "webReaderLink" for url
                // TODO: Include buy link?
                String url = accessInfo.getString("webReaderLink");

                // Extract "imageLinks" JSONObject
                JSONObject imageLinks = currentBook.getJSONObject("imageLinks");

                // Extract "smallThumbnail" for imageUrl
                String imageUrl = imageLinks.getString("smallThumbnail");

                // Extract "description" for snippet
                // TODO: Shorten texts?
                String snippet = volumeInfo.getString("description");

                // Extract "publishedDate" for publishedDate
                String publishedDate = volumeInfo.getString("publishedDate");

                // Extract "pageCount" for pageCount
                // TODO: Set "0" pages for books without pageCount info for now.
                // We still need to handle pages with undefined pageCount in the Adapter.
                int pageCount = 0;
                if (volumeInfo.has("pageCount")) {
                    pageCount = volumeInfo.getInt("pageCount");
                }

                // Create Book object from JSON data
                Book book = new Book(title, author, url, snippet, publishedDate, imageUrl, pageCount);

                // Add Book to list of books.
                books.add(book);
            }

        } catch (JSONException e) {
            // Handle invalid JSON data
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the list of books.
        return books;
    }
}
