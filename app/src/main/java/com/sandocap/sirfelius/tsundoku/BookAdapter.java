package com.sandocap.sirfelius.tsundoku;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link BookAdapter} is an {@link ArrayAdapter} that can provide a layout
 * for each list based on a data source, i.e. a list of {@link Book} objects.
 */

class BookAdapter extends ArrayAdapter<Book> {

    /** Log string */
    private static final String LOG_TAG = BookAdapter.class.getName();

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file,
     * and the list is the data to be populated into the lists.
     *
     * @param context   The current context. Used to inflate the layout file.
     * @param books     A list of Book objects to display in the list.
     */
    BookAdapter(Activity context, ArrayList<Book> books) {
        // Initialize the ArrayAdapter's internal storage.
        super(context, 0, books);
    }

    /**
     * Provides a View for an AdapterView (ListView, GridView)
     *
     * @param position      The position in the list of data to be displayed.
     * @param convertView   The recycled View to be populated.
     * @param parent        The parent ViewGroup that is used for inflation.
     * @return the view for the position in the AdapterView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing View is being reused, otherwise inflate the View.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.book_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list.
        Book currentBook = getItem(position);

        if (currentBook != null) {
            // TODO: Implement book covers!

            // Find the TextView in the book_item.xml with the ID book_title.
            TextView bookTitleView = (TextView) listItemView.findViewById(R.id.book_title);

            // Get book title for current book.
            String bookTitle = currentBook.getTitle();

            // Display the title of the current book in that TextView.
            bookTitleView.setText(bookTitle);

        }

        return listItemView;
    }
}
