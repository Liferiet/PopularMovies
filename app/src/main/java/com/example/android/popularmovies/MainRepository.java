package com.example.android.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

    public LiveData<List<FavouriteEntry>> loadAllFavourites() {
        LiveData<List<FavouriteEntry>> data = mDatabase.favouriteDao().loadAllFavourites();
        if (data == null) System.out.println("loadAllFav in repository - data is null");
        if (data != null) {
            System.out.println("loadAllFav in repository - livedata is not null, but what's inside?");
            System.out.println(data.getValue());
        }
        return data;
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
                System.out.println("Background thread: loadPopular: " + list);
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
                System.out.println("Background thread: loadTopRated: " + list);
                movies.postValue(list);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

}
