package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.bean.TabDetailPagerBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.atguigu.beijingnews.detailpager.TabDetailPager.ID_ARRAY;

/**
 * Created by 万里洋 on 2017/2/9.
 */

public class TabDetailPagerAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<TabDetailPagerBean.DataEntity.NewsEntity> datas;

    public TabDetailPagerAdapter(Context mContext, List<TabDetailPagerBean.DataEntity.NewsEntity> news) {
        this.mContext = mContext;
        this.datas = news;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_tab_detailpager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置得到相应的item数据  datas就是每一个详情页面的列表数据
        TabDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        viewHolder.tvTitle.setText(newsEntity.getTitle());
        viewHolder.tvTime.setText(newsEntity.getPubdate());
        //加载图片
        Glide.with(mContext).load(Constants.BASE_URL+newsEntity.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .into(viewHolder.ivIcon);
        //重新从取出保存的id数组
        String idArray = CacheUtils.getString(mContext, ID_ARRAY);//
        if(idArray.contains(newsEntity.getId()+"")){
            //该条被点击过-灰色
            viewHolder.tvTitle.setTextColor(Color.GRAY);
        }else{
            //设置黑色
            viewHolder.tvTitle.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
