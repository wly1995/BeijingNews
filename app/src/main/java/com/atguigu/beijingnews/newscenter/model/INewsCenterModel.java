package com.atguigu.beijingnews.newscenter.model;

/**
 * Created by 万里洋 on 2017/4/1.
 */

public interface INewsCenterModel {
    //进行网络请求的方法
   void getDataFromNet(String url,OnRequestListener listener);
   void fromLocalData(String url,OnRequestListener listener);
}
