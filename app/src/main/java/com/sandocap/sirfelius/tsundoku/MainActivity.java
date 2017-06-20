package com.sandocap.sirfelius.tsundoku;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /**
     * Activity Log String
     */
    static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Base String for API request
     */
    // TODO: Use an UriBuilder to create the request address.
    static final String BOOKS_API_BASE_URL =
            "https://www.googleapis.com/books/v1/volumes?";
    /**
     * String to hold API query
     */
    private static String apiQuery =
            "";

    /**
     * Constant value for the book loader ID.
     */
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

        /* Reference to SearchView field. */
        final SearchView searchView = (SearchView) findViewById(R.id.search_query);

        // Expand and clear focus.
        searchView.setIconified(false);
        searchView.clearFocus();

        // Implement listener for searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Hide on screen keyboard if present.
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // Hide keyboard if present
                inputMethodManager.hideSoftInputFromWindow(
                        null == getCurrentFocus() ? null : getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Get text from search field and update apiQuery.
                apiQuery = query;

                // Remove focus from searchView
                searchView.clearFocus();

                // Then start search.
                submitSearch();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

                // Get text from search field and update apiQuery.
                apiQuery = searchView.getQuery().toString();

                // Remove focus from searchView
                searchView.clearFocus();

                // Then start search.
                submitSearch();
            }
        });

        // Find a reference to the {@link ListView} in the layout.
        ListView bookListView = (ListView) findViewById(R.id.book_list);

        // Set the empty View on the {@link ListView} in case the list cannot be populated.
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

        /* Following section is used to check connectivity */
        // Get a reference to the ConnectivityManager to check the state of network connectivity.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Get details on the currently active default network
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            // Get a reference to the LoaderManager to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader with the constant ID defined above, and
            // this activity for the LoaderCallbacks parameter.
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Hide progress indicator
            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(GONE);

            // Disable search button
            button.setEnabled(false);

            // Display error
            mEmptyStateTextView.setText(R.string.no_internet_connection_string);
        }

    }

    /* The following methods were implemented through the LoaderCallbacks interface */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        // Show progress indicator
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Create a new Loader for the given URL if there is a query string.
        return new BookLoader(this, String.format("%sq=%s", BOOKS_API_BASE_URL, apiQuery));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Hide progress indicator
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(GONE);

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
     * This method is called when the search button is clicked.
     */
    public void submitSearch() {
        // Clear error text
        mEmptyStateTextView.setText("");

        // Restart loader if we have a query string
        if (apiQuery.length() > 0) {
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
        }
    }
}
