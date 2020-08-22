package com.example.android.popularmovies;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsRepository {

    private static DetailsRepository sInstance;

    private DetailsRepository() {

    }

    public static DetailsRepository getInstance() {
        if (sInstance == null) {
            synchronized (DetailsRepository.class) {
                if (sInstance == null) {
                    sInstance = new DetailsRepository();
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
}
