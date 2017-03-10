package com.atguigu.beijingnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.atguigu.baselibrary.CacheUtils;
import com.atguigu.beijingnews.activity.GuideActivity;
import com.atguigu.beijingnews.activity.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    private RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        //设置欢迎界面的动画
        setAnimation();
    }

    private void setAnimation() {
        //三个动画：旋转动画，渐变动画，缩放动画
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);//设置持续时间
        ra.setFillAfter(true);//设置停留在旋转后的状态


        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);//设置持续时间
        aa.setFillAfter(true);//设置停留在旋转后的状态

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);//设置持续时间
        sa.setFillAfter(true);//设置停留在旋转后的状态

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(ra);
        set.addAnimation(sa);

        //开始播放动画
        activity_main.startAnimation(set);
        //监听动画播放完成
        set.setAnimationListener(new MyAnimationListener());
    }
    class MyAnimationListener implements Animation.AnimationListener {
        /**
         * 动画开始播放的时候回调此方法
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 动画播放结束时回调的方法
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {

            boolean startMain = CacheUtils.getBoolean(WelcomeActivity.this,"start_main");
            Intent intent = null;
            if(startMain){
                //进入主页面
                intent = new Intent(WelcomeActivity.this,MainActivity.class);
            }else{
                intent = new Intent(WelcomeActivity.this,GuideActivity.class);

            }
            startActivity(intent);

            finish();
        }

        /**
         * 动画重复播放时回调此方法
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
