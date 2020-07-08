package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        MovieAdapter.OnListItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private final int LOADER_IDENTIFIER = 409;
    private final String PATH_EXTRA = "path-extra";

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ProgressBar mLoadingProgressBar;
    private TextView mErrorMessageTextView;

    private boolean mFirstSpinnerUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstSpinnerUse = true;

        mLoadingProgressBar = findViewById(R.id.loading_data_pb);
        mErrorMessageTextView = findViewById(R.id.error_message_tv);

        mRecyclerView = findViewById(R.id.recyclerview_movies);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            loadData(NetworkUtils.POPULAR);
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList("movies");
            mAdapter.setMovieData(list);
        }

        getSupportLoaderManager().initLoader(LOADER_IDENTIFIER, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.sort_by_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        initializeSpinner(spinner);
        return true;
    }

    public void loadData(String sortBy) {
        showResults();
        Bundle bundle = new Bundle();
        bundle.putString(PATH_EXTRA, sortBy);
        getSupportLoaderManager().restartLoader(LOADER_IDENTIFIER, bundle, this);
    }

    public void showResults() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);

    }

    public void initializeSpinner(Spinner spinner) {
        final List<String> options = new ArrayList<>();
        options.add(getString(R.string.popular_sort));
        options.add(getString(R.string.top_rated_sort));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    // Listener method for spinner options
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mFirstSpinnerUse) {
            mFirstSpinnerUse = false;
            return;
        }
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (adapterView.getItemAtPosition(i).toString().equals("popularity")) {
            loadData(NetworkUtils.POPULAR);
        }
        if (adapterView.getItemAtPosition(i).toString().equals("rating")) {
            loadData(NetworkUtils.TOP_RATED);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Not needed
    }

    // Listener method for RecyclerView's adapter
    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Movie> list =  mAdapter.getMovieData();
        outState.putParcelableArrayList("movies", list);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> cache;

            @Override
            protected void onStartLoading() {
                if (cache != null) deliverResult(cache);
                else {
                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public ArrayList<Movie> loadInBackground() {
                String path = args.getString(PATH_EXTRA);
                if (path == null || TextUtils.isEmpty(path)) return null;

                String apiKey = MainActivity.this.getString(R.string.API_KEY);

                URL url;
                if (path.equals(NetworkUtils.TOP_RATED)) url = NetworkUtils.generateUrlSortByTopRated(apiKey);
                else if (path.equals(NetworkUtils.POPULAR)) url = NetworkUtils.generateUrlSortByPopular(apiKey);
                else throw new IllegalArgumentException();

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                    String noDataString = MainActivity.this.getString(R.string.no_data);
                    return JsonMovieUtils.getMovieListFromJson(jsonMovieResponse, noDataString);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<Movie> data) {
                cache = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);

        if (data != null) {
            showResults();
            mAdapter.setMovieData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

}
