package com.example.priyankanandiraju.popularmovies.utilities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyankanandiraju on 3/27/17.
 */

public class MovieVideosList {
    @SerializedName("results")
    List<MovieVideo> movieVideoList = new ArrayList<>();

    public List<MovieVideo> getMovieVideoList() {
        return movieVideoList;
    }

    public void setMovieVideoList(List<MovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
    }
}
