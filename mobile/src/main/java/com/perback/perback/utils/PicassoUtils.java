package com.perback.perback.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PicassoUtils {

    private static Picasso picasso;

    public static void init(Context context) {
        picasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("Picasso", "Image load failed for: "+uri.toString());
                        Log.e("Picasso", "Exception: "+exception.getMessage());
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

}
