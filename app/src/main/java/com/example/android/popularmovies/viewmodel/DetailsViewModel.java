package com.example.android.popularmovies.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.DetailsRepository;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import java.util.ArrayList;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private Movie mMovie;

    private MutableLiveData<ArrayList<Review>> mReviews;
    private MutableLiveData<ArrayList<Trailer>> mTrailers;

    private DetailsRepository mRepository;

    private String mApiKey;

    public DetailsViewModel(Movie movie, String apiKey) {
        Log.d(TAG, "Preparing main viewModel");
        mRepository = DetailsRepository.getInstance();
        mApiKey = apiKey;

        mReviews = new MutableLiveData<>();
        mTrailers = new MutableLiveData<>();

        mMovie = movie;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public LiveData<ArrayList<Trailer>> getTrailers() {
        return mTrailers;
    }

    public LiveData<ArrayList<Review>> getReviews() {
        return mReviews;
    }

    public void loadReviewsForMovie() {
        mRepository.loadReviews(mReviews, mApiKey, mMovie);
    }

    public void loadTrailersForMovie() {
        mRepository.loadTrailers(mTrailers, mApiKey, mMovie);
    }
}
