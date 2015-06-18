package com.perback.perback.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.perback.perback.R;
import com.perback.perback.apis.places.PlacesApiWrapper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PicassoUtils {

    private static Picasso picasso;
    public static final String PLACE_PHOTOS_URL = "https://maps.googleapis.com/maps/api/place/photo?key=" + PlacesApiWrapper.API_KEY + "&photoreference=";
    private static final String PLACES_PLACEHOLDER = "http://cdn1.tekrevue.com/wp-content/uploads/2014/06/map-pin-location.jpg";

    public static void init(Context context) {
        picasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("Picasso", "Image load failed for: " + uri.toString());
                        Log.e("Picasso", "Exception: " + exception.getMessage());
                    }
                })
                .loggingEnabled(false)
                .build();
    }

    public static void load(String url, ImageView target) {
        picasso.load(url).fit().into(target);
    }

    public static void load(Uri uri, ImageView target) {
        picasso.load(uri).fit().into(target);
    }

    public static void load(File file, ImageView target) {
        picasso.load(file).fit().into(target);
    }

    public static void load(int resId, ImageView target) {
        picasso.load(resId).fit().into(target);
    }

    public static void loadPlacePhoto(String photoReference, final ImageView target) {
        if (photoReference != null)
            picasso.load(PLACE_PHOTOS_URL + photoReference).fit().centerInside().into(target, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    picasso.load(PLACES_PLACEHOLDER).fit().centerInside().into(target);
                }
            });
        else {
            picasso.load(PLACES_PLACEHOLDER).fit().centerInside().into(target);
        }
    }

}
