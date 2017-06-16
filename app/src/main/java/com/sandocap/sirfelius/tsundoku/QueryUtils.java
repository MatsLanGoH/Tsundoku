package com.sandocap.sirfelius.tsundoku;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
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
     * Returns an {@link URL} from a String
     * */
    private URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL: ", e);
        }

        return url;
    }
}
