package com.example.android.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ActivityDetailsBinding;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.viewmodel.DetailsViewModel;
import com.example.android.popularmovies.viewmodel.DetailsViewModelFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = DetailsActivity.class.getSimpleName();
    private final int LOADER_ID = 2431;

    private Movie mMovie;
    private ActivityDetailsBinding mBinding;

    private DetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intentThatStartedActivity = getIntent();

        if (!intentThatStartedActivity.hasExtra("movie")) {
            finish();
            return;
        }

        Movie movie = intentThatStartedActivity.getParcelableExtra("movie");

        DetailsViewModelFactory factory = new DetailsViewModelFactory(
                movie, getString(R.string.API_KEY));
        mViewModel = new ViewModelProvider(this, factory).get(DetailsViewModel.class);

        setTitle(getString(R.string.detail_activity_title));

        mBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);

        populateUiWithMovieData(movie);

        mViewModel.getTrailers().observe(this, this::loadUIWithTrailers);
        mViewModel.getReviews().observe(this, this::loadUIWithReviews);

        mViewModel.loadReviewsForMovie();
        mViewModel.loadTrailersForMovie();
    }

    private void populateUiWithMovieData(Movie movie) {
        mBinding.detailsTitleTv.setText(movie.getTitle());
        mBinding.detailMovieData.detailsOriginalTitleTv.setText(movie.getOriginalTitle());

        String rating = movie.getUserRating() + "/10.0";
        mBinding.detailMovieData.detailsRatingTv.setText(rating);

        mBinding.detailMovieData.detailsReleaseDateTv.setText(movie.getReleaseDate());
        mBinding.detailMovieOverview.detailsOverviewTv.setText(movie.getOverview());

        Uri posterUri = movie.getMoviePosterUri();
        Picasso.get().load(posterUri).placeholder(R.drawable.placeholder)
                .into(mBinding.detailsPosterIv);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    private void loadUIWithTrailers(ArrayList<Trailer> trailers) {
        LayoutInflater inflater = LayoutInflater.from(this);

        if (trailers.isEmpty()) {
            mBinding.noTrailers.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < trailers.size(); i++) {
            View v = inflater.inflate(R.layout.trailer_item, null);

            String trailerName = trailers.get(i).getName();
            ((TextView)v.findViewById(R.id.trailer_name_tv)).setText(trailerName);

            v.setTag(i);
            v.setOnClickListener(this);

            mBinding.trailersLinearWrapper.addView(v);
        }
    }

    private void loadUIWithReviews(ArrayList<Review> reviews) {
        LayoutInflater inflater = LayoutInflater.from(this);

        if (reviews.isEmpty()) {
            mBinding.noReviews.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < reviews.size(); i++) {
            View v = inflater.inflate(R.layout.review_item, null);

            String author = reviews.get(i).getAuthor();
            String content = reviews.get(i).getContent();

            System.out.println("Author: " + author + " content: " + content);
            ((TextView) v.findViewById(R.id.review_author_tv)).setText(author);
            ((TextView) v.findViewById(R.id.review_content_tv)).setText(content);

            if (i == reviews.size() - 1) {
                v.findViewById(R.id.divider).setVisibility(View.INVISIBLE);
            }

            mBinding.reviewsLinearWrapper.addView(v);
        }
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
                    String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(urlTrailers);
                    String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(urlReviews);

                    mMovie.setTrailers(JsonMovieUtils.getTrailersFromJson(jsonTrailersResponse));
                    mMovie.setReviews(JsonMovieUtils.getReviewFromJson(jsonReviewsResponse));

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
        loadUIWithTrailers();
        loadUIWithReviews();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Uri uri = mViewModel.getTrailers().getValue().get(position).getUri();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void clickFavouriteButton(View v) {
        boolean checked = mBinding.addToFavouriteButton.isChecked();
        if (checked) {
            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "unchecked", Toast.LENGTH_SHORT).show();
        }
    }
}
