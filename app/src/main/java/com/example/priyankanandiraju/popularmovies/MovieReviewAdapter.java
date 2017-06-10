package com.example.priyankanandiraju.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyankanandiraju.popularmovies.utilities.MovieReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyankanandiraju on 3/15/17.
 */

class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder> {

    private List<MovieReview> mMovieReviewList;

    public MovieReviewAdapter(Context context, List<MovieReview> list) {
        mMovieReviewList = new ArrayList<>(list);
    }

    @Override
    public MovieReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, parent, false);
        return new MovieReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewHolder holder, int position) {
        holder.setHolderData(mMovieReviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieReviewList.size();
    }

    public void setReviewData(List<MovieReview> movieReviewList) {
        mMovieReviewList.clear();
        mMovieReviewList.addAll(movieReviewList);
        notifyDataSetChanged();
    }

    public class MovieReviewHolder extends RecyclerView.ViewHolder {

        private TextView tvAuthor;
        private TextView tvContent;

        public MovieReviewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_rating_content);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_rating_author);
        }

        public void setHolderData(MovieReview data) {
            String authorName = "- " + data.getAuthor();
            tvAuthor.setText(authorName);
            tvContent.setText(data.getContent());
        }
    }
}
