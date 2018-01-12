package ua.bellkross.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 0;
    public static final String BASIC_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private SearchView mSearchView;
    private String mUrl;
    private BooksAdapter mBooksAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private ListView mListView;
    private BooksLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mListView = findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyView);

        mBooksAdapter = new BooksAdapter(this, new ArrayList<Book>());
        mListView.setAdapter(mBooksAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = mBooksAdapter.getItem(position);

                Uri bookUri = Uri.parse(book.getUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(webIntent);
            }
        });

        mLoader = (BooksLoader) getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_books, menu);

        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mUrl = BASIC_URL + query;
                mLoader.setUrl(mUrl);
                mLoader.forceLoad();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BooksLoader(this, mProgressBar);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mBooksAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mBooksAdapter.addAll(data);
        }

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            mEmptyView.setText(R.string.no_books);
        } else {
            mEmptyView.setText(R.string.no_internet);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBooksAdapter.clear();
    }

}
