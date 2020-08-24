package com.example.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.android.popularmovies.database.UriConverter;

import java.util.ArrayList;
import java.util.Formatter;

@Entity(tableName = "favourite_movie")
@TypeConverters({UriConverter.class})
public class Movie implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "internal_id")
    private int internalId;
    private int id;
    private String title;
    @ColumnInfo(name = "poster_uri")
    private Uri moviePosterUri;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    private String overview;
    @ColumnInfo(name = "user_rating")
    private String userRating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public Movie() {
    }

    @Ignore
    public Movie(int id, String title, String originalTitle, Uri moviePosterUri, String overview, String userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.moviePosterUri = moviePosterUri;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getInternalId() {
        return internalId;
    }

    public void setInternalId(int internalId) {
        this.internalId = internalId;
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

    @Ignore
    protected Movie(Parcel in) {
        setId(in.readInt());
        setTitle(in.readString());
        originalTitle = in.readString();
        setMoviePosterUri((Uri) in.readValue(Uri.class.getClassLoader()));
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
        dest.writeInt(getId());
        dest.writeString(getTitle());
        dest.writeString(originalTitle);
        dest.writeValue(getMoviePosterUri());
        dest.writeString(overview);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    @Ignore
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