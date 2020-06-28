package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private final String TAG = DetailsActivity.class.getSimpleName();

    private TextView mOriginalTitleTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.d(TAG, "onCreate");

        Intent intentThatStartedActivity = getIntent();
        Bundle extras = intentThatStartedActivity.getExtras();

        if (extras == null) {
            finish();
            return;
        }

        mOriginalTitleTextView = findViewById(R.id.details_original_title_tv);
        mRatingTextView = findViewById(R.id.details_rating_tv);
        mReleaseDateTextView = findViewById(R.id.details_release_date_tv);
        mOverviewTextView = findViewById(R.id.details_overview_tv);
        mPosterImageView = findViewById(R.id.details_poster_iv);

        mMovie = new Movie();

        setTitle(extras.getString("title"));

        mOriginalTitleTextView.setText(extras.getString("originalTitle"));
        mRatingTextView.setText(extras.getString("userRating"));
        mReleaseDateTextView.setText(extras.getString("releaseDate"));
        mOverviewTextView.setText(extras.getString("overview"));

        Uri posterUri = Uri.parse(extras.getString("poster"));
        Picasso.get().load(posterUri).into(mPosterImageView);



    }
}
