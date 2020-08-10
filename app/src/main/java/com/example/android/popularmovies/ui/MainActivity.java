package com.example.android.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        MovieAdapter.OnListItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int LOADER_IDENTIFIER = 409;
    private final String PATH_EXTRA = "path-extra";

    private ActivityMainBinding mBinding;

    private MovieAdapter mAdapter;
    private Bundle savedInstance;
    private Spinner mSpinner;

    private boolean mFirstSpinnerUse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Stetho.initializeWithDefaults(this);

        mFirstSpinnerUse = true;
        savedInstance = savedInstanceState;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (mBinding.navView != null) {
            setupDrawerContent(mBinding.navView);
        }

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mBinding.recyclerviewMovies.setLayoutManager(manager);
        mBinding.recyclerviewMovies.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mBinding.recyclerviewMovies.setAdapter(mAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.d("Main onCreate", "savedInstance is null so load new data");
            loadData(NetworkUtils.POPULAR);
            //currentSpinnerOption = getString(R.string.popular_sort);
        } else {
            Log.d("Main onCreate", "load data from savedInstance");
            ArrayList<MovieModel> list = savedInstanceState.getParcelableArrayList("movies");
            mAdapter.setMovieData(list);
            //currentSpinnerOption = savedInstanceState.getString("spinner_option");
        }

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
        mBinding.errorMessageTv.setVisibility(View.INVISIBLE);
        mBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mBinding.recyclerviewMovies.setVisibility(View.INVISIBLE);
        mBinding.errorMessageTv.setVisibility(View.VISIBLE);

    }

    public void initializeSpinner(Spinner spinner) {
        mSpinner = spinner;
        final List<String> options = new ArrayList<>();
        options.add(getString(R.string.popular_sort));
        options.add(getString(R.string.top_rated_sort));
        //options.add(getString(R.string.favourite_sort));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (savedInstance != null) {
            spinner.setSelection(savedInstance.getInt("spinner", 0));
        }
    }

    // Listener method for spinner options
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mFirstSpinnerUse) {
            mFirstSpinnerUse = false;
            return;
        }
        mAdapter = new MovieAdapter(this);
        mBinding.recyclerviewMovies.setAdapter(mAdapter);

        Log.d("main spinner", "item selected - load new data");
        if (adapterView.getItemAtPosition(i).toString().equals("popularity")) {
            Log.d(TAG, "Spinner: popular selected");
            loadData(NetworkUtils.POPULAR);
            //currentSpinnerOption = getString(R.string.popular_sort);
        } else if (adapterView.getItemAtPosition(i).toString().equals("rating")) {
            Log.d(TAG, "Spinner: top rated selected");
            loadData(NetworkUtils.TOP_RATED);
            //currentSpinnerOption = getString(R.string.top_rated_sort);
        }
    }

/*    private void setupViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(AppDatabase.getInstance(getApplicationContext()));
        MainViewModel viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        viewModel.getFavourites().observe(this, new Observer<List<FavouriteEntry>>() {
            @Override
            public void onChanged(List<FavouriteEntry> favouriteEntries) {
                Log.d(TAG, "Updating list of favourites from LiveData in ViewModel");
                ArrayList<FavouriteEntry> fav = (ArrayList<FavouriteEntry>) favouriteEntries;
                ArrayList<MovieModel> list = new ArrayList<MovieModel>(fav);
                mAdapter.setMovieData(list);
            }
        });
    }*/

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mBinding.drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Not needed
    }

    // Listener method for RecyclerView's adapter
    @Override
    public void onListItemClick(MovieModel movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<MovieModel> list =  mAdapter.getMovieData();

/*        if ((getString(R.string.favourite_sort).equals(currentSpinnerOption)) &&
        list == null) {
            list = new ArrayList<>();
        }*/
        if (list != null) outState.putParcelableArrayList("movies", list);
        if (mSpinner != null) {
            outState.putInt("spinner", mSpinner.getSelectedItemPosition());
        }

        //outState.putString("spinner_option", currentSpinnerOption);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieModel>>(this) {

            ArrayList<MovieModel> cache;

            @Override
            protected void onStartLoading() {
                if (cache != null) deliverResult(cache);
                else {
                    mBinding.loadingDataPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public ArrayList<MovieModel> loadInBackground() {
                String path = args.getString(PATH_EXTRA);
                if (path == null || TextUtils.isEmpty(path)) return null;

                String apiKey = MainActivity.this.getString(R.string.API_KEY);

                URL url;
                if (path.equals(NetworkUtils.TOP_RATED)) url = NetworkUtils.generateUrlSortByTopRated(apiKey);
                else if (path.equals(NetworkUtils.POPULAR)) url = NetworkUtils.generateUrlSortByPopular(apiKey);
                else throw new IllegalArgumentException();

                Log.d("Loader Main", "fetching data from internet");
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
            public void deliverResult(ArrayList<MovieModel> data) {
                cache = data;
                super.deliverResult(data);
                Log.d("Loader Main", "Cache stored");
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        mBinding.loadingDataPb.setVisibility(View.INVISIBLE);

        if (data != null) {
            showResults();
            mAdapter.setMovieData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {

    }

}
