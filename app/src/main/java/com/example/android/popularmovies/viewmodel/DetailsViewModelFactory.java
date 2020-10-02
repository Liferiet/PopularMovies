package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;

/**
 * Factory for DetailsViewModel so that it can take the parameters in constructor
 */
public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDatabase;
    private final Movie mMovie;
    private final String mApiKey;

    /**
     * Constructor
     * @param database instance created at application startup
     * @param movie the movie about which information will be displayed
     * @param apiKey application API_KEY
     */
    public DetailsViewModelFactory(AppDatabase database, Movie movie, String apiKey) {
        mDatabase = database;
        mMovie = movie;
        mApiKey = apiKey;
    }

    /**
     * This method is not called by developer
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(mDatabase, mMovie, mApiKey);
    }
}
