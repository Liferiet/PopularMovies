package com.example.android.popularmovies.ui;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Utility class for grid adapter
 */
public class GridDecorator {

    /**
     * Calculates how many movies can fit into one row
     * @param context activity context
     * @return number of movies in one row
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
