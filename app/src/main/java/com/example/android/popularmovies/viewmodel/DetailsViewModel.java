package com.example.android.popularmovies.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.DetailsRepository;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Stores data for DetailsActivity.
 * Has reference to repository that is responsible for the entire data retrieval logic
 */
public class DetailsViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private Movie mMovie;
    private AtomicBoolean mIsFavourite;

    private MutableLiveData<ArrayList<Review>> mReviews;
    private MutableLiveData<ArrayList<Trailer>> mTrailers;

    private DetailsRepository mRepository;

    private String mApiKey;

    /**
     * Constructor
     * Checks if a movie is in the database as favourite and loads reviews and trailers for this
     * movie
     * @param database instance created at application startup
     * @param apiKey application API_KEY retrieved from remote source
     */
    public DetailsViewModel(AppDatabase database, Movie movie, String apiKey) {
        Log.d(TAG, "Preparing main viewModel");
        mRepository = DetailsRepository.getInstance(database);
        mApiKey = apiKey;
        mMovie = movie;

        mIsFavourite = new AtomicBoolean(false);

        isFavourite();

        mReviews = new MutableLiveData<>();
        mTrailers = new MutableLiveData<>();

        loadReviewsForMovie();
        loadTrailersForMovie();
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

    public void addFavourite() {
        mRepository.addFavourite(mMovie);
    }

    public void removeFavourite() {
        mRepository.removeFavourite(mMovie);
    }

    /**
     * Checks if movie is in database as favourite and writes the result in second parameter
     */
    private void isFavourite() {
        mRepository.isFavourite(mMovie, mIsFavourite);
    }

    private void loadReviewsForMovie() {
        mRepository.loadReviews(mReviews, mApiKey, mMovie);
    }

    private void loadTrailersForMovie() {
        mRepository.loadTrailers(mTrailers, mApiKey, mMovie);
    }

    public boolean getIsFavourite() {
        return mIsFavourite.get();
    }
}
