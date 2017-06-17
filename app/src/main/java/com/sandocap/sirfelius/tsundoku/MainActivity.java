package com.sandocap.sirfelius.tsundoku;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Activity Log String */
    static final String LOG_TAG = MainActivity.class.getName();

    /** Mock string for API request */
    static final String API_QUERY_MOCK =
            "https://www.googleapis.com/books/v1/volumes?q=harry+inauthor:rowling&langRestrict=en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a placeholder book
        // TODO: Use a real book instead.
        Book testBook = new Book("How to tame an SDK", "Brody Clankerwaft", "https://www.goodreads.com", "She was a seamstress. Now she puts Docstrings to REST.", "Jun 16, 2017", 1337);

        // Create a String to display (since we don't have an adapter yet)
        // TODO: Well, implement that adapter.
        String testBookString = testBook.getAuthor() + ": "
                + testBook.getTitle() + "\n"
                + "\"" + testBook.getSnippet() + "\"\n"
                + "Published on: " + testBook.getPublishedDate() + " / "
                + String.valueOf(testBook.getPageCount()) + " pages\n"
                + "More information: " + testBook.getUrl() + "\n";

        // Make sure that TextView works
        // TODO: Replace this with an actual call to an adapter
        TextView textView = (TextView) findViewById(R.id.simple_text_view);
        textView.setText(testBookString);

        // Test to see if we can get some output from the API for now
        // TODO: Use a loader instead of the AsyncTask
        new BookAsyncTask().execute(API_QUERY_MOCK);
    }

    /**
     * AsyncTask to download Book data
     **/
    private class BookAsyncTask extends AsyncTask<String, Void, Book> {

        @Override
        protected Book doInBackground(String... urls) {
            // Do nothing if there are no valid URLs.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Perform the HTTP request for book data and process the response.
            List<Book> results = QueryUtils.fetchBookData(urls[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Book book) {
            Log.v(LOG_TAG, book.toString());
        }
    }

}
