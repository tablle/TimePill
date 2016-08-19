package com.tablle.timepill.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tablle.timepill.R;
import com.tablle.timepill.constant.Url;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class AboutMeFragment extends Fragment {
    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    /*@Bind(R.id.aboutme_coverd_image)
    ImageView mIvPlaceholder; // 大图片*/

    @Bind(R.id.main_ll_title_container)
    LinearLayout mLlTitleContainer; // Title的LinearLayout

    @Bind(R.id.main_fl_title)
    FrameLayout mFlTitleContainer; // Title的FrameLayout

    @Bind(R.id.main_abl_app_bar)
    AppBarLayout mAblAppBar; // 整个可以滑动的AppBar

    /*@Bind(R.id.main_tv_toolbar_title)
    TextView mTvToolbarTitle; // 标题栏Title*/

    @Bind(R.id.main_tb_toolbar)
    Toolbar mTbToolbar; // 工具栏

    @Bind(R.id.aboutme_person_intro)
    TextView boutme_person_intro;

    @Bind(R.id.aboutme_person_name)
    TextView aboutme_person_name;

    @Bind(R.id.aboutme_person_title)
    TextView aboutme_person_title;

    @Bind(R.id.aboutme_toolbar_name)
    TextView aboutme_toolbar_name;

    @Bind(R.id.aboutme_icon_image)
    ImageView aboutme_icon_image;

    @Bind(R.id.aboutme_coverd_image)
    ImageView aboutme_coverd_image;
    private AppBarLayout appBar;

    public AboutMeFragment() {
        // Required empty public constructor
    }

    public static AboutMeFragment newInstance() {
        return new AboutMeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);
        ButterKnife.bind(this, view);

        mTbToolbar.setTitle("");

        initData();

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //appBar = (AppBarLayout) getActivity().findViewById(R.id.appbarLayout);

        // AppBar的监听
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);


                //AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getChildAt(0).getLayoutParams();
               /* Toolbar.LayoutParams params = (Toolbar.LayoutParams) toolbar.getChildAt(0).getLayoutParams();
                if(verticalOffset == 0){ //设置不滑动
                    params.setScrollFlags(0); //AppBarLayout 自动滑出来
                    //toolbar.setExpanded(true);
                }else {
                    //设置滑动
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                }
                //layout是childAt(0)
                toolbar.getChildAt(0).setLayoutParams(params);
                //下面是设置两个属性
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);*/

                /*if(verticalOffset==0){
                    toolbar.setS
                    Toast.makeText(getActivity(), "展开", Toast.LENGTH_SHORT).show();
                }else if(verticalOffset==1){
                    toolbar.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"收缩",Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        initParallaxValues(); // 自动滑动效果

        return view;
    }

    private void initData() {
        String email = "tbl0311@126.com";
        String password = "603616203";
        String token;
        token = getBase64Token(email, password);

        OkHttpUtils
                .get()
                .url(Url.URL_URERS_MY + "?page=1" + "&page_size=20")
                .addHeader("Authorization", token)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("faile=======" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String name = json.getString("name");
                            String intro = json.getString("intro");
                            String created = json.getString("created");
                            String iconUrl = json.getString("iconUrl");
                            String coverUrl = json.getString("coverUrl");

                            boutme_person_intro.setText(intro);
                            aboutme_person_name.setText(name);
                            aboutme_person_title.setText(created);
                            aboutme_toolbar_name.setText(name);

                            setImage(iconUrl, coverUrl);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void setImage(String iconUrl, String coverUrl) {
        OkHttpUtils
                .get()//
                .url(iconUrl)//
                .build()//
                .execute(new BitmapCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        aboutme_icon_image.setImageBitmap(response);
                    }
                });

        OkHttpUtils
                .get()//
                .url(coverUrl)//
                .build()//
                .execute(new BitmapCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        aboutme_coverd_image.setImageBitmap(response);
                    }
                });

    }

    // 设置自动滑动的动画效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) aboutme_coverd_image.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        aboutme_coverd_image.setLayoutParams(petDetailsLp);
        mFlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(aboutme_toolbar_name, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(aboutme_toolbar_name, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private String getBase64Token(String email, String password) {
        return "Basic " + Base64.encodeToString((email + ":" + password).getBytes(), Base64.NO_WRAP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
