package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 万里洋 on 2017/2/6.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 屏蔽它的左右滑动
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;//代表消费了触摸事件，就不能再滑动了
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//表示不拦截，因为它里面还有一个ViewPager（是存放那12个页面的ViewPager），拦截的话会造成事件的冲突
    }
}
