package com.atguigu.beijingnews.newscenter.presenter;

import android.widget.Toast;

import com.atguigu.beijingnews.MyApplication;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.atguigu.beijingnews.newscenter.model.INewsCenterModel;
import com.atguigu.beijingnews.newscenter.model.NewsCenterModel;
import com.atguigu.beijingnews.newscenter.model.OnRequestListener;
import com.atguigu.beijingnews.newscenter.view.INewsCenterView;
import com.atguigu.beijingnews.newscenter.view.NewsCenterPager;

/**
 * Created by 万里洋 on 2017/4/1.
 * p层
 */

public class NewsCenterPresenter {
    //M
    private INewsCenterModel iNewsCenterModel;
    //V
    private INewsCenterView iNewsCenterView;

    public NewsCenterPresenter(NewsCenterPager newsCenterPager) {
        this.iNewsCenterView = newsCenterPager;
        iNewsCenterModel = new NewsCenterModel();
    }

    public void getDataFromNet() {
        iNewsCenterView.showLoading();
        iNewsCenterModel.getDataFromNet(iNewsCenterView.getUrl(), new OnRequestListener() {
            @Override
            public void onSuccess(final NewsCenterBean newsCenterBean) {
                iNewsCenterView.hideLoading();
                //成功的时候回传数据给view
                iNewsCenterView.onSuccess(newsCenterBean);
            }

            @Override
            public void onFailed(Exception ex) {
                iNewsCenterView.hideLoading();
                iNewsCenterView.onFailed(ex);
            }
        });
    }
    //从本地获取数据
    public void fromLocalData() {
        iNewsCenterModel.fromLocalData(iNewsCenterView.getUrl(), new OnRequestListener() {
            @Override
            public void onSuccess(NewsCenterBean newsCenterBean) {
                Toast.makeText(MyApplication.getInstance(), "是从本地获取的", Toast.LENGTH_SHORT).show();
                iNewsCenterView.onSuccess(newsCenterBean);
            }
            @Override
            public void onFailed(Exception ex) {
                iNewsCenterView.onFailed(ex);
            }
        });
    }
}
