package com.example.android.popularmovies;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Class responsible for communication with database and network sources
 *  and handles the entire logic of operations
 */
public class DetailsRepository {

    private static DetailsRepository sInstance;

    private final AppDatabase mDatabase;

    private DetailsRepository(final AppDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    /**
     * Returns instance of repository
     * @param database instance created at application startup
     */
    public static DetailsRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DetailsRepository.class) {
                if (sInstance == null) {
                    sInstance = new DetailsRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Retrieves reviews for the movie from remote data source
     * @param reviews the list that will be returned with the data
     * @param apiKey application API_KEY
     * @param movie a movie about which information is needed
     */
    public void loadReviews(MutableLiveData<ArrayList<Review>> reviews, String apiKey, Movie movie) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            int id = movie.getId();
            URL urlReviews = NetworkUtils.generateUrlReviewsForMovie(apiKey, id);

            Log.d("DetailsRepository", "fetching reviews data from internet");
            try {
                String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(urlReviews);
                reviews.postValue(JsonMovieUtils.getReviewFromJson(jsonReviewsResponse));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Retrieves trailers for the movie from remote data source
     * @param trailers the list that will be returned with the data
     * @param apiKey application API_KEY
     * @param movie a movie about which information is needed
     */
    public void loadTrailers(MutableLiveData<ArrayList<Trailer>> trailers, String apiKey, Movie movie) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            int id = movie.getId();
            URL urlTrailers = NetworkUtils.generateUrlVideosForMovie(apiKey, id);

            Log.d("DetailsRepository", "fetching trailers data from internet");
            try {
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(urlTrailers);
                trailers.postValue(JsonMovieUtils.getTrailersFromJson(jsonTrailersResponse));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Adds a movie to the database
     * @param movie a movie to add to the database
     */
    public void addFavourite(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() ->
                mDatabase.favouriteDao().insertFavourite(movie));
    }

    /**
     * Removes a movie from the database
     * @param movie a movie to remove from the database
     */
    public void removeFavourite(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() ->
            mDatabase.favouriteDao().deleteFavourite(movie.getId()));
    }

    /**
     * Checks if movie is in database as a favourite
     * @param movie a movie to check if it is a favourite
     * @param isFavourite value to return with data
     */
    public void isFavourite(Movie movie, final AtomicBoolean isFavourite) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            int id = mDatabase.favouriteDao().isFavourite(movie.getId());
            if (id > 0) isFavourite.set(true);
        });
    }
}
