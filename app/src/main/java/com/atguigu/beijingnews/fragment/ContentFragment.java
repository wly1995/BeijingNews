package com.atguigu.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.newscenter.view.NewsCenterPager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.view.NoScrollViewPager;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 万里洋 on 2017/2/5.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    /**
     * 存放三个pager的集合
     */
    private ArrayList<BasePager> basePagers;
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        //因为initview本身就在oncreateview的方法中
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //初始化界面
        initPager();

        //设置ViewPager的适配器
        setAdapter();
        //设置监听
        initListener();
    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //先默认设置不可以滑动
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                switch (checkedId){
                    case R.id.rb_home:
                        //false就是屏蔽了滑动效果，直接进去跳转
                        viewpager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        viewpager.setCurrentItem(1,false);
                        //如果选中了新闻中心界面即可进行滑动
                        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    case R.id.rb_setting:
                        viewpager.setCurrentItem(2,false);
                        break;
                }
            }
        });
        rgMain.check(R.id.rb_home);
        //设置viewpager页面变化时的监听
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        //默认加载第一个界面
        basePagers.get(0).initData();
    }

    /**
     * 得到新闻中心
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当某个界面被选中的时候才调用initData，这样才能保证点那个界面的时候就加载那个界面
            basePagers.get(position).initData();//孩子的视图和父类的FrameLayout结合,并进行联网请求等一系列的数据加载
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置ViewPager的适配器
     */
    private void setAdapter() {
        viewpager.setAdapter(new MyPagerAdapter());
    }
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);


            View rootView = basePager.rootView;//这个rootView就是代表其中一个界面的实例
            //把实例添加到ViewPager中
            container.addView(rootView);

            //调用initData方法，不然子类pager的视图显示不出来（不知道为什么不去调用initDate方法？）
            //basePager.initData(); 这个时候不去调用这个方法，是因为在viewpager的适配器中，会进行预加载
            //但我们是需要在点击那个界面才加载那个界面的，所以要在选中某个界面的时候才加载这个界面

            return rootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }

    private void initPager() {
        //初始化三个界面
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(mContext));
        basePagers.add(new NewsCenterPager(mContext));
        basePagers.add(new SettingPager(mContext));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
