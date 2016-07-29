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
import com.tablle.timepill.adapter.HomeAdapter;
import com.tablle.timepill.helper.HomeHelper;
import com.tablle.timepill.listener.OnClickMainFABListener;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public class HomeFragment extends Fragment implements OnClickMainFABListener{
    private static final int REQUEST_CREATE_TASK_ACTIVITY = 1000;

    private OnFragmentInteractionListener mListener;

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
        TabLayout tabLayout = findById(view, R.id.tab_layout);
        ViewPager viewPager = findById(view, R.id.home_viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        final HomeAdapter adapter = new HomeAdapter(getChildFragmentManager());
        adapter.addCategory(HomeHelper.HOME_PAGE);
        adapter.addCategory(HomeHelper.ATTENTION_PAGE);
        adapter.addCategory(HomeHelper.TOPIC_PAGE);
        adapter.addCategory(HomeHelper.MINE_PAGE);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
