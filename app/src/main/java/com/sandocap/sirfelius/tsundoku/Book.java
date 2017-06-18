package com.sandocap.sirfelius.tsundoku;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * {@link Book} represents a single Book.
 * Each object has 7 properties:
 * title, author, title url, snippet, publish date, image url and page count.
 */

class Book {
    private final static String LOG_TAG = Book.class.getName();

    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mSnippet;
    private String mPublishedDate;
    private String mImageUrl;
    private int mPageCount;

    /**
     * Create a new {@link Book} object.
     *
     * TODO: Insert @param definitions.
     * */
    Book(String title, String author, String url, String snippet, String publishedDate, String imageUrl, int pageCount) {
        mTitle = title;
        mAuthor = author;
        mUrl = url;
        mSnippet = snippet;
        mPublishedDate = publishedDate;
        mImageUrl = imageUrl;
        mPageCount = pageCount;
    }

    /** Get the title of the Book. */
    String getTitle() {
        return mTitle;
    }

    /** Get the author of the Book. */
    String getAuthor() {
        return mAuthor;
    }

    /** Get the url of the Book. */
    String getUrl() {
        return mUrl;
    }

    /** Get the snippet of the Book. */
    String getSnippet() {
        return mSnippet;
    }

    /** Get the published date of the Book. */
    String getPublishedDate() {
        return mPublishedDate;
    }

    /** Get the imageUrl of the Book. */
    URL getImageUrl() {
        try {
            return new URL(mImageUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Invalid imageUrl", e);
        }
        return null;
    }

    /** Get the page count of the Book. */
    int getPageCount() {
        return mPageCount;
    }
}
