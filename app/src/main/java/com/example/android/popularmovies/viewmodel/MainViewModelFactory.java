package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.AppDatabase;

/**
 * Factory for MainViewModel so that it can take the parameters in constructor
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDb;
    private final String mApiKey;

    /**
     * Constructor
     * @param database instance created at application startup
     * @param apiKey application API_KEY
     */
    public MainViewModelFactory(AppDatabase database, String apiKey) {
        mDb = database;
        mApiKey = apiKey;
    }

    /**
     * This method is not called by developer
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mDb, mApiKey);
    }
}
