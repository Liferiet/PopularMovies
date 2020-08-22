package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDatabase;
    private final Movie mMovie;
    private final String mApiKey;

    public DetailsViewModelFactory(AppDatabase database, Movie movie, String apiKey) {
        mDatabase = database;
        mMovie = movie;
        mApiKey = apiKey;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(mDatabase, mMovie, mApiKey);
    }
}
