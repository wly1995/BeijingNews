package com.atguigu.beijingnews.newscenter.view;

import com.atguigu.beijingnews.bean.NewsCenterBean;

/**
 * Created by 万里洋 on 2017/4/1.
 * view层中要实现的方法
 */

public interface INewsCenterView {
    void showLoading();
    void hideLoading();
    //联网成功时的回调，形参为请求得到的数据
    void onSuccess(NewsCenterBean newsCenterBean);
    void onFailed(Exception e);
    String getUrl();
}
