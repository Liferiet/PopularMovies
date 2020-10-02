package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.viewmodel.MainViewModel;
import com.example.android.popularmovies.viewmodel.MainViewModelFactory;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * Activity launched during application startup
 * Displays movie list for current selected option. Default option after application startup
 * is "popular". Options can be changed in the drawer.
 */
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.OnListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    private MovieAdapter mAdapter;

    /**
     * Initializes respective ViewModel and observes the list of movies in it.
     * Creates view with: adapter with list of movies, menu and drawer.
     * @param savedInstanceState information saved before app was paused or rotated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModelFactory factory = new MainViewModelFactory(
                AppDatabase.getInstance(getApplicationContext()), getString(R.string.API_KEY));
        mViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbarInclude.mainToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);


        mBinding.loadingDataPb.setVisibility(View.VISIBLE);

        setupDrawerContent(mBinding.navView);

        GridLayoutManager manager = new GridLayoutManager(this,
                GridDecorator.calculateNoOfColumns(this));
        mBinding.recyclerviewMovies.setLayoutManager(manager);
        mBinding.recyclerviewMovies.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this);

        mViewModel.getCurrentMovies().observe(this, movies -> {
            mBinding.loadingDataPb.setVisibility(View.INVISIBLE);
            mAdapter.setMovieData((ArrayList<Movie>) movies);
            mBinding.recyclerviewMovies.setAdapter(mAdapter);
            showResults();
        });

    }

    /**
     * Inflates custom menu layout
     * @param menu menu object
     * @return true if menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Adds behavior to menu options
     * @param item menu item
     * @return true if option selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showResults() {
        mBinding.errorMessageTv.setVisibility(View.INVISIBLE);
        mBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mBinding.recyclerviewMovies.setVisibility(View.INVISIBLE);
        mBinding.errorMessageTv.setVisibility(View.VISIBLE);
    }

    /**
     * Listener method that handles movie click in adapter
     */
    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie", movie);

        startActivity(intent);
    }

    /**
     * Setup drawer and adds behavior to click events
     * @param navigationView element in view with drawer
     */
    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if (menuItem.isChecked()) {
                        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }

                    mBinding.loadingDataPb.setVisibility(View.VISIBLE);
                    mAdapter.setMovieData(new ArrayList<>(0));

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
        mViewModel.loadFavourites();
    }

    private void loadPopular() {
        mViewModel.loadPopular();
    }

    private void loadTopRated() {
        mViewModel.loadTopRated();
    }

}