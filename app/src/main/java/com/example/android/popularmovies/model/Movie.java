package com.example.android.popularmovies.model;

import android.net.Uri;

public class Movie {

    private String title;
    private String originalTitle;
    private Uri moviePosterUri;
    private String overview;
    private String userRating;
    private String releaseDate;

    public Movie() {
    }

    public Movie(String title, String originalTitle, Uri moviePosterUri, String overview, String userRating, String releaseDate) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.moviePosterUri = moviePosterUri;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Uri getMoviePosterUri() {
        return moviePosterUri;
    }

    public void setMoviePosterUri(Uri moviePosterUri) {
        this.moviePosterUri = moviePosterUri;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
