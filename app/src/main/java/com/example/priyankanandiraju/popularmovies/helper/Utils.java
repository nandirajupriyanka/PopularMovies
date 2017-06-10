package com.example.priyankanandiraju.popularmovies.helper;

/**
 * Created by priyankanandiraju on 3/28/17.
 */

public class Utils {
    public static String generateImageUrl(String imageUri) {
        String imagePath = Constants.BASE_IMAGE_URL + imageUri;
        return imagePath;
    }
}
