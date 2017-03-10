package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String CONENT_TAG = "conent_tag";
    private int screenHeight;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置主页面
        setContentView(R.layout.activity_main);
        //得到屏幕宽和高
        DisplayMetrics out = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(out);
        screenHeight = out.heightPixels;
        screenWidth = out.widthPixels;

        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.leftmenu);


        //4.设置模式：左侧+主页；左侧+主页+右侧；主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动的模式：全屏，边缘，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页面占的宽度dip
//        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
        slidingMenu.setBehindOffset((int) (screenWidth*0.65));

        //初始化fragment
        initFragment();

    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        //开启事物
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //进行fragment的替换
        ft.replace(R.id.fl_leftmenu,new LeftMenuFragment(),LEFTMENU_TAG);
        ft.replace(R.id.fl_content,new ContentFragment(),CONENT_TAG);
        //提交事务
        ft.commit();
    }

    /**
     * 得到左侧菜单，用事务对象根据tag去找对应的fragment 目的是找同一个fragment实例
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        //找同一个实例
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }
    /**
     * 得到ContentFragment，用事务对象根据tag去找对应的fragment
     * @return
     */
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONENT_TAG);
    }
}
