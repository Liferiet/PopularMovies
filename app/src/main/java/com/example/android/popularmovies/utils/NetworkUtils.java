package com.example.android.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String BASE_URL ="http://api.themoviedb.org/3";
    private final static String API_KEY_PARAM = "api_key";
    private final static String MOVIE = "/movie";

    public final static String TOP_RATED = "/top_rated";
    public final static String POPULAR = "/popular";
    public final static String VIDEOS = "/videos";
    public final static String REVIEWS = "/reviews";


    public static URL generateUrlSortByTopRated(String apiKey) {
        String path = MOVIE + TOP_RATED;
        return generateUrl(apiKey, path);
    }

    public static URL generateUrlSortByPopular(String apiKey) {
        String path = MOVIE + POPULAR;
        return generateUrl(apiKey, path);
    }

    public static URL generateUrlVideosForMovie(String apiKey, int movieId) {
        String path = MOVIE + "/" + movieId + VIDEOS;
        return generateUrl(apiKey, path);
    }

    public static URL generateUrlReviewsForMovie(String apiKey, int movieId) {
        String path = MOVIE + "/" + movieId + REVIEWS;
        return generateUrl(apiKey, path);
    }

    private static URL generateUrl (String apiKey, String path) {

        String baseUrl = BASE_URL + path;
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream input = connection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }

    }
}
