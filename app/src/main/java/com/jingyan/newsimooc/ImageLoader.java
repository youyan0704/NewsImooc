package com.jingyan.newsimooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Administrator on 2016/4/26.
 */
public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;
    //创建Cache
    private LruCache<String,Bitmap> mCaches;

    public ImageLoader() {
        //获取最大可用内存
        int maxMemorry = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemorry / 4;
        mCaches = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    //添加到缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        if (getBitmapfromCache(url) == null){
            mCaches.put(url,bitmap);
        }
    }

    //从缓存获取数据
    public Bitmap getBitmapfromCache(String url){
       return mCaches.get(url);
    }
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };
    public void showImageByThread(ImageView imageView, final String url){
        mImageView = imageView;
        mUrl = url;
        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap =getBitmapFromURL(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    public Bitmap getBitmapFromURL(String url){
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            URL ur1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) ur1.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         return bitmap;
    }

}
