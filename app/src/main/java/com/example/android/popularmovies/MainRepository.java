package com.example.android.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class responsible for communication with database and network sources
 *  and handles the entire logic of operations
 */
public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();

    private static MainRepository sInstance;
    private final AppDatabase mDatabase;

    private MainRepository(final AppDatabase database) {
        mDatabase = database;
    }

    /**
     * Returns instance of repository
     * @param database instance created at application startup
     */
    public static MainRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (MainRepository.class) {
                if (sInstance == null) {
                    sInstance = new MainRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Reads the entire list of movies from the database
     * @return LiveData object with list of favourites movies
     */
    public LiveData<List<Movie>> loadAllFavourites() {
        return mDatabase.favouriteDao().loadAllFavourites();
    }

    /**
     * Retrieve list of popular movies from remote data source
     * @param movies the list that will be returned with the data
     * @param apiKey application API_KEY
     */
    public void loadPopular(final MutableLiveData<List<Movie>> movies, String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            URL url = NetworkUtils.generateUrlSortByPopular(apiKey);

            String jsonMovieResponse;
            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                String noDataString = "none";
                ArrayList<Movie> list = JsonMovieUtils.getMovieListFromJson(jsonMovieResponse, noDataString);
                movies.postValue(list);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Retrieve list of top rated movies from remote data source
     * @param movies the list that will be returned with the data
     * @param apiKey application API_KEY
     */
    public void loadTopRated(final MutableLiveData<List<Movie>> movies, String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            URL url = NetworkUtils.generateUrlSortByTopRated(apiKey);

            String jsonMovieResponse;
            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                String noDataString = "none";
                ArrayList<Movie> list = JsonMovieUtils.getMovieListFromJson(jsonMovieResponse, noDataString);
                movies.postValue(list);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

}
