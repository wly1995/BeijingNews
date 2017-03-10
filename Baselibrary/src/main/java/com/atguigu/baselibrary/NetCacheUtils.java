package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 万里洋 on 2017/2/10.
 */

public class NetCacheUtils {

    public static final int SECUSS = 1;
    public static final int FAIL = 2;
    private final LocalCacheUtils localCacheutils;
    private final MemoryCacheUtils memoryCacheUtils;
    private  Handler handler;
    private ExecutorService service;
    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheutils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        //是一个线程池，里面放的是每次网络请求需要的子线程
        service = Executors.newFixedThreadPool(10);
        this.localCacheutils = localCacheutils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmapFromNet(String url, int position) {

        //每进来一次创建一个线程请求一张图片
        service.execute(new MyRunnable(url,position));
    }
    class MyRunnable implements Runnable{

        private final String url;
        private final int position;

        public MyRunnable(String url, int position) {
            this.url = url;
            this.position = position;
        }

        /**
         * 在里面进行网络请求
         */
        @Override
        public void run() {
            try {
                //1.得到连接对象
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                //2.设置参数
                connection.setRequestMethod("GET");
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                int code = connection.getResponseCode();
                //3.检查是否连接成功
                if (code == 200) {
                    //请求图片成功,得到输入流
                    InputStream is = connection.getInputStream();
                    //得到图片对象
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    //保存一份到本地
                    memoryCacheUtils.putBitmap(url,bitmap);
                    //保存一份到内存
                    localCacheutils.putBitmap(url,bitmap);
                    //把图片显示到控件上
                    Message msg = Message.obtain();
                    //消息对象携带了图片对象、id和位置过去
                    msg.obj = bitmap;
                    msg.what = SECUSS;
                    msg.arg1 = position;
                    //发消息
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //请求图片失败
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        }
    }
}
