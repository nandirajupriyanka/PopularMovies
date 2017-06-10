package com.example.priyankanandiraju.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.priyankanandiraju.popularmovies.utilities.MovieVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyankanandiraju on 3/15/17.
 */

class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoHolder> {

    private List<MovieVideo> mMovieVideoList;
    private OnMovieTrailerClickListener mClickHandler;

    public interface OnMovieTrailerClickListener {
        void onMovieTrailerClick(String key);
    }

    public MovieVideoAdapter(Context context, List<MovieVideo> list, OnMovieTrailerClickListener listener) {
        mClickHandler = listener;
        mMovieVideoList = new ArrayList<>(list);
    }

    @Override
    public MovieVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_trailer, parent, false);
        return new MovieVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieVideoHolder holder, int position) {
        holder.setHolderData(mMovieVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieVideoList.size();
    }

    public void setVideoData(List<MovieVideo> movieVideoList) {
        mMovieVideoList.clear();
        mMovieVideoList.addAll(movieVideoList);
        notifyDataSetChanged();
    }

    public class MovieVideoHolder extends RecyclerView.ViewHolder {
        private TextView tvTrailerName;
        private ImageButton playBtn;

        public MovieVideoHolder(View itemView) {
            super(itemView);
            tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            playBtn = (ImageButton) itemView.findViewById(R.id.ib_play_trailer);
        }

        public void setHolderData(final MovieVideo data) {
            tvTrailerName.setText(data.getName());
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onMovieTrailerClick(data.getKey());
                }
            });
        }
    }
}
