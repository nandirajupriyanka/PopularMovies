package com.example.priyankanandiraju.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.priyankanandiraju.popularmovies.data.FavouriteMoviesContract.FavouriteMovieEvent;

/**
 * Created by priyankanandiraju on 3/11/17.
 */

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FavouriteMovies.db";
    public static final int DATABASE_VERSION = 1;
    private static FavouriteMoviesDbHelper mDbHelper;

    public static synchronized FavouriteMoviesDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mDbHelper == null) {
            mDbHelper = new FavouriteMoviesDbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }


    public FavouriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + FavouriteMovieEvent.TABLE_NAME + " (" +
                FavouriteMovieEvent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_IMAGE_PATH + " TEXT, " +
                FavouriteMovieEvent.COLUMN_IS_FAV + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
