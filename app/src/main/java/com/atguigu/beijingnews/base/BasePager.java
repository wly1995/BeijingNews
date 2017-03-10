package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;

/**
 * Created by 万里洋 on 2017/2/5.
 */

public class BasePager {
    /**
     * 上下文
     */
    public final Context mContext;
    public ImageButton ib_menu;
    public TextView tv_title;
    public FrameLayout fl_main;
    public ImageButton ib_swtich_list_gird;
    /**
     * 代表各个页面的实例
     */
    public View rootView;
    public BasePager(Context context){
        this.mContext = context;

        rootView = initView();
    }

    /**
     * 初始化公共部分的视图控件，这样孩子就可以直接用
     * @return
     */
    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager,null);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        fl_main = (FrameLayout) view.findViewById(R.id.fl_main);
        ib_swtich_list_gird = (ImageButton) view.findViewById(R.id.ib_swtich_list_gird);
        //给菜单键设置点击事件
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();//关<->开
            }
        });
        return view;
    }

    /**
     * 1.在子类重新initData方法，实现子类的视图，并且视图在该方法中和基类的Fragmelayout布局结合在一起
     2.绑定数据或者请求数据再绑定数据
     */
    public void  initData(){

    }
}
