package com.tablle.timepill.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tablle.timepill.R;
import com.tablle.timepill.adapter.DiaryCommentAdapter;
import com.tablle.timepill.listener.AppBarStateChangeListener;
import com.tablle.timepill.model.DiaryBean;
import com.tablle.timepill.utils.Utils;
import com.tablle.timepill.widget.DividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;

public class DiaryDetailActivity extends AppCompatActivity {

    private TextView diary_detail_reply;
    private DiaryCommentAdapter diaryCommentAdapter;
    private RecyclerView comment_rv;
    private LinearLayoutManager layoutManager;
    private ImageView diary_detail_icon;


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
        initDiary(diary);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {

                    //展开状态
                    diary_detail_reply.setVisibility(View.GONE);
                    diary_detail_icon.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {

                    //折叠状态
                    diary_detail_reply.setVisibility(View.VISIBLE);
                    diary_detail_icon.setVisibility(View.GONE);
                    diary_detail_reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(DiaryDetailActivity.this, "点击回复了啊", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    //中间状态
                    diary_detail_icon.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    private void initDiary(DiaryBean diary) {
        TextView diary_detail_notebook = (TextView) findViewById(R.id.diary_detail_notebook);
        TextView diary_detail_time = (TextView) findViewById(R.id.diary_detail_time);
        TextView diary_detail_content = (TextView) findViewById(R.id.diary_detail_content);
        TextView comment_empty_tv = (TextView) findViewById(R.id.comment_empty_tv);
        comment_rv = (RecyclerView) findViewById(R.id.comment_rv);

        comment_rv.addItemDecoration(
                new DividerItemDecoration(Utils.getDrawableResource(this, R.drawable.line)));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comment_rv.setLayoutManager(layoutManager);
        comment_rv.setHasFixedSize(true);
        comment_rv.setItemAnimator(new DefaultItemAnimator());
        
        final ImageView diary_detail_photo = (ImageView) findViewById(R.id.diary_detail_photo);
        diary_detail_icon = (ImageView) findViewById(R.id.diary_detail_icon);

        diary_detail_notebook.setText(diary.notebook_subject);
        diary_detail_content.setText(diary.content);
        diary_detail_time.setText(diary.getCreatedTimeString());
        if(diary.comment_count.equals("0")){
            comment_empty_tv.setVisibility(View.VISIBLE);
           // comment_rv.setVisibility(View.GONE);
        }else{
            comment_empty_tv.setVisibility(View.GONE);
           // comment_rv.setVisibility(View.VISIBLE);
            int diaryId = diary.id;
            String commentCount = diary.comment_count;
            initComment(diaryId,commentCount);


        }
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
        OkHttpUtils
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
                });
    }

    private void initComment(int diaryId, final String commentCount) {
        int diary_id = diaryId;
        //String url = Url.URL_Diary_COMMENT;
        String url = "https://open.timepill.net/api/diaries/"+diary_id+"/comments";
        String email = "tbl0311@126.com";
        String password = "603616203";
        String token = getBase64Token(email, password);
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("Authorization", token)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("faile=======" + e.toString());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        System.out.println("success=========================" + response);

                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println("arr=================="+arr);
                            diaryCommentAdapter = new DiaryCommentAdapter(DiaryDetailActivity.this,arr);
                            System.out.println("33333==================");
                            comment_rv.setAdapter(diaryCommentAdapter);
                            System.out.println("after========-=========================" );
                            // homePageAdapter.notifyDataSetChanged();
                            diaryCommentAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       /* Type listType = new TypeToken<LinkedList<CommentBean>>(){}.getType();
                        Gson gson = new Gson();
                        LinkedList<CommentBean> comments = gson.fromJson(response, listType);
                        System.out.println("comments=================="+comments);
                        for (Iterator iterator = comments.iterator(); iterator.hasNext();) {
                            CommentBean comment = (CommentBean) iterator.next();
                            System.out.println("comment=================="+comment);
                        }
*/


                        /*System.out.println("444444==================");
                        homePageAdapter.setOnItemClickLitener(new HomepageAdapter.OnItemClickLitener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(),DiaryDetailActivity.class);
                                DiaryBean diary = diaries.get(position);
                                intent.putExtra("diary", diary);
                                startActivity(intent);
                            }
                        });
*/
                    }
                });
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

    private String getBase64Token(String email, String password) {
        return "Basic " + Base64.encodeToString((email + ":" + password).getBytes(), Base64.NO_WRAP);
    }

}
