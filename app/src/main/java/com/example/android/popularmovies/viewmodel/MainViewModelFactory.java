package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.AppDatabase;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDb;
    private final String mApiKey;

    // COMPLETED (3) Initialize the member variables in the constructor with the parameters received
    public MainViewModelFactory(AppDatabase database, String apiKey) {
        mDb = database;
        mApiKey = apiKey;
    }

    // COMPLETED (4) Uncomment the following method
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mDb, mApiKey);
    }
}
