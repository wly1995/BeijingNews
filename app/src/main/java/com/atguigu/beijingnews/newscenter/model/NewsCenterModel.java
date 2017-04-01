package com.atguigu.beijingnews.newscenter.model;

import android.text.TextUtils;
import android.util.Log;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews.MyApplication;
import com.atguigu.beijingnews.bean.NewsCenterBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 万里洋 on 2017/4/1.
 */

public class NewsCenterModel implements INewsCenterModel {
    //真正在此进行网络请求
    @Override
    public void getDataFromNet(final String url, final OnRequestListener listener) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //在网络请求成功的时候就缓存数据
                CacheUtils.putString(MyApplication.getInstance(), Constants.NEWSCENTER_PAGER_URL,result);
                Log.e("TAG", "请求成功=="+result);
                //数据请求成功后开始解析数据
//                processData(result);
                Gson gson = new Gson();
                NewsCenterBean newsCenterBean = gson.fromJson(result, NewsCenterBean.class);
                listener.onSuccess(newsCenterBean);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
                listener.onFailed((Exception) ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished==");
            }
        });
    }
    //从本地获取数据
    @Override
    public void fromLocalData(String url, OnRequestListener listener) {
        //从本地获取保存的数据
        String saveJson = CacheUtils.getString(MyApplication.getInstance(), Constants.NEWSCENTER_PAGER_URL);//""
        if(!TextUtils.isEmpty(saveJson)){
            NewsCenterBean newsCenterBean = paraseJson(saveJson);
            listener.onSuccess(newsCenterBean);
        }
    }
    /**
     * 手动解析json数据使用系统的api
     *
     * @param json
     * @return
     */
    private NewsCenterBean paraseJson(String json) {

        NewsCenterBean centerBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            centerBean.setRetcode(retcode);
            JSONArray data = jsonObject.optJSONArray("data");

            //数据集合
            List<NewsCenterBean.DataBean> dataBeans = new ArrayList<>();
            centerBean.setData(dataBeans);

            for (int i = 0; i < data.length(); i++) {
                JSONObject itemObject = (JSONObject) data.get(i);
                if (itemObject != null) {


                    //集合装数据
                    NewsCenterBean.DataBean itemBean = new NewsCenterBean.DataBean();
                    dataBeans.add(itemBean);

                    int id = itemObject.optInt("id");
                    itemBean.setId(id);
                    String title = itemObject.optString("title");
                    itemBean.setTitle(title);
                    int type = itemObject.optInt("type");
                    itemBean.setType(type);
                    String url = itemObject.optString("url");
                    itemBean.setUrl(url);
                    String url1 = itemObject.optString("url1");
                    itemBean.setUrl1(url1);
                    String excurl = itemObject.optString("excurl");
                    itemBean.setExcurl(excurl);
                    String dayurl = itemObject.optString("dayurl");
                    itemBean.setDayurl(dayurl);
                    String weekurl = itemObject.optString("weekurl");
                    itemBean.setDayurl(weekurl);

                    JSONArray children = itemObject.optJSONArray("children");

                    if (children != null && children.length() > 0) {


                        //设置children的数据
                        List<NewsCenterBean.DataBean.ChildrenBean> childrenBeans = new ArrayList<>();
                        itemBean.setChildren(childrenBeans);
                        for (int j = 0; j < children.length(); j++) {

                            NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            //添加到集合中
                            childrenBeans.add(childrenBean);
                            JSONObject childenObje = (JSONObject) children.get(j);
                            int idc = childenObje.optInt("id");
                            childrenBean.setId(idc);
                            String titlec = childenObje.optString("title");
                            childrenBean.setTitle(titlec);
                            int typec = childenObje.optInt("type");
                            childrenBean.setType(typec);
                            String urlc = childenObje.optString("url");
                            childrenBean.setUrl(urlc);


                        }
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return centerBean;
    }
}
