package com.prd.testtv.request;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * 作者：李富 on 2015/9/23.
 * 邮箱：lifuzz@163.com
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> lruCache;

    public BitmapCache() {
        int maxSize = 10 * 1024 * 1024;

        lruCache = new LruCache<String, Bitmap>(maxSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {


        return lruCache.get(url);
    }


    @Override
    public void putBitmap(String url, Bitmap bitmap) {

        lruCache.put(url, bitmap);

    }
}
