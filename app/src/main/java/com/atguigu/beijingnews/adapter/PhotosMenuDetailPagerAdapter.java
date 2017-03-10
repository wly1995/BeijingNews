package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.baselibrary.BitmapCacheUtils;
import com.atguigu.baselibrary.Constants;
import com.atguigu.baselibrary.NetCacheUtils;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.beijingnews.bean.PhotosMenuDetailPagerBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 万里洋 on 2017/2/10.
 */

public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> datas;
    private final BitmapCacheUtils bitmapCacheUtils;
    private DisplayImageOptions options;
    private  RecyclerView recyclerview;
    /**
     * 用于在子线程中请求到的数据
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SECUSS://图片请求成功
                    //位置
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if(recyclerview.isShown()) {
                        //根据标识过去图片的对象
                        ImageView ivIcon = (ImageView) recyclerview.findViewWithTag(position);
                        if(ivIcon != null&& bitmap != null){
                            Log.e("TAG","网络缓存图片显示成功"+position);
                            //显示到控件上
                            ivIcon.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAIL://图片请求失败
                    position = msg.arg1;
                    Log.e("TAG","网络缓存失败"+position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity>
            news,RecyclerView recyclerview) {
        this.mContext = mContext;
        this.datas = news;
        this.recyclerview = recyclerview;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default)
                .showImageForEmptyUri(R.drawable.pic_item_list_default)
                .showImageOnFail(R.drawable.pic_item_list_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_photosmenu_detail_pager, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //根据位置获取一个item数据对象的bean对象
        PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = datas.get(position);
        holder.tvTitle.setText(newsEntity.getTitle());
        //1.设置图片
        //加载图片
//        Glide.with(mContext).load(Constants.BASE_URL+newsEntity.getListimage())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.news_pic_default)
//                .error(R.drawable.news_pic_default)
//                .into(holder.ivIcon);
        //2.三级缓存（自定义的加载图片的方式）
        //设置标识
//        holder.ivIcon.setTag(position);
//
//
//        //这个方法返回的就是null，因为从网络上请求的没有直接返回，而是用handler发消息返回了
//        Bitmap bitmap = bitmapCacheUtils.getBitmapFromNet(Constants.BASE_URL+newsEntity.getListimage(),position);
//        if(bitmap != null) {//所以如果不为空，必定不是从网络上请求得到的而是从内存或者本地
//            Log.e("TAG","我是本地得到的哦=="+bitmap);
//            holder.ivIcon.setImageBitmap(bitmap);
//        }
        //3.imageLoader加载图片
        ImageLoader.getInstance().displayImage(Constants.BASE_URL+newsEntity.getListimage(), holder.ivIcon, options);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
                    intent.putExtra("url",Constants.BASE_URL+datas.get(getLayoutPosition()).getListimage());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
