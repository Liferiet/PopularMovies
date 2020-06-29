package com.example.android.popularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonMovieUtils {

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    public static ArrayList<Movie> getMovieListFromJson (Context context, String data) throws JSONException {
        JSONObject fullJSONObject = new JSONObject(data);

        JSONArray results = fullJSONObject.optJSONArray("results");

        if (results == null) return null;

        ArrayList<Movie> movieList = new ArrayList<>(results.length());
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            Movie movie = new Movie();
            String fallback = context.getResources().getString(R.string.no_data);

            movie.setTitle(movieJson.optString("title", fallback));

            movie.setOriginalTitle(movieJson.optString("original_title", fallback));

            Uri movieImageUri = null;
            String movieImageString = movieJson.optString("poster_path");
            if (!"".equals(movieImageString)) {
                movieImageUri = Uri.parse(BASE_IMAGE_URL + movieImageString);
            }
            movie.setMoviePosterUri(movieImageUri);

            movie.setOverview(movieJson.optString("overview", fallback));

            movie.setReleaseDate(movieJson.optString("release_date", fallback));

            movie.setUserRating(movieJson.optString("vote_average", fallback));

            movieList.add(movie);
        }
        return movieList;
    }
}
