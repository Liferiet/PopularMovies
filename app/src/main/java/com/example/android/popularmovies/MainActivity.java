package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MovieAdapter.OnListItemClickListener {

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ProgressBar mLoadingProgressBar;
    private TextView mErrorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = findViewById(R.id.loading_data_pb);
        mErrorMessageTextView = findViewById(R.id.error_message_tv);

        mRecyclerView = findViewById(R.id.recyclerview_movies);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.sort_by_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        Log.v("MainActivity", "znaleziony spinner: " + spinner);
        initializeSpinner(spinner);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/
    public void loadData(String sortBy) {
        showResults();
        new FetchMoviesTask(this).execute(sortBy);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    @Override
    public void onListItemClick(Movie movie) {
        Log.d("MainActivity", "onListItemClick");

        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString("title", movie.getTitle());
        extras.putString("originalTitle", movie.getOriginalTitle());
        extras.putString("overview", movie.getOverview());
        Uri uri = movie.getMoviePosterUri();
        extras.putString("poster", uri.toString());
        extras.putString("userRating", movie.getUserRating());
        extras.putString("releaseDate", movie.getReleaseDate());

        intent.putExtras(extras);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private Context context;

        public FetchMoviesTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            if (strings.length == 0) return null;

            String sortBy = strings[0];
            URL url = null;
            String apiKey = context.getResources().getString(R.string.API_KEY);
            if (sortBy.equals(NetworkUtils.TOP_RATED)) url = NetworkUtils.generateUrlSortByTopRated(apiKey);
            else if (sortBy.equals(NetworkUtils.POPULAR)) url = NetworkUtils.generateUrlSortByPopular(apiKey);
            else return null;

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);

                List<Movie> moviesList = JsonMovieUtils.getMovieListFromJson(context, jsonMovieResponse);

                return moviesList;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);

            if (movies != null) {
                showResults();
                mAdapter.setMovieData(movies);
            }

            if (movies == null) {
                showErrorMessage();
            }
        }


    }
}
