package com.example.priyankanandiraju.popularmovies.utilities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by priyankanandiraju on 3/15/17.
 */

public class MovieReview {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
