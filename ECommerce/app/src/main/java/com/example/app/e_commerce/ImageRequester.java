package com.example.app.e_commerce;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


public class ImageRequester {
    private static volatile ImageRequester instance;
    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private final int maxByteSize;

    private ImageRequester(Context context) {
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        this.requestQueue.start();
        this.maxByteSize = calculateMaxByteSize(context);
        this.imageLoader =
                new ImageLoader(
                        requestQueue,
                        new ImageLoader.ImageCache() {
                            private final LruCache<String, Bitmap> lruCache =
                                    new LruCache<String, Bitmap>(maxByteSize) {
                                        @Override
                                        protected int sizeOf(String url, Bitmap bitmap) {
                                            return bitmap.getByteCount();
                                        }
                                    };

                            @Override
                            public synchronized Bitmap getBitmap(String url) {
                                return lruCache.get(url);
                            }

                            @Override
                            public synchronized void putBitmap(String url, Bitmap bitmap) {
                                lruCache.put(url, bitmap);
                            }
                        });
    }

    private static int calculateMaxByteSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final int screenBytes = displayMetrics.widthPixels * displayMetrics.heightPixels * 4;
        return screenBytes * 3;
    }

    public static ImageRequester getInstance(Context context) {
        ImageRequester result = instance;
        if (result == null) {
            synchronized (ImageRequester.class) {
                result = instance;
                if (result == null) {
                    result = instance = new ImageRequester(context);
                }
            }
        }

        return result;
    }

    public void setImageFromUrl(NetworkImageView networkImageView, String url) {
        networkImageView.setImageUrl(url, imageLoader);
    }
}
