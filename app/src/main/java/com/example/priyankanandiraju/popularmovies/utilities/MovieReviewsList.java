package com.example.priyankanandiraju.popularmovies.utilities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyankanandiraju on 3/27/17.
 */

public class MovieReviewsList {
    @SerializedName("results")
    private List<MovieReview> movieReviewList = new ArrayList<>();

    public List<MovieReview> getMovieReviewList() {
        return movieReviewList;
    }

    public void setMovieReviewList(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }
}
