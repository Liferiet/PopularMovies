package com.example.android.popularmovies.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movie")
public class FavouriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int externalId;
    private String title;
    @ColumnInfo(name = "poster_uri")
    private String posterUri;

    @Ignore
    public FavouriteEntry(int externalId, String title, String posterUri) {
        this.externalId = externalId;
        this.title = title;
        this.posterUri = posterUri;
    }

    public FavouriteEntry(int id, int externalId, String title, String posterUri) {
        this.id = id;
        this.externalId = externalId;
        this.title = title;
        this.posterUri = posterUri;
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

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }
}
