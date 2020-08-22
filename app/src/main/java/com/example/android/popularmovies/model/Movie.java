package com.example.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie extends MovieModel implements Parcelable {

    private String originalTitle;
    private String overview;
    private String userRating;
    private String releaseDate;

    public Movie() {
    }

    public Movie(int id, String title, String originalTitle, Uri moviePosterUri, String overview, String userRating, String releaseDate) {
        super(id, title, moviePosterUri);
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    protected Movie(Parcel in) {
        super();
        super.setId(in.readInt());
        super.setTitle(in.readString());
        originalTitle = in.readString();
        super.setMoviePosterUri( (Uri) in.readValue(Uri.class.getClassLoader()) );
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(super.getId());
        dest.writeString(super.getTitle());
        dest.writeString(originalTitle);
        dest.writeValue(super.getMoviePosterUri());
        dest.writeString(overview);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}