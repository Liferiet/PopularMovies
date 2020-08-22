package com.example.android.popularmovies.database;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.android.popularmovies.model.MovieModel;

@Entity(tableName = "favourite_movie")
@TypeConverters({UriConverter.class})
public class FavouriteEntry extends MovieModel implements Parcelable {

    @ColumnInfo(name = "external_id")
    private int externalId;

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
        this.externalId = externalId;
    }

    @Ignore
    protected FavouriteEntry(Parcel in) {
        super();
        setId(in.readInt());
        externalId = in.readInt();
        setTitle(in.readString());
        setMoviePosterUri((Uri) in.readValue(Uri.class.getClassLoader()));
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

    public int getExternalId() {
        return externalId;
    }

    public void setExternalId(int externalId) {
        this.externalId = externalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(externalId);
        parcel.writeString(getTitle());
        parcel.writeString(getMoviePosterUri().toString());
    }
}
