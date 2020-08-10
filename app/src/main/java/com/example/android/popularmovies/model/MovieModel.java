package com.example.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class MovieModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @ColumnInfo(name = "poster_uri")
    private Uri moviePosterUri;

    public MovieModel() {
    }

    public MovieModel(int id, String title, Uri moviePosterUri) {
        this.id = id;
        this.title = title;
        this.moviePosterUri = moviePosterUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getMoviePosterUri() {
        return moviePosterUri;
    }

    public void setMoviePosterUri(Uri moviePosterUri) {
        this.moviePosterUri = moviePosterUri;
    }
}
