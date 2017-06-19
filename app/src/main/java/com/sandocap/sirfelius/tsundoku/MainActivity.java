package com.sandocap.sirfelius.tsundoku;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /** Activity Log String */
    static final String LOG_TAG = MainActivity.class.getName();

    /** Mock string for API request */
    // TODO: Use an UriBuilder to create the request address.
    static final String API_BASE_URL_MOCK =
            "https://www.googleapis.com/books/v1/volumes?";
    private static String apiQuery =
            "Madness";

    /** Constant value for the book loader ID. */
    private static final int BOOK_LOADER_ID = 12;

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

        // Find a reference to the {@link Button} in the layout.
        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide on screen keyboard if present.
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // Hide keyboard if present
                inputMethodManager.hideSoftInputFromWindow(
                        null == getCurrentFocus() ? null : getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Then start search.
                submitSearch(v);
            }
        });

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

        // Set item click listener on the ListView to send an intent to web browser
        // so users can open a website with information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object to pass into the Intent constructor
                Uri bookUri = null;
                if (currentBook != null) {
                    bookUri = Uri.parse(currentBook.getUrl());
                }

                // Create a new Intent to view the book URI.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the Intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader with the constant ID defined above, and
        // this activity for the LoaderCallbacks parameter.
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);


    }

    /* The following methods were implemented through the LoaderCallbacks interface */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        // Create a new Loader for the given URL.
        return new BookLoader(this, String.format("%sq=%s", API_BASE_URL_MOCK, apiQuery) );

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Hide progress indicator
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.result_no_books_found);

        // Clear the adapter of previous earthquake data.
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's data set.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Clear the existing data from the adapter.
        mAdapter.clear();
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
            // Clear the adapter of previous book data.
            mAdapter.clear();

            // If there is a valid list of {@link Book}s, add them to the adapter's data set.
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
        }
    }

    /**
     * This method is called when the search button is clicked.
     */
    public void submitSearch(View view) {
        // Get text from search field.
        EditText editText = (EditText) findViewById(R.id.search_query);
        apiQuery = editText.getText().toString();

        // Test to see if we can get some output from the API for now
        // TODO: Use a loader instead of the AsyncTask
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

//        new BookAsyncTask().execute(String.format("%sq=%s", API_BASE_URL_MOCK, searchQuery));
    }

}
