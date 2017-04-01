package com.atguigu.beijingnews.newscenter.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.DensityUtil;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.detailpager.InteractMenuDetailPager;
import com.atguigu.beijingnews.detailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews.detailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews.detailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.newscenter.presenter.NewsCenterPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 万里洋 on 2017/2/5.
 */

public class NewsCenterPager extends BasePager implements INewsCenterView{
    /**
     * 存放新闻中心里面四个pager界面的集合
     */
    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    private Context mContext;
    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean.DataBean> dataBeanList;
    private NewsCenterPresenter presenter;
    private ProgressDialog progressDialog ;
    public NewsCenterPager(Context context) {
        super(context);
        this.mContext = context;
        presenter = new NewsCenterPresenter(NewsCenterPager.this);
        progressDialog = new ProgressDialog(mContext);
    }
    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","新闻中心加载数据了");
        //显示菜单按钮，因为只有新闻中心界面才有这按钮
        ib_menu.setVisibility(View.VISIBLE);
        //实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(DensityUtil.dip2px(mContext,20));
        textView.setGravity(Gravity.CENTER);
//        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);

        //和父类的FrameLayout结合
        fl_main.addView(textView);
        /**
         * 在初始化数据的时候从sp中获取缓存的数据
         */
//        String saveJson = CacheUtils.getString(mContext, Constants.NEWSCENTER_PAGER_URL);//""
//        if(!TextUtils.isEmpty(saveJson)){
//            processData(saveJson);
//        }
        //从本地获取数据
        presenter.fromLocalData();
        
        //进行联网的请求
        presenter.getDataFromNet();
    }
    private void processData(NewsCenterBean newsCenterBean) {
//        Gson gson = new Gson();
//        NewsCenterBean newsCenterBean = gson.fromJson(json, NewsCenterBean.class);
        dataBeanList = newsCenterBean.getData();
        Log.e("TAG", "新闻中心解析成功=" + dataBeanList.get(0).getChildren().get(0).getTitle());

        //把新闻中心的数据传递给左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;
        //得到左侧菜单
        LeftMenuFragment leftMunuFragment = mainActivity.getLeftMenuFragment();
        //把得到的数据给左侧菜单
        leftMunuFragment.setData(dataBeanList);

        //2.绑定数据

        menuDetailBasePagers = new ArrayList<>();
        //把数据传递给了新闻详情页面
        //在new各个详情页面的时候就调用各自的initView方法
        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity,dataBeanList.get(0)));//新闻详情页面
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity,dataBeanList.get(0)));//专题详情页面
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity,dataBeanList.get(2)));//组图详情页面
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity));//互动详情页面
        //进来的时候就调一下这个方法，让其默认的界面就是新闻详情中心的界面
        switchPager(0);

    }

    /**
     * 更加位置切换到不同的详情页面
     *
     * @param prePosition
     */
    public void switchPager(int prePosition) {
        if (prePosition < menuDetailBasePagers.size()) {
            //设置标题
            tv_title.setText(dataBeanList.get(prePosition).getTitle());
            /**
             * 得到四个详情页面中的某一个实例
             */
            MenuDetailBasePager menuDetailBasePager = menuDetailBasePagers.get(prePosition);
            //调用，其实就是某一个实例的initDate方法
            menuDetailBasePager.initData();
            //视图，得到某一个实例视图的视图
            View rootView = menuDetailBasePager.rootView;
            //放在之前的framelayout中，进行替换
            fl_main.removeAllViews();//移除之前的
            fl_main.addView(rootView);

            if(prePosition ==2){
                //组图
                ib_swtich_list_gird.setVisibility(View.VISIBLE);
                ib_swtich_list_gird.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager) menuDetailBasePagers.get(2);
                        photosMenuDetailPager.swichListGrid(ib_swtich_list_gird);
                    }
                });
            }else{
                //其他
                ib_swtich_list_gird.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(mContext, "该页面暂时未实现", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccess(NewsCenterBean newsCenterBean) {
        //解析从model层获取的数据
        processData(newsCenterBean);
    }

    @Override
    public void onFailed(Exception e) {
        Toast.makeText(mContext, "请求失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回网络请求地址
     * @return
     */
    @Override
    public String getUrl() {
        return Constants.NEWSCENTER_PAGER_URL;
    }
}
