package com.sandocap.sirfelius.tsundoku;

/**
 * {@link Book} represents a single Book.
 * Each object has 6 properties: title, author, title url, snippet, publish date, and page count.
 */

class Book {
    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mSnippet;
    private String mPublishedDate;
    private int mPageCount;

    /**
     * Create a new {@link Book} object.
     *
     * TODO: Insert @param definitions.
     * */
    Book(String title, String author, String url, String snippet, String publishedDate, int pageCount) {
        mTitle = title;
        mAuthor = author;
        mUrl = url;
        mSnippet = snippet;
        mPublishedDate = publishedDate;
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

    /** Get the page count of the Book. */
    int getPageCount() {
        return mPageCount;
    }
}
