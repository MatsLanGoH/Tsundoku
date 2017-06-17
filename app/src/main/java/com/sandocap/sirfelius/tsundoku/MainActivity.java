package com.sandocap.sirfelius.tsundoku;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Activity Log String */
    static final String LOG_TAG = MainActivity.class.getName();

    /** Mock string for API request */
    static final String API_QUERY_MOCK =
            "https://www.googleapis.com/books/v1/volumes?q=harry+inauthor:rowling&langRestrict=en";


    /**
     * Adapter for a list of books
     **/
    private BookAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout.
        ListView bookListView = (ListView) findViewById(R.id.book_list);

        // Set the empty View on the {@link ListView}
        // in case the list cannot be populated.
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as an input.
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface.
        bookListView.setAdapter(mAdapter);


        // Get a reference to EditText field
        EditText editText = (EditText) findViewById(R.id.search_query);

        // Test to see if we can get some output from the API for now
        // TODO: Use a loader instead of the AsyncTask
        new BookAsyncTask().execute(API_QUERY_MOCK);
    }

    /**
     * AsyncTask to download Book data
     **/
    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            // Do nothing if there are no valid URLs.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Perform the HTTP request for book data and process the response.
            return QueryUtils.fetchBookData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.v(LOG_TAG, String.valueOf(books));

            // Clear the adapter of previous book data.
            mAdapter.clear();

            // If there is a valid list of {@link Book}s, add them to the adapter's data set.
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
        }
    }

}
