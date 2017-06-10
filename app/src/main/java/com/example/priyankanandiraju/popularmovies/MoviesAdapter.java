package com.example.priyankanandiraju.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.priyankanandiraju.popularmovies.helper.Utils;
import com.example.priyankanandiraju.popularmovies.utilities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.priyankanandiraju.popularmovies.helper.Constants.BASE_IMAGE_URL;

/**
 * Created by priyankanandiraju on 3/8/17.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private OnMovieClickListener mClickHandler;
    private List<Movie> mMoviesData;
    private Context mContext;


    public interface OnMovieClickListener {
        void onMovieClick(int movieId);
    }


    public MoviesAdapter(Context context, OnMovieClickListener movieClickListener) {
        mClickHandler = movieClickListener;
        mContext = context;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_layout, parent, false);
        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {

        Movie currentMovie = mMoviesData.get(position);
        Picasso.with(mContext)
            .load(Utils.generateImageUrl(currentMovie.getImagePath()))
            .resize(300, 600)
            .placeholder(R.drawable.placeholder)
            .into(holder.ivPoster);

    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.size();
    }

    public void setMoviesData(List<Movie> movies) {

        mMoviesData = movies;
        notifyDataSetChanged();
    }

    public class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = MoviesHolder.class.getSimpleName();
        public final ImageView ivPoster;

        public MoviesHolder(View view) {
            super(view);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesData.get(adapterPosition);
            Log.v(TAG, "onClick() " + adapterPosition + " : " + movie.getTitle());
            mClickHandler.onMovieClick(movie.getId());
        }
    }
}
