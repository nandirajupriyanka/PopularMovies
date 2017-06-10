package com.example.priyankanandiraju.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyankanandiraju.popularmovies.helper.Constants;
import com.example.priyankanandiraju.popularmovies.helper.MoviesAPI;
import com.example.priyankanandiraju.popularmovies.helper.MoviesAPIService;
import com.example.priyankanandiraju.popularmovies.helper.Utils;
import com.example.priyankanandiraju.popularmovies.utilities.Movie;
import com.example.priyankanandiraju.popularmovies.utilities.MovieReview;
import com.example.priyankanandiraju.popularmovies.utilities.MovieReviewsList;
import com.example.priyankanandiraju.popularmovies.utilities.MovieVideo;
import com.example.priyankanandiraju.popularmovies.utilities.MovieVideosList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.priyankanandiraju.popularmovies.MainActivity.MOVIE_ID;
import static com.example.priyankanandiraju.popularmovies.data.FavouriteMoviesContract.FavouriteMovieEvent;

/**
 * Created by priyankanandiraju on 3/8/17.
 */

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, MovieVideoAdapter.OnMovieTrailerClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    public static final String YOUTUBE_PARAM = "v";
    private TextView tvReleaseDate;
    private RatingBar ratingBar;
    private TextView tvSynopsis;
    private ConstraintLayout reviewBlock, trailerBlock;
    private String mId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView image;
    private ImageButton btnFavourite;

    private RecyclerView rvTrailer, rvReviews;
    private MovieReviewAdapter mReviewAdapter;
    private MovieVideoAdapter mVideosAdapter;
    private Movie mMovie;
    private MoviesAPIService mMoviesAPIService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            if (getIntent().hasExtra(MOVIE_ID)) {
                mId = getIntent().getStringExtra(MOVIE_ID);
            }
        }
        Log.v(TAG, "id " + mId);

        reviewBlock = (ConstraintLayout) findViewById(R.id.review_block);
        trailerBlock = (ConstraintLayout) findViewById(R.id.trailer_block);

        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        tvSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        image = (ImageView) findViewById(R.id.image);
        btnFavourite = (ImageButton) findViewById(R.id.btn_favourite);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.color_bg_ib));

        ratingBar.setIsIndicator(true);

        rvTrailer = (RecyclerView) findViewById(R.id.rv_trailers);
        rvTrailer.setHasFixedSize(true);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this));
        rvTrailer.setNestedScrollingEnabled(false);
        mVideosAdapter = new MovieVideoAdapter(this, new ArrayList<MovieVideo>(), this);

        rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        rvReviews.setHasFixedSize(true);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setNestedScrollingEnabled(false);
        mReviewAdapter = new MovieReviewAdapter(this, new ArrayList<MovieReview>());

        rvTrailer.setAdapter(mVideosAdapter);
        rvReviews.setAdapter(mReviewAdapter);
        btnFavourite.setOnClickListener(this);


        getLoaderManager().initLoader(0, null, this);

        createMoviesAPI();
        fetchMovieDetails();
        fetchMovieVideos();
        fetchMovieReviews();
    }

    private void fetchMovieDetails() {
        Call<Movie> movieDetailsCall = mMoviesAPIService.getMovieDetails(mId, BuildConfig.THE_MOVIE_DB_APP_KEY);
        movieDetailsCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    setData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, getString(R.string.unable_to_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieReviews() {
        Call<MovieReviewsList> movieReviewsListCall = mMoviesAPIService.getMovieReviews(mId, BuildConfig.THE_MOVIE_DB_APP_KEY);
        movieReviewsListCall.enqueue(new Callback<MovieReviewsList>() {
            @Override
            public void onResponse(Call<MovieReviewsList> call, Response<MovieReviewsList> response) {
                if (response.isSuccessful()) {
                    List<MovieReview> reviewList = response.body().getMovieReviewList();
                    if (reviewList.isEmpty()) {
                        reviewBlock.setVisibility(View.GONE);
                    } else {
                        reviewBlock.setVisibility(View.VISIBLE);
                        mReviewAdapter.setReviewData(reviewList);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieReviewsList> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, getString(R.string.unable_to_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieVideos() {
        Call<MovieVideosList> movieVideosListCall = mMoviesAPIService.getMovieVideos(mId, BuildConfig.THE_MOVIE_DB_APP_KEY);
        movieVideosListCall.enqueue(new Callback<MovieVideosList>() {
            @Override
            public void onResponse(Call<MovieVideosList> call, Response<MovieVideosList> response) {
                if (response.isSuccessful()) {
                    List<MovieVideo> videosList = response.body().getMovieVideoList();
                    if (videosList.isEmpty()) {
                        trailerBlock.setVisibility(View.GONE);
                    } else {
                        trailerBlock.setVisibility(View.VISIBLE);
                        mVideosAdapter.setVideoData(videosList);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieVideosList> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, getString(R.string.unable_to_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createMoviesAPI() {
        Retrofit retrofit = MoviesAPI.getClient();
        mMoviesAPIService = retrofit.create(MoviesAPIService.class);
    }

    private void setData(Movie movie) {
        mMovie = movie;
        collapsingToolbarLayout.setTitle(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvSynopsis.setText(movie.getOverview());
        Picasso.with(this)
                .load(Utils.generateImageUrl(movie.getImagePath()))
                .into(image);
        ratingBar.setRating(movie.getVoteAverage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_favourite:
                onFavouriteClicked();
                break;
            default:
                break;
        }
    }

    private void onFavouriteClicked() {

        AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                if (uri == null) {
                    Toast.makeText(MovieDetailActivity.this, R.string.failed_to_mark_as_favourite, Toast.LENGTH_SHORT).show();
                    btnFavourite.setPressed(false);
                } else {
                    Toast.makeText(MovieDetailActivity.this, R.string.successfully_saved_as_favourite, Toast.LENGTH_SHORT).show();
                    btnFavourite.setPressed(true);
                }
            }
        };

        // Save this movie as favourite
        int movieId = Integer.parseInt(mId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID, movieId);
        contentValues.put(FavouriteMovieEvent.COLUMN_FAV_MOVIE_TITLE, mMovie.getTitle());
        contentValues.put(FavouriteMovieEvent.COLUMN_FAV_MOVIE_IMAGE_PATH, mMovie.getImagePath());
        contentValues.put(FavouriteMovieEvent.COLUMN_IS_FAV, 1);

        asyncQueryHandler.startInsert(1, null, FavouriteMovieEvent.CONTENT_URI, contentValues);
    }

    @Override
    public void onMovieTrailerClick(String key) {
        // Play trailer
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_PARAM, key)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.no_supporting_app, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        int movieId = Integer.parseInt(mId);
        Uri currentItemUri = ContentUris.withAppendedId(FavouriteMovieEvent.CONTENT_URI, movieId);

        String projection[] = {
                FavouriteMovieEvent._ID,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_TITLE,
                FavouriteMovieEvent.COLUMN_IS_FAV
        };

        return new CursorLoader(this,
                currentItemUri,
                projection,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int isFavColumnIndex = cursor.getColumnIndex(FavouriteMovieEvent.COLUMN_IS_FAV);
            boolean isFav;
            isFav = cursor.getInt(isFavColumnIndex) != 0;

            btnFavourite.setPressed(isFav);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        btnFavourite.setPressed(false);
    }

}
