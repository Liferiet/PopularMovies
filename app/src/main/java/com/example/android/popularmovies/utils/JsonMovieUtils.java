package com.example.android.popularmovies.utils;

import android.net.Uri;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility class that parse json format to java classes
 */
public class JsonMovieUtils {

    /**
     * Base URL for retrieve poster image from MovieBD database in w185 size
     */
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    /**
     * Base URL to create an URL to trailers
     */
    private static final String YOUTUBE_URL = "https://youtu.be/";

    /**
     * Parse json from MovieDB to list of movies
     * @param data data in json
     * @param noDataFallback if no data in some field (ex. "none")
     * @return list of movies
     * @throws JSONException
     */
    public static ArrayList<Movie> getMovieListFromJson (String data, String noDataFallback) throws JSONException {
        JSONObject fullJSONObject = new JSONObject(data);

        JSONArray results = fullJSONObject.optJSONArray("results");

        if (results == null) return null;

        ArrayList<Movie> movieList = new ArrayList<>(results.length());
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            Movie movie = new Movie();

            int id = movieJson.optInt("id", -1);
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

    /**
     * Parse json from MovieDB to list of trailers
     * @param data data in json
     * @return list of trailers
     * @throws JSONException
     */
    public static ArrayList<Trailer> getTrailersFromJson (String data) throws JSONException {
        JSONObject fullJSONObject = new JSONObject(data);

        JSONArray results = fullJSONObject.optJSONArray("results");

        if (results == null) return null;

        ArrayList<Trailer> trailersList = new ArrayList<>(results.length());

        for (int i = 0; i < results.length(); i++) {
            JSONObject trailerJSON = results.getJSONObject(i);

            String site = trailerJSON.optString("site");

            if (!site.equals("YouTube")) continue;

            String path = trailerJSON.optString("key");
            Uri uri = Uri.parse(YOUTUBE_URL + path);

            String name = trailerJSON.optString("name");

            Trailer trailer = new Trailer(name, uri);

            trailersList.add(trailer);
        }
        return trailersList;
    }

    /**
     * Parse json from MovieDB to list of reviews
     * @param data data in json
     * @return list of reviews
     * @throws JSONException
     */
    public static ArrayList<Review> getReviewFromJson (String data) throws JSONException {
        JSONObject fullJSONObject = new JSONObject(data);

            JSONArray results = fullJSONObject.optJSONArray("results");

            if (results == null) return null;

            ArrayList<Review> reviewsList = new ArrayList<>(results.length());

            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewJSON = results.getJSONObject(i);

                String author = reviewJSON.getString("author");
                String content = reviewJSON.getString("content");

                Review review = new Review(author, content);

                reviewsList.add(review);
        }
            return reviewsList;
    }
}
