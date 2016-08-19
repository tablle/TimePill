package com.tablle.timepill.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tablle.timepill.R;
import com.tablle.timepill.listener.AppBarStateChangeListener;
import com.tablle.timepill.model.DiaryBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;

public class DiaryDetailActivity extends AppCompatActivity {

    private TextView diary_detail_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        DiaryBean diary = (DiaryBean) getIntent().getSerializableExtra("diary");

       /* Toolbar diary_detail_toolbar = (Toolbar) findViewById(R.id.diary_detail_toolbar);
        this.setSupportActionBar(diary_detail_toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }*/
        Toolbar diary_detail_toolbar = (Toolbar) findViewById(R.id.diary_detail_toolbar);
        //diary_detail_toolbar.setNavigationIcon(R.drawable.arrow_back);
        this.setSupportActionBar(diary_detail_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle("详情页面");

        collapsing_toolbar.setCollapsedTitleTextColor(Color.WHITE);

        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);//伸展
        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);//收缩
        //collapsing_toolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        //collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        diary_detail_reply = (TextView) findViewById(R.id.diary_detail_reply);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {

                    //展开状态
                    diary_detail_reply.setVisibility(View.GONE);
                }else if(state == State.COLLAPSED){

                    //折叠状态
                    diary_detail_reply.setVisibility(View.VISIBLE);
                    diary_detail_reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(DiaryDetailActivity.this,"点击回复了啊",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {

                    //中间状态

                }
            }
        });

        initDiary(diary);

    }

    private void initDiary(DiaryBean diary) {
        TextView diary_detail_notebook = (TextView) findViewById(R.id.diary_detail_notebook);
        TextView diary_detail_time = (TextView) findViewById(R.id.diary_detail_time);
        TextView diary_detail_content = (TextView) findViewById(R.id.diary_detail_content);
        final ImageView diary_detail_photo = (ImageView) findViewById(R.id.diary_detail_photo);
        final ImageView diary_detail_icon = (ImageView) findViewById(R.id.diary_detail_icon);

        diary_detail_notebook.setText(diary.notebook_subject);
        diary_detail_content.setText(diary.content);
        diary_detail_time.setText(diary.getCreatedTimeString());
        if (diary.type.equals("2") && diary.photoThumbUrl != null) {
            diary_detail_photo.setVisibility(View.VISIBLE);
            OkHttpUtils
                    .get()//
                    .url(diary.photoUrl)//
                    .build()//
                    .execute(new BitmapCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(Bitmap response, int id) {
                            diary_detail_photo.setImageBitmap(response);
                        }
                    });
        } else {
            diary_detail_photo.setVisibility(View.GONE);
        }
        /*OkHttpUtils
                .get()//
                .url(diary.user.iconUrl)//
                .build()//
                .execute(new BitmapCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        diary_detail_icon.setImageBitmap(response);
                    }
                });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*switch (id){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }*/

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        /*if (id == R.id.home){
            finish();
            return true;
        }*/
        /*case android.R.id.home:
        finish();
        return true;*/
        if(item.getItemId() == android.R.id.home)
        {
            DiaryDetailActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
