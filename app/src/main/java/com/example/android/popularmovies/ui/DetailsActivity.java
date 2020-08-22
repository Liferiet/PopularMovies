package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.databinding.ActivityDetailsBinding;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.viewmodel.DetailsViewModel;
import com.example.android.popularmovies.viewmodel.DetailsViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = DetailsActivity.class.getSimpleName();

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
                AppDatabase.getInstance(getApplicationContext()), movie, getString(R.string.API_KEY));
        mViewModel = new ViewModelProvider(this, factory).get(DetailsViewModel.class);

        setTitle(getString(R.string.detail_activity_title));

        mBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_details);

        populateUiWithMovieData(movie);

        mViewModel.getTrailers().observe(this, this::loadUIWithTrailers);
        mViewModel.getReviews().observe(this, this::loadUIWithReviews);

        Log.d(TAG, "isFavourite: " + mViewModel.getIsFavourite());
        if (mViewModel.getIsFavourite()) mBinding.addToFavouriteButton.setChecked(true);
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

            ((TextView) v.findViewById(R.id.review_author_tv)).setText(author);
            ((TextView) v.findViewById(R.id.review_content_tv)).setText(content);

            if (i == reviews.size() - 1) {
                v.findViewById(R.id.divider).setVisibility(View.INVISIBLE);
            }

            mBinding.reviewsLinearWrapper.addView(v);
        }
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
            mViewModel.addFavourite();
            Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
        } else {
            mViewModel.removeFavourite();
            Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();
        }
    }
}
