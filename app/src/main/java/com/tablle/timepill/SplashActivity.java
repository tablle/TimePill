package com.tablle.timepill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * Created by tong on 2016/8/1.
 */
public class SplashActivity extends Activity{
    private ImageView splash_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_iv = (ImageView) findViewById(R.id.splash_iv);

        // 渐变动画
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(1000);
        aa.setFillAfter(true);

        // 播放动画
        splash_iv.setAnimation(aa);

        // 监听动画是否播放完成
        aa.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements AnimationListener {

        /*
         * 当动画开始播放的时候回调这个方法
         */
        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        /*
         * 当动画结束的时候回调这个方法
         */
        @Override
        public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
            // 关闭欢迎页面
            finish();
            }

        /*
         * 当动画重复播放的时候回调这个方法
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

}
