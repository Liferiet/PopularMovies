package com.example.android.popularmovies.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.model.Movie;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Movie mMovie;
    private final String mApiKey;

    public DetailsViewModelFactory(Movie movie, String apiKey) {
        mMovie = movie;
        mApiKey = apiKey;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(mMovie, mApiKey);
    }
}
