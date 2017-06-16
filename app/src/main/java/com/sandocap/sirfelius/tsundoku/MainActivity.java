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

        // Make sure that TextView works
        TextView textView = (TextView) findViewById(R.id.simple_text_view);
        textView.setText(API_QUERY_MOCK);
    }
}
