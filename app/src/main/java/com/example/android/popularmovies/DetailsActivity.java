package com.example.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovies.databinding.ActivityDetailsBinding;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private final String TAG = DetailsActivity.class.getSimpleName();

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
        mBinding.detailsOriginalTitleTv.setText(mMovie.getOriginalTitle());

        String rating = mMovie.getUserRating() + "/10.0";
        mBinding.detailsRatingTv.setText(rating);

        mBinding.detailsReleaseDateTv.setText(mMovie.getReleaseDate());
        mBinding.detailsOverviewTv.setText(mMovie.getOverview());

        Uri posterUri = mMovie.getMoviePosterUri();
        Picasso.get().load(posterUri).into(mBinding.detailsPosterIv);

    }
}
