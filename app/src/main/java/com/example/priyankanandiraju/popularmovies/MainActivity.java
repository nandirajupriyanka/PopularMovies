package com.example.priyankanandiraju.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.priyankanandiraju.popularmovies.data.FavouriteMoviesContract.FavouriteMovieEvent;
import com.example.priyankanandiraju.popularmovies.helper.MoviesAPI;
import com.example.priyankanandiraju.popularmovies.helper.MoviesAPIService;
import com.example.priyankanandiraju.popularmovies.utilities.Movie;
import com.example.priyankanandiraju.popularmovies.utilities.MoviesList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MOVIE_ID = "MOVIE_ID";
    private static final int FAVOURITE_MOVIE_LOADER = 0;
    private RecyclerView rvMovies;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private MoviesAPIService mMoviesAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovies = (RecyclerView) findViewById(R.id.recycler_view_movies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        mMoviesAdapter = new MoviesAdapter(this, this);
        rvMovies.setAdapter(mMoviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        createMoviesAPI();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        fetchPopularMovies();
    }

    private void createMoviesAPI() {
        Retrofit retrofit = MoviesAPI.getClient();
        mMoviesAPIService = retrofit.create(MoviesAPIService.class);
    }

    @Override
    public void onMovieClick(int id) {
        String movieId = String.valueOf(id);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                mLoadingIndicator.setVisibility(View.VISIBLE);
                fetchPopularMovies();
                return true;
            case R.id.rating:
                mLoadingIndicator.setVisibility(View.VISIBLE);
                fetchTopRatedMovies();
                return true;
            case R.id.favourites:
                getLoaderManager().initLoader(FAVOURITE_MOVIE_LOADER, null, MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void fetchPopularMovies() {
        Call<MoviesList> popularMoviesListCall = mMoviesAPIService.getPopularMovies(BuildConfig.THE_MOVIE_DB_APP_KEY);
        popularMoviesListCall.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mMoviesAdapter.setMoviesData(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.unable_to_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTopRatedMovies() {
        Call<MoviesList> topRatedMoviesListCall = mMoviesAPIService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_APP_KEY);
        topRatedMoviesListCall.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mMoviesAdapter.setMoviesData(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.unable_to_fetch_data), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        String projection[] = {
                FavouriteMovieEvent._ID,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_TITLE,
                FavouriteMovieEvent.COLUMN_FAV_MOVIE_IMAGE_PATH,
                FavouriteMovieEvent.COLUMN_IS_FAV,
        };
        return new CursorLoader(this,
                FavouriteMovieEvent.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.no_fav_movies, Toast.LENGTH_SHORT).show();
            return;
        }
        List<Movie> moviesList = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            int favMovieIdColumnIndex = cursor.getColumnIndex(FavouriteMovieEvent.COLUMN_FAV_MOVIE_ID);
            int titleColumnIndex = cursor.getColumnIndex(FavouriteMovieEvent.COLUMN_FAV_MOVIE_TITLE);
            int imagePathColumnIndex = cursor.getColumnIndex(FavouriteMovieEvent.COLUMN_FAV_MOVIE_IMAGE_PATH);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            int movieId = cursor.getInt(favMovieIdColumnIndex);
            String imagePath = cursor.getString(imagePathColumnIndex);

            Movie movies = new Movie();
            movies.setId(movieId);
            movies.setTitle(title);
            movies.setImagePath(imagePath);
            movies.setFav(true);
            moviesList.add(movies);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMoviesAdapter.setMoviesData(moviesList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
