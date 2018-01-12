package ua.bellkross.android.booklisting;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private ProgressBar mProgressBar;

    public BooksLoader(Context context, String url, ProgressBar progressBar) {
        super(context);
        mUrl = url;
        mProgressBar = progressBar;
    }

    public BooksLoader(Context context, ProgressBar progressBar) {
        super(context);
        mProgressBar = progressBar;
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books = QueryUtils.fetchBooksData(mUrl);
        return books;
    }

    @Override
    protected void onForceLoad() {
        mProgressBar.setVisibility(View.VISIBLE);
        super.onForceLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        mProgressBar.setVisibility(View.GONE);
        return super.onCancelLoad();
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }
}