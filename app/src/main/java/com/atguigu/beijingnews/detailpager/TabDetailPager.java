package com.atguigu.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.DensityUtil;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.adapter.TabDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by 万里洋 on 2017/2/7.
 */

public class TabDetailPager extends MenuDetailBasePager {
    public static final String ID_ARRAY = "id_array";
    /**
     * 接收来自新闻中心详情页面传过来的一个页面的数据
     */
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    public TabDetailPagerAdapter adapter;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pulltorefreshlistview;
    ListView listview;


    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llGroupPoint;

    private String url;
    /**
     * 列表数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsEntity> news;
    private List<TabDetailPagerBean.DataEntity.TopnewsEntity> topNews;

    private int prePosition;
    /**
     * 获取更多数据的路径
     */
    private String moreUrl;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图

        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        ButterKnife.inject(this, view);
        //listview由这个第三方的控件得到
        listview = pulltorefreshlistview.getRefreshableView();
        //给listview添加头部的视图
        View headerView = View.inflate(mContext, R.layout.header_view, null);
        viewpager = (HorizontalScrollViewPager) headerView.findViewById(R.id.viewpager);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        llGroupPoint = (LinearLayout) headerView.findViewById(R.id.ll_group_point);
        listview.addHeaderView(headerView);


        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pulltorefreshlistview.setOnPullEventListener(soundListener);

        //给listview设置点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //得到bean对象
                TabDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position-2);
                int ids = news.get(position-2).getId();
                String title = news.get(position-2).getTitle();
                Log.e("Tag",title);
                //从缓存中取得
                String idArray = CacheUtils.getString(mContext, ID_ARRAY);//""-->1111,
                if (!idArray.contains(ids+"")){
                    //如果不包含保存点击过的item的对应的id
                    CacheUtils.putString(mContext,ID_ARRAY,idArray+ids+",");//""-->1111,
                    //刷新适配器会导致getView方法执行
                    adapter.notifyDataSetChanged();//getCount-->getView
                }

                //跳转到新闻的浏览页面
                Intent intent = new Intent(mContext,NewsDetailActivity.class);
                intent.putExtra("url",Constants.BASE_URL+newsEntity.getUrl());
                mContext.startActivity(intent);
            }
        });

        //设置监听
        pulltorefreshlistview.setOnRefreshListener(new MyOnRefreshListener2());
        return view;
    }

    /**
     * 上拉时加载更多的网络请求的方法
     * @return
     */
    public void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                processData(result);
                //请求完成的时候，下拉刷新结束 上拉也会隐藏
                pulltorefreshlistview.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求数据失败==TabDetailPager==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2<ListView> {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//            Toast.makeText(mContext, "下拉刷新", Toast.LENGTH_SHORT).show();
            isLoadMore = false;
            getDataFromNet();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//            Toast.makeText(mContext, "上拉刷新", Toast.LENGTH_SHORT).show();
            if (!TextUtils.isEmpty(moreUrl)) {
                isLoadMore = true;
                getMoreDataFromNet();
            } else{
                Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
                pulltorefreshlistview.onRefreshComplete();
            }

        }
    }

    @Override
    public void initData() {
        super.initData();
        //这个url就是各个详情页面的url
        url = Constants.BASE_URL + childrenBean.getUrl();
        //联网请求之前先从缓存中读取
        String saveJson = CacheUtils.getString(mContext,url);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
//        textView.setText(childrenBean.getTitle());
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求数据成功==TabDetailPager==" + childrenBean.getTitle());
                //请求成功的时候进行缓存
                CacheUtils.putString(mContext,url,result);
                processData(result);

                //请求完成的时候，下拉刷新结束 上拉也会隐藏
                pulltorefreshlistview.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求数据失败==TabDetailPager==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
    private InternalHandler handler;
    private void processData(String json) {
        final TabDetailPagerBean pagerBean = new Gson().fromJson(json, TabDetailPagerBean.class);
        String more = pagerBean.getData().getMore();
        if (TextUtils.isEmpty(more)) {
            moreUrl = "";
        } else{
            moreUrl = Constants.BASE_URL + more;
        }
        if (!isLoadMore){
            //得到北京页面的列表数据
            news = pagerBean.getData().getNews();
            //设置适配器
            adapter = new TabDetailPagerAdapter(mContext, news);
            listview.setAdapter(adapter);


            //设置顶部新闻

            topNews = pagerBean.getData().getTopnews();
            //设置顶部viewpager的适配器
            viewpager.setAdapter(new MyPagerAdapter());
            //监听页面的变化，选中某个页面时，进行数据的填充
            tvTitle.setText(topNews.get(prePosition).getTitle());
            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                tvTitle.setText(topNews.get(position).getTitle());
                    //先把之前的变灰
                    llGroupPoint.getChildAt(prePosition).setEnabled(false);
                    //把当前变高亮
                    llGroupPoint.getChildAt(position).setEnabled(true);
                    prePosition = position;
                }

                @Override
                public void onPageSelected(int position) {
                    tvTitle.setText(topNews.get(position).getTitle());
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state==ViewPager.SCROLL_STATE_DRAGGING){
                        handler.removeCallbacksAndMessages(null);
                    }else if(state==ViewPager.SCROLL_STATE_IDLE){
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(new MyRunnable(),3000);
                    }
                }
            });


            //把之前所有的移除，要么会有缓存
            llGroupPoint.removeAllViews();

            //添加红点
            for (int i =0;i<topNews.size();i++) {
                //添加到线性布局
                ImageView point = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, ViewGroup.LayoutParams.WRAP_CONTENT);
                if(i!= 0){
                    //设置距离左边的距离
                    params.leftMargin = DensityUtil.dip2px(mContext,8);
                    point.setEnabled(false);
                }else{
                    point.setEnabled(true);
                }
                point.setLayoutParams(params);
                //设置图片背景选择器
                point.setBackgroundResource(R.drawable.point_selector);


                llGroupPoint.addView(point);
            }
        } else {//这时更多数据的url不为空，说明有更多列表数据存在
            List<TabDetailPagerBean.DataEntity.NewsEntity> moreNews = pagerBean.getData().getNews();
            //然后把这个列表添加到原来的列表，这就是上拉加载更多的数据
            news.addAll(moreNews);
            //然后刷新适配器，得意更新界面
            adapter.notifyDataSetChanged();
        }
        //顶部轮播图的切换
        if(handler ==null){
            handler = new InternalHandler();
        }
        //把之前所有消息和任务移除
        handler.removeCallbacksAndMessages(null);


        handler.postDelayed(new MyRunnable(),3000);
    }
    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //切换到下个页面
            int item = (viewpager.getCurrentItem()+1)%topNews.size();
            viewpager.setCurrentItem(item);
            //递归发送消息  形成循环
            handler.postDelayed(new MyRunnable(),3000);
        }
    }
    class MyRunnable implements Runnable{

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置默认的和联网请求
            final TabDetailPagerBean.DataEntity.TopnewsEntity topnewsEntity = topNews.get(position);
            //加载图片
            Glide.with(mContext).load(Constants.BASE_URL + topnewsEntity.getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //设置默认图片
                    .placeholder(R.drawable.news_pic_default)
                    //请求图片失败
                    .error(R.drawable.news_pic_default)
                    .into(imageView);
            //添加到ViewPager和返回
            container.addView(imageView);

            //给图片设置触摸事件
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.postDelayed(new MyRunnable(),3000);
                            break;
                    }
                    return false;
                }
            });
            //点击图片跳转到webview
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,NewsDetailActivity.class);
                    intent.putExtra("url",Constants.BASE_URL+topnewsEntity.getUrl());
                    mContext.startActivity(intent);
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
