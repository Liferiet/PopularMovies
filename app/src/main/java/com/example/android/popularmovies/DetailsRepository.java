package com.example.android.popularmovies;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;
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

public class DetailsRepository {

    private static DetailsRepository sInstance;

    private final AppDatabase mDatabase;

    private DetailsRepository(final AppDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

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

    public void addFavourite(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            FavouriteEntry favourite = new FavouriteEntry(movie.getId(),
                    movie.getTitle(), movie.getMoviePosterUri());
            mDatabase.favouriteDao().insertFavourite(favourite);
        });
    }

    public void removeFavourite(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            mDatabase.favouriteDao().deleteFavourite(movie.getId());
        });
    }

    public void isFavourite(Movie movie, final AtomicBoolean isFavourite) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            int id = mDatabase.favouriteDao().isFavourite(movie.getId());
            if (id > 0) isFavourite.set(true);
        });
    }
}
