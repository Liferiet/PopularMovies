package com.example.android.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonMovieUtils {

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    public static ArrayList<Movie> getMovieListFromJson (String data, String noDataFallback) throws JSONException {
        JSONObject fullJSONObject = new JSONObject(data);

        JSONArray results = fullJSONObject.optJSONArray("results");

        if (results == null) return null;

        ArrayList<Movie> movieList = new ArrayList<>(results.length());
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            Movie movie = new Movie();

            int id = movieJson.optInt("id", -1);
            Log.d("Json getMovieList", "movie id: " + id);
            movie.setId(id);

            movie.setTitle(movieJson.optString("title", noDataFallback));

            movie.setOriginalTitle(movieJson.optString("original_title", noDataFallback));

            Uri movieImageUri = null;
            String movieImageString = movieJson.optString("poster_path");
            if (!"".equals(movieImageString)) {
                movieImageUri = Uri.parse(BASE_IMAGE_URL + movieImageString);
            }
            movie.setMoviePosterUri(movieImageUri);

            movie.setOverview(movieJson.optString("overview", noDataFallback));

            movie.setReleaseDate(movieJson.optString("release_date", noDataFallback));

            movie.setUserRating(movieJson.optString("vote_average", noDataFallback));

            movieList.add(movie);
        }
        return movieList;
    }

    public static ArrayList<Uri> getTrailersFromJson (String data) throws JSONException {
        JSONObject fullJsonObject = new JSONObject(data);
        System.out.println(data);
        return null;
    }
}
