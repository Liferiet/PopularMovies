package com.example.android.popularmovies.model;

import android.net.Uri;

public class Trailer {

    private String name;
    private Uri uri;

    public Trailer(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public Trailer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
