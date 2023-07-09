package com.harika.dinorun.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

public class ResourceManager {

    private static ResourceManager instance;
    private Context context;
    private SparseArray<Bitmap> bitmapCache;

    private ResourceManager(Context context) {
        this.context = context.getApplicationContext();
        this.bitmapCache = new SparseArray<>();
    }

    public static synchronized ResourceManager getInstance(Context context) {
        if (instance == null) {
            instance = new ResourceManager(context);
        }
        return instance;
    }

    public Drawable getDrawableFromResource(int resourceId) {
        return context.getResources().getDrawable(resourceId);
    }
}


