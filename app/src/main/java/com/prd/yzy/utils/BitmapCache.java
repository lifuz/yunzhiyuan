package com.prd.yzy.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

/**
 * 图片加载与缓存
 *
 * 作者：李富 on 2015/8/10.
 * 邮箱：lifuzz@163.com
 */
public class BitmapCache  {

    static private BitmapCache cache;
    //用于Cache内容存储
    private Hashtable<String,BitmapRef> bitmapRefs;

    //垃圾Reference的队列（所引用的对象已经被收回，则将该引用存入队列中）
    private  ReferenceQueue<Bitmap> q;

    private class BitmapRef extends SoftReference<Bitmap> {
        private String _key = "";

        public BitmapRef(Bitmap bitmap, ReferenceQueue<Bitmap> q, String _key) {
            super(bitmap, q);
            this._key = _key;
        }
    }

    private BitmapCache() {

        bitmapRefs = new Hashtable<>();
        q = new ReferenceQueue<>();
    }

    /**
     * 取得缓存器实例
     */
    public static BitmapCache getInstance() {
        if(cache == null) {
            cache = new BitmapCache();
        }

        return cache;
    }

    /**
     * 依据指定文件名获取图片
     * @param fileName
     * @param assetManager
     * @return
     */
    public Bitmap getBitmap(String fileName,AssetManager assetManager) {

        Bitmap bitmap = null;
        //缓存中是否有该Bitmap实例的软引用，，如果有，则从软引用中取得
        if(bitmapRefs.contains(fileName)) {
            BitmapRef ref = (BitmapRef) bitmapRefs.get(fileName);
            bitmap = (Bitmap) ref.get();
        }

        //如果没有，或者从软引用中得到是空值，重新构建一个实例
        //并保存对这个新建实例的软引用
        if (bitmap == null) {
            //初始化Bitmap的配置对象
            BitmapFactory.Options options = new BitmapFactory.Options();
            //配置Bitmap的参数
            options.inTempStorage = new byte[16 * 1024];
            BufferedInputStream buf;

            try {
                //获取图片
                buf = new BufferedInputStream(assetManager.open(fileName));
                bitmap = BitmapFactory.decodeStream(buf);
                addCacheBitmap(bitmap,fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return bitmap;

    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     * @param bitmap
     * @param fileName
     */
    private void addCacheBitmap(Bitmap bitmap,String fileName) {

        cleanCache();//清理垃圾缓存

        BitmapRef ref = new BitmapRef(bitmap,q,fileName);
        bitmapRefs.put(fileName,ref);

    }

    /**
     * 清除垃圾缓存
     *
     */
    private void cleanCache() {
        BitmapRef ref = null;
        while ((ref = (BitmapRef) q.poll()) != null) {
            bitmapRefs.remove(ref._key);
        }
    }

    /**
     * 清楚Cache内所有内容
     */
    public void clearCache() {
        cleanCache();;
        bitmapRefs.clear();
        System.gc();
        System.runFinalization();
    }


}
