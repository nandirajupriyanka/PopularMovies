package com.example.priyankanandiraju.popularmovies.utilities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by priyankanandiraju on 3/15/17.
 */

public class MovieVideo {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;
    @SerializedName("site")
    private String site;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
