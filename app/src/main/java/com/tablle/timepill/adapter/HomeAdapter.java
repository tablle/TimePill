package com.tablle.timepill.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tablle.timepill.fragment.home.AttentionFragment;
import com.tablle.timepill.fragment.home.HomePageFragment;
import com.tablle.timepill.fragment.home.MinePageFragment;
import com.tablle.timepill.fragment.home.TopicPageFragment;
import com.tablle.timepill.helper.HomeHelper;

import java.util.ArrayList;
import java.util.List;


public class HomeAdapter extends FragmentPagerAdapter {

    private final List<String> mCategoryNames = new ArrayList<>();

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addCategory(String categoryName) {
        mCategoryNames.add(categoryName);
    }

    @Override
    public Fragment getItem(int position) {
        String categoryName = mCategoryNames.get(position);
        switch (categoryName) {
            case HomeHelper.HOME_PAGE:
                return HomePageFragment.newInstance();

            case HomeHelper.ATTENTION_PAGE:
                return AttentionFragment.newInstance();

            case HomeHelper.TOPIC_PAGE:
                return TopicPageFragment.newInstance();

            case HomeHelper.MINE_PAGE:
                return MinePageFragment.newInstance();


        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return mCategoryNames.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryNames.get(position);
    }
}
