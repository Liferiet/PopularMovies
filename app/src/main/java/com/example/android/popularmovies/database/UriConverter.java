package com.example.android.popularmovies.database;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriConverter {

    @TypeConverter
    public static Uri stringToUri(String string) {
        return string == null ? null : Uri.parse(string);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }
}
