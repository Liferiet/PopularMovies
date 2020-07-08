package com.example.android.popularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.popularmovies.R;

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


    public static URL generateUrlSortByTopRated(Context context) {
        String path = MOVIE + TOP_RATED;
        return generateUrl(context, path);
    }

    public static URL generateUrlSortByPopular(Context context) {
        String path = MOVIE + POPULAR;
        return generateUrl(context, path);
    }

    public static URL generateUrlVideosForMovie(Context context, int movieId) {
        String path = MOVIE + "/" + movieId + VIDEOS;
        return generateUrl(context, path);
    }

    public static URL generateUrlReviewsForMovie(Context context, int movieId) {
        String path = MOVIE + "/" + movieId + REVIEWS;
        return generateUrl(context, path);
    }

    private static URL generateUrl (Context context, String path) {
        String api = context.getResources().getString(R.string.API_KEY);

        String baseUrl = BASE_URL + path;
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api)
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
