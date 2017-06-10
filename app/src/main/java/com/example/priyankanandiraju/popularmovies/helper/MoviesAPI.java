package com.example.priyankanandiraju.popularmovies.helper;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.priyankanandiraju.popularmovies.helper.Constants.BASE_URL;

/**
 * Created by priyankanandiraju on 3/27/17.
 */

public class MoviesAPI {

    private static Retrofit mRetrofit = null;

    public static Retrofit getClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return mRetrofit;
    }
}
