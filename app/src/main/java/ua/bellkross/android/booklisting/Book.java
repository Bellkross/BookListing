package ua.bellkross.android.booklisting;


public class Book {

    private String mTitle;
    private String mAuthors;
    private long mPublishedDate;
    private int mPageCount;
    private String mUrl;

    public Book(String title, String authors, long publishedDate, int pageCount, String url) {
        mTitle = title;
        mAuthors = authors;
        mPublishedDate = publishedDate;
        mPageCount = pageCount;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String authors) {
        mAuthors = authors;
    }

    public long getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        mPublishedDate = publishedDate;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthors + '\'' +
                ", mPublishedDate=" + mPublishedDate +
                ", mPageCount=" + mPageCount +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
