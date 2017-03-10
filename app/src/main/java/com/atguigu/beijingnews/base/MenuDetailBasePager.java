package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by 万里洋 on 2017/2/7.
 */

public abstract class MenuDetailBasePager {
    /**
     * 上下文
     */
    public final Context mContext;
    /**
     * 代表各个菜单详情页面的实例，视图
     */
    public View rootView;
    public MenuDetailBasePager(Context context){
        this.mContext = context;
        rootView = initView();

    }

    /**
     * 由子类实现该方法，初始化子类的视图
     * @return
     */
    public abstract View initView() ;

    /**
     绑定数据或者请求数据再绑定数据
     */
    public void  initData(){

    }
}
