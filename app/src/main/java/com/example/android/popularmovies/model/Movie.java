package com.example.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

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

    protected Movie(Parcel in) {
        title = in.readString();
        originalTitle = in.readString();
        moviePosterUri = in.readParcelable(Uri.class.getClassLoader());
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeValue(moviePosterUri);
        parcel.writeString(overview);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
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
