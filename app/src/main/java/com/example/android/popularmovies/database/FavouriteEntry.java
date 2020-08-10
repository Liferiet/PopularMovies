package com.example.android.popularmovies.database;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.android.popularmovies.model.MovieModel;

@Entity(tableName = "favourite_movie")
@TypeConverters({UriConverter.class})
public class FavouriteEntry extends MovieModel implements Parcelable {

/*    @PrimaryKey(autoGenerate = true)
    private int id;*/
    @ColumnInfo(name = "external_id")
    private int externalId;
    /*private String title;*/
/*    @ColumnInfo(name = "poster_uri")
    private String moviePosterUri;*/

    public FavouriteEntry() {
    }

    @Ignore
    public FavouriteEntry(int externalId, String title, Uri posterUri) {
        this.externalId = externalId;
        super.setTitle(title);
        super.setMoviePosterUri(posterUri);
    }

    public FavouriteEntry(int id, int externalId, String title, Uri posterUri) {
        super(id, title, posterUri);
/*        this.id = id;*/
        this.externalId = externalId;
/*        this.title = title;
        this.moviePosterUri = posterUri;*/
    }

    @Ignore
    protected FavouriteEntry(Parcel in) {
        super();
        super.setId(in.readInt());
        externalId = in.readInt();
        super.setTitle(in.readString());
        super.setMoviePosterUri((Uri) in.readValue(Uri.class.getClassLoader()));
    }

    @Ignore
    public static final Creator<FavouriteEntry> CREATOR = new Creator<FavouriteEntry>() {
        @Override
        public FavouriteEntry createFromParcel(Parcel in) {
            return new FavouriteEntry(in);
        }

        @Override
        public FavouriteEntry[] newArray(int size) {
            return new FavouriteEntry[size];
        }
    };

/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public int getExternalId() {
        return externalId;
    }

    public void setExternalId(int externalId) {
        this.externalId = externalId;
    }

/*    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUri() {
        return moviePosterUri;
    }

    public void setPosterUri(String posterUri) {
        this.moviePosterUri = posterUri;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(super.getId());
        parcel.writeInt(externalId);
        parcel.writeString(super.getTitle());
        parcel.writeString(super.getMoviePosterUri().toString());
    }
}
