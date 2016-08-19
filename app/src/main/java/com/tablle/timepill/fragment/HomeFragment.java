package com.tablle.timepill.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tablle.timepill.R;
import com.tablle.timepill.activity.WriteNewDiaryActivity;
import com.tablle.timepill.adapter.ViewPagerAdapter;
import com.tablle.timepill.fragment.home.HomePageFragment;
import com.tablle.timepill.listener.OnClickMainFABListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public class HomeFragment extends Fragment implements OnClickMainFABListener{
    private static final int REQUEST_CREATE_TASK_ACTIVITY = 1000;
    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onClickMainFAB() {
        Intent intent = new Intent(getActivity(), WriteNewDiaryActivity.class);
        startActivity(intent);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = findById(view, R.id.tab_layout);
        viewPager = findById(view, R.id.home_viewpager);
        setupViewPager(viewPager);
        //tabLayout.setupWithViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(3);
    }

    private void setupViewPager(ViewPager viewPager) {
        /*final HomeAdapter adapter = new HomeAdapter(getChildFragmentManager());
        adapter.addCategory(HomeHelper.HOME_PAGE);
        adapter.addCategory(HomeHelper.ATTENTION_PAGE);
        adapter.addCategory(HomeHelper.TOPIC_PAGE);
        adapter.addCategory(HomeHelper.MINE_PAGE);
        viewPager.setAdapter(adapter);*/

        /*HomePageFragment homePageFragment = new HomePageFragment();
        AttentionFragment attentionFragment = new AttentionFragment();
        TopicPageFragment topicPageFragment = new TopicPageFragment();
        MinePageFragment minePageFragment = new MinePageFragment();*/

        HomePageFragment homePageFragment = new HomePageFragment();
        HomePageFragment attentionFragment = new HomePageFragment();
        HomePageFragment topicPageFragment = new HomePageFragment();
        HomePageFragment minePageFragment = new HomePageFragment();

        list_fragment = new ArrayList<>();
        list_fragment.add(homePageFragment);
        list_fragment.add(attentionFragment);
        list_fragment.add(topicPageFragment);
        list_fragment.add(minePageFragment);

        list_Title = new ArrayList<>();
        list_Title.add("最新");
        list_Title.add("关注");
        list_Title.add("话题");
        list_Title.add("我的");

        //设置TabLayout的模式
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_Title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_Title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_Title.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(list_Title.get(3)));

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getChildFragmentManager(),list_fragment,list_Title);
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
