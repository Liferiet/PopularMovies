package com.example.android.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.popularmovies.MainViewModel;
import com.example.android.popularmovies.MainViewModelFactory;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;
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
        MovieAdapter.OnListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModelFactory factory = new MainViewModelFactory(
                AppDatabase.getInstance(getApplicationContext()), getString(R.string.API_KEY));
        mViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbarInclude.mainToolbar);

        mBinding.loadingDataPb.setVisibility(View.VISIBLE);

        setupDrawerContent(mBinding.navView);

        GridLayoutManager manager = new GridLayoutManager(this,
                GridDecorator.calculateNoOfColumns(this));
        mBinding.recyclerviewMovies.setLayoutManager(manager);
        mBinding.recyclerviewMovies.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mBinding.recyclerviewMovies.setAdapter(mAdapter);


        mViewModel.getCurrentMovies().observe(this, movies -> {
            mBinding.loadingDataPb.setVisibility(View.INVISIBLE);
            mAdapter.setMovieData((ArrayList<MovieModel>)movies);
            showResults();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void showResults() {
        mBinding.errorMessageTv.setVisibility(View.INVISIBLE);
        mBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mBinding.recyclerviewMovies.setVisibility(View.INVISIBLE);
        mBinding.errorMessageTv.setVisibility(View.VISIBLE);
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if (menuItem.isChecked()){
                        return true;
                    }

                    switch (menuItem.getItemId()) {
                        case R.id.nav_load_favourites:
                            loadFavourites();
                            break;
                        case R.id.nav_load_popular:
                            loadPopular();
                            break;
                        case R.id.nav_load_top_rated:
                            loadTopRated();
                            break;
                    }

                    mBinding.drawerLayout.closeDrawers();
                    return true;
                });
    }

    private void loadFavourites() {
        mBinding.loadingDataPb.setVisibility(View.VISIBLE);
        mViewModel.loadFavourites();
    }

    private void loadPopular() {
        mBinding.loadingDataPb.setVisibility(View.VISIBLE);
        mViewModel.loadPopular();
    }

    private void loadTopRated() {
        mBinding.loadingDataPb.setVisibility(View.VISIBLE);
        mViewModel.loadTopRated();
    }

    // Listener method for RecyclerView's adapter
    @Override
    public void onListItemClick(MovieModel movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);

        startActivity(intent);
    }

}
