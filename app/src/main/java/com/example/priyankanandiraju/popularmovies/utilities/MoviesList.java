package com.example.priyankanandiraju.popularmovies.utilities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
/**
 * Created by priyankanandiraju on 3/27/17.
 */

public class MoviesList {

    @SerializedName("results")
    private List<Movie> movieList = new ArrayList<>();

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
