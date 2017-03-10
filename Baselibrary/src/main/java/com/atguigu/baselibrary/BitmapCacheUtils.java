package com.atguigu.baselibrary;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * Created by 万里洋 on 2017/2/10.
 */

public class BitmapCacheUtils {
    //网络缓存工具类
    private NetCacheUtils netCacheUtils;
    //本地缓存工具类
    private LocalCacheUtils localCacheutils;
    //内存缓存工具类
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler){
        //用于在网络上做缓存的工具类,把这个handler传过去，为了发消息，这个handler来自主线程
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheutils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler,localCacheutils,memoryCacheUtils);
    }
    /**
     * 三级缓存设计步骤：
     * 从内存中取图片
     * 从本地文件中取图片
     * 向内存中保持一份
     * 请求网络图片，获取图片，显示到控件上
     * 向内存存一份
     * 向本地文件中存一份
     */


    public Bitmap getBitmapFromNet(String url, int position){
        //1.先从内存中获取
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("TAG", "内存缓存图片成功==" + position);
                return bitmap;
            }
        }
        //2.在从本地中获取
        if (localCacheutils != null) {
            Bitmap bitmap = localCacheutils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("TAG", "本地缓存图片成功==" + position);
                return bitmap;
            }
        }
        //3.在从网络上获取

        netCacheUtils.getBitmapFromNet(url,position);//没有直接返回bitmap的对象，而是直接在子线程用handler往主线程发消息了

        return  null;
    }

}
