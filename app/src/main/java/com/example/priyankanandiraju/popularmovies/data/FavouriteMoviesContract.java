package com.example.priyankanandiraju.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by priyankanandiraju on 3/11/17.
 */

public class FavouriteMoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.priyankanandiraju.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE_MOVIES = "favMovies";

    public FavouriteMoviesContract() {
    }

    public static final class FavouriteMovieEvent implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVOURITE_MOVIES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;

        public static final String TABLE_NAME = "entry";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FAV_MOVIE_ID = "movieId";
        public static final String COLUMN_FAV_MOVIE_TITLE = "title";
        public static final String COLUMN_FAV_MOVIE_IMAGE_PATH = "imagePath";
        public static final String COLUMN_IS_FAV = "isFavourite";
    }
}
