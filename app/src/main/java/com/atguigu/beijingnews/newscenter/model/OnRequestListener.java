package com.atguigu.beijingnews.newscenter.model;

import com.atguigu.beijingnews.bean.NewsCenterBean;

/**
 * Created by 万里洋 on 2017/4/1.
 * 请求是否成功的监听
 */

public interface OnRequestListener {
    /**
     * 当请求成功的时候回调
     * @param newsCenterBean
     */
    void onSuccess(NewsCenterBean newsCenterBean);

    /**
     * 当失败的时候回调
     * @param ex
     */
    void onFailed(Exception ex);
}
