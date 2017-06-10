package com.example.priyankanandiraju.popularmovies.helper;

import com.example.priyankanandiraju.popularmovies.utilities.Movie;
import com.example.priyankanandiraju.popularmovies.utilities.MovieReviewsList;
import com.example.priyankanandiraju.popularmovies.utilities.MovieVideosList;
import com.example.priyankanandiraju.popularmovies.utilities.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.priyankanandiraju.popularmovies.helper.Constants.API_KEY_PARAM;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.MOVIE_DETAILS;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.MOVIE_REVIEWS;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.PATH_MOVIE_ID;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.POPULAR_MOVIES;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.TOP_RATED_MOVIES;
import static com.example.priyankanandiraju.popularmovies.helper.Constants.MOVIE_VIDEOS;

/**
 * Created by priyankanandiraju on 3/27/17.
 */

public interface MoviesAPIService {

    @GET(POPULAR_MOVIES)
    Call<MoviesList> getPopularMovies(@Query(API_KEY_PARAM) String apiKey);

    @GET(TOP_RATED_MOVIES)
    Call<MoviesList> getTopRatedMovies(@Query(API_KEY_PARAM) String apiKey);

    @GET(MOVIE_VIDEOS)
    Call<MovieVideosList> getMovieVideos(@Path(PATH_MOVIE_ID) String movieId, @Query(API_KEY_PARAM) String apiKey);

    @GET(MOVIE_REVIEWS)
    Call<MovieReviewsList> getMovieReviews(@Path(PATH_MOVIE_ID) String movieId, @Query(API_KEY_PARAM) String apiKey);

    @GET(MOVIE_DETAILS)
    Call<Movie> getMovieDetails(@Path(PATH_MOVIE_ID) String movieId, @Query(API_KEY_PARAM) String apiKey);
}
