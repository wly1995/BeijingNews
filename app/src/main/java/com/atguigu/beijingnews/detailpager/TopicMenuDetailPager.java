package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 万里洋 on 2017/2/7.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager{
    /**
     * 新闻详情页面的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> childrenData;
    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.ib_next)
    ImageButton ibNext;
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;


    public TopicMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.childrenData = dataBean.getChildren();//12条
    }

    @Override
    public View initView() {
        //专题详情页面的视图
        View view = View.inflate(mContext, R.layout.topic_menu_detail_pager, null);

        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备数据-页面
        tabDetailPagers = new ArrayList<>();
        //根据有多少数据创建多少个TabDetailPager，并且把childrenData数据传入到对象中
        for (int i = 0; i < childrenData.size(); i++) {
            //TabDetailPager这个类就相当于是一个一个的页面，把这些页面全部添加到集合中
            tabDetailPagers.add(new TabDetailPager(mContext, childrenData.get(i)));
        }

        //设置适配器
        viewpager.setAdapter(new TopicMenuDetailPager.MyPagerAdapter());

        //要在设置适配器之后，如果要设置标题那么必须在设置适配器的时候就重写CharSequence getPageTitle
        // 方法才可以显示出来，这个方法也就是把indicator和ViewPager结合在一起
        tabLayout.setupWithViewPager(viewpager);
        //设置滑动模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //监听页面的变化用TabPageIndicator，之所以可以用它来做监听是因为他已经和ViewPager关联了
        viewpager.addOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            MainActivity mainActivity = (MainActivity) mContext;
            if(position==0){
                //北京-可以滑动左侧的菜单
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //其他不能滑动
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @OnClick(R.id.ib_next)
    public void onClick() {
        if (viewpager.getCurrentItem()+1 < tabDetailPagers.size()){
            //切换到下一个页面
            viewpager.setCurrentItem(viewpager.getCurrentItem()+1);
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        /**
         * 这个方法不重写的话ViewPagerIndicator的标题显示不出来
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return childrenData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        //实例化新闻中心详情页面的每一个页面（12个）
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            tabDetailPager.initData();//不要忘记
            View rootView = tabDetailPager.rootView;
            //添加到ViewPager中
            container.addView(rootView);
            return rootView;
        }
    }
}
