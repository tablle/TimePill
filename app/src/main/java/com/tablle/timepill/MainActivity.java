package com.tablle.timepill;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tablle.timepill.fragment.AboutMeFragment;
import com.tablle.timepill.fragment.ConcernMeFragment;
import com.tablle.timepill.fragment.HomeFragment;
import com.tablle.timepill.fragment.MyConcernFragment;
import com.tablle.timepill.fragment.MyNoteBookFragment;
import com.tablle.timepill.listener.OnCanSetLayoutParamsListener;
import com.tablle.timepill.listener.OnMakeSnackbar;
import com.tablle.timepill.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements OnMakeSnackbar {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.main_fab)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.navigation_view)
    NavigationView navigation_view;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentMenu;

    private HomeFragment homeFragment;

    private MyConcernFragment myConcernFragment;

    private ConcernMeFragment concernMeFragment;

    private MyNoteBookFragment myNoteBookFragment;

    private AboutMeFragment aboutMeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation_view.setItemIconTintList(null);
        navigation_view.setItemTextColor(null);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationView(toolbar);
        setupFloatingActionButton();
        switchFragment(R.id.home);
    }

    private void setupFloatingActionButton() {
        Utils.setFabLayoutParams(mFloatingActionButton, new OnCanSetLayoutParamsListener() {
            @Override
            public void onCanSetLayoutParams() {
                // setMargins to fix floating action button's layout bug
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mFloatingActionButton
                        .getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                mFloatingActionButton.setLayoutParams(params);
            }
        });
    }

    private void switchFragment(int menuId) {
        mCurrentMenu = menuId;
        ActionBar actionBar = getSupportActionBar();
        switch (menuId) {
            case R.id.home:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.home);
                }
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                //Toast.makeText(MainActivity.this,R.string.home, Toast.LENGTH_SHORT).show();
                mFloatingActionButton.setVisibility(View.VISIBLE);
                replaceMainFragment(homeFragment);
                break;
            case R.id.my_concern:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.my_concern);
                }
                if (myConcernFragment == null) {
                    myConcernFragment = MyConcernFragment.newInstance();
                }
                //Toast.makeText(MainActivity.this,R.string.my_concern,Toast.LENGTH_SHORT).show();
                mFloatingActionButton.setVisibility(View.GONE);
                replaceMainFragment(myConcernFragment);
                break;
            case R.id.concern_me:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.concern_me);
                }
                if (concernMeFragment == null) {
                    concernMeFragment = ConcernMeFragment.newInstance();
                }
                //Toast.makeText(MainActivity.this,R.string.concern_me,Toast.LENGTH_SHORT).show();
                mFloatingActionButton.setVisibility(View.GONE);
                replaceMainFragment(concernMeFragment);
                break;
            case R.id.my_notebook:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.my_notebook);
                }
                if (myNoteBookFragment == null) {
                    myNoteBookFragment = MyNoteBookFragment.newInstance();
                }
                Toast.makeText(MainActivity.this,R.string.my_notebook,Toast.LENGTH_SHORT).show();
                mFloatingActionButton.setVisibility(View.GONE);
                replaceMainFragment(myNoteBookFragment);
                break;
            case R.id.my_profile:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.my_profile);
                }
                if (aboutMeFragment == null) {
                    aboutMeFragment = AboutMeFragment.newInstance();
                }
                Toast.makeText(MainActivity.this,R.string.my_profile,Toast.LENGTH_SHORT).show();
                mFloatingActionButton.setVisibility(View.GONE);
                replaceMainFragment(aboutMeFragment);
                break;
            default:
                break;
        }
    }

    private void setupNavigationView(Toolbar toolbar) {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switchFragment(menuItem.getItemId());
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void replaceMainFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.main_fab)
    public void onClickFAB() {
        switch (mCurrentMenu) {
            case R.id.home:
                if (homeFragment != null) {
                    homeFragment.onClickMainFAB();
                }
                break;
        }
    }

   @Override
    public Snackbar onMakeSnackbar(CharSequence text, int duration) {
        return Snackbar.make(mCoordinatorLayout, text, duration);
    }
}
