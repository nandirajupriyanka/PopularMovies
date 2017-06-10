package com.example.priyankanandiraju.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.priyankanandiraju.popularmovies.data.FavouriteMoviesContract.FavouriteMovieEvent;

/**
 * Created by priyankanandiraju on 3/11/17.
 */

public class FavouriteMoviesProvider extends ContentProvider {
    public static final String LOG_TAG = FavouriteMoviesProvider.class.getSimpleName();
    private static final int FAVOURITE_MOVIES = 100;
    private static final int FAVOURITE_MOVIE_ITEM = 200;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(FavouriteMoviesContract.CONTENT_AUTHORITY, FavouriteMoviesContract.PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        sUriMatcher.addURI(FavouriteMoviesContract.CONTENT_AUTHORITY, FavouriteMoviesContract.PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIE_ITEM);
    }

    private FavouriteMoviesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = FavouriteMoviesDbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE_MOVIES:
                cursor = database.query(FavouriteMovieEvent.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVOURITE_MOVIE_ITEM:
                selection = selection + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(FavouriteMovieEvent.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE_MOVIES:
                return FavouriteMovieEvent.CONTENT_LIST_TYPE;
            case FAVOURITE_MOVIE_ITEM:
                return FavouriteMovieEvent.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE_MOVIES:
                return insertFavouriteMovie(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertFavouriteMovie(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(FavouriteMovieEvent.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE_MOVIES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(FavouriteMovieEvent.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVOURITE_MOVIE_ITEM:
                // Delete a single row given by the ID in the URI
                selection = FavouriteMovieEvent._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FavouriteMovieEvent.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
