package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // TODO 3. do przeniesienia do jakis resourcersow czy cos
    private final static String BASE_URL ="http://api.themoviedb.org/3";
    private final static String API_KEY_PARAM = "api_key";

    public final static String TOP_RATED = "/movie/top_rated";
    public final static String POPULAR = "/movie/popular";

    public static URL generateUrlSortByTopRated(String API_KEY) {
        return generateUrl(TOP_RATED, API_KEY);
    }

    public static URL generateUrlSortByPopular(String API_KEY) {
        return generateUrl(POPULAR, API_KEY);
    }

    private static URL generateUrl (String requestSortBy, String API_KEY) {
        String sortBy = "";
        if (requestSortBy.equals(TOP_RATED)) {
            sortBy = TOP_RATED;
        } else if (requestSortBy.equals(POPULAR)) {
            sortBy = POPULAR;
        } else {
            return null;
        }
        String baseUrl = BASE_URL + sortBy;
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

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
