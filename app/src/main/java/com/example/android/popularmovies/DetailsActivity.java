package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.android.popularmovies.databinding.ActivityDetailsBinding;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.JsonMovieUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private final String TAG = DetailsActivity.class.getSimpleName();
    private final int LOADER_ID = 2431;

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intentThatStartedActivity = getIntent();

        if (!intentThatStartedActivity.hasExtra("movie")) {
            finish();
            return;
        }

        mMovie = intentThatStartedActivity.getParcelableExtra("movie");

        setTitle(getString(R.string.detail_activity_title));

        ActivityDetailsBinding mBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);

        mBinding.detailsTitleTv.setText(mMovie.getTitle());
        mBinding.detailMovieData.detailsOriginalTitleTv.setText(mMovie.getOriginalTitle());

        String rating = mMovie.getUserRating() + "/10.0";
        mBinding.detailMovieData.detailsRatingTv.setText(rating);

        mBinding.detailMovieData.detailsReleaseDateTv.setText(mMovie.getReleaseDate());
        mBinding.detailMovieOverview.detailsOverviewTv.setText(mMovie.getOverview());

        Uri posterUri = mMovie.getMoviePosterUri();
        Picasso.get().load(posterUri).placeholder(R.drawable.placeholder)
                .into(mBinding.detailsPosterIv);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            Movie cache;

            @Override
            protected void onStartLoading() {
                if (cache != null) deliverResult(cache);
                else {
                    //mBinding.loadingDataPb.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public Movie loadInBackground() {

                String apiKey = DetailsActivity.this.getString(R.string.API_KEY);
                int id = mMovie.getId();
                Log.d("details loadInBg", "id: " + id);
                URL urlTrailers = NetworkUtils.generateUrlVideosForMovie(apiKey, id);
                URL urlReviews = NetworkUtils.generateUrlReviewsForMovie(apiKey, id);

                Log.d("Loader Details", "fetching data from internet");
                try {
                    String noDataString = DetailsActivity.this.getString(R.string.no_data);

                    String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(urlTrailers);
                    String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(urlReviews);

                    mMovie.setTrailers(JsonMovieUtils.getTrailersFromJson(jsonTrailersResponse));
                    return mMovie;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(Movie data) {
                cache = data;
                super.deliverResult(data);
                Log.d("Loader Main", "Cache stored");
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
