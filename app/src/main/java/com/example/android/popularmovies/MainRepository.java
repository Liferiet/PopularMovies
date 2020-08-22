package com.example.android.popularmovies;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.FavouriteEntry;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.ui.MainActivity;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();

    private static MainRepository sInstance;

    private final AppDatabase mDatabase;

    private MainRepository(final AppDatabase database) {
        mDatabase = database;
    }

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

    public void loadAllFavourites(final MutableLiveData<List<FavouriteEntry>> favourites) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<FavouriteEntry> favouriteEntries = mDatabase.favouriteDao().loadAllFavourites();
            Log.d(TAG, "in loadAllFavourites");
            Log.d(TAG, String.valueOf(favouriteEntries));
            favourites.postValue(favouriteEntries);
        });
    }

    public void loadPopular(final MutableLiveData<List<MovieModel>> movies, String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            URL url = NetworkUtils.generateUrlSortByPopular(apiKey);

            String jsonMovieResponse = null;
            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                String noDataString = "none";
                ArrayList<MovieModel> list = JsonMovieUtils.getMovieListFromJson(jsonMovieResponse, noDataString);
                movies.postValue(list);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadTopRated(final MutableLiveData<List<MovieModel>> movies, String apiKey) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            URL url = NetworkUtils.generateUrlSortByTopRated(apiKey);

            String jsonMovieResponse = null;
            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                String noDataString = "none";
                ArrayList<MovieModel> list = JsonMovieUtils.getMovieListFromJson(jsonMovieResponse, noDataString);
                movies.postValue(list);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

}
