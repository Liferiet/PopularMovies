package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.AppDatabase;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDb;
    private final String mApiKey;

    public MainViewModelFactory(AppDatabase database, String apiKey) {
        mDb = database;
        mApiKey = apiKey;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mDb, mApiKey);
    }
}
