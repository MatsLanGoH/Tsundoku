package com.sandocap.sirfelius.tsundoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /** Activity Log String */
    static final String LOG_TAG = MainActivity.class.getName();

    /** Mock string for API request */
    static final String API_QUERY_MOCK =
            "https://www.googleapis.com/books/v1/volumes?q=harry+inauthor:rowling&results=1";

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
    }
}
