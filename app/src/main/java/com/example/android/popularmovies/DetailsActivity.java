package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;

public class DetailsActivity extends AppCompatActivity {

    private final String TAG = DetailsActivity.class.getSimpleName();

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.d(TAG, "onCreate");

        Intent intentThatStartedActivity = getIntent();
        Bundle extras = intentThatStartedActivity.getExtras();

        if (extras != null) {
            mMovie = new Movie();
            System.out.println(extras.getString("title"));
            System.out.println(extras.getString("originalTitle"));
            System.out.println(extras.getString("moviePosterUri"));
            System.out.println(extras.getString("overview"));
            System.out.println(extras.getString("userRating"));
            System.out.println(extras.getString("releaseDate"));
        }
    }
}
