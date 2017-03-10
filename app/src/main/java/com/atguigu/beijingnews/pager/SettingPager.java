package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.baselibrary.DensityUtil;
import com.atguigu.beijingnews.base.BasePager;

/**
 * Created by 万里洋 on 2017/2/5.
 */

public class SettingPager extends BasePager {
    private Context mContext;
    public SettingPager(Context context) {
        super(context);
        this.mContext = context;
    }
    @Override
    public void initData() {
        super.initData();

        //设置标题
        tv_title.setText("设置中心");
        Log.e("TAG","设置中心加载数据了");

        //实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(DensityUtil.dip2px(mContext,20));
        textView.setGravity(Gravity.CENTER);
        textView.setText("设置中心");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);
    }
}
