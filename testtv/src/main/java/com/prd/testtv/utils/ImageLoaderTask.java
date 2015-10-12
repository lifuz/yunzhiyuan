package com.prd.testtv.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prd.testtv.bean.Car;

import java.lang.ref.WeakReference;

/**
 * 作者：李富 on 2015/8/10.
 * 邮箱：lifuzz@163.com
 */
public class ImageLoaderTask extends AsyncTask<Car, Void, Bitmap> {

    private Car car;
    //若引用，保存的对象可以被GC回收掉
    private WeakReference<ImageView> imageViewWeakReference;

    public ImageLoaderTask(ImageView imageView) {

        imageViewWeakReference = new WeakReference<ImageView>(imageView);

    }

    @Override
    protected Bitmap doInBackground(Car... params) {
        //回去TaskParam对象

        car = params[0];

        return loadImageFile(car.getPicFile(), car.getAssetManager());
    }

    private Bitmap loadImageFile(final String fileName, final AssetManager manager) {


        Bitmap bmp = BitmapCache.getInstance().getBitmap(fileName, car.getAssetManager());
        Log.i("tag", bmp.toString());

        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewWeakReference != null) {

            ImageView imageView = imageViewWeakReference.get();

            if (imageView != null) {

                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();


                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                imageView.setLayoutParams(layoutParams);
                imageView.setImageBitmap(bitmap);
            }

        }

    }
}
