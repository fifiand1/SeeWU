package com.wzf.wucarryme.modules.main.ui;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseActivity;
import com.wzf.wucarryme.common.C;
import com.wzf.wucarryme.common.utils.CircularAnimUtil;
import com.wzf.wucarryme.common.utils.RxDrawer;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.modules.about.ui.AboutActivity;
import com.wzf.wucarryme.modules.city.ui.ChoiceCityActivity;
import com.wzf.wucarryme.modules.main.adapter.HomePagerAdapter;
import com.wzf.wucarryme.modules.service.CollectorService;
import com.wzf.wucarryme.modules.setting.ui.SettingActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private MainFragment mMainFragment;
    private SelfSelectStockFragment mMultiCityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDrawer();
        initIcon();
//        startService(new Intent(this, AutoUpdateService.class));
        startService(new Intent(this, CollectorService.class));
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initIcon();
    }

    /**
     * 初始化基础View
     */
    private void initView() {
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(v -> showShareDialog());
        HomePagerAdapter mAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mMainFragment = new MainFragment();
        mMultiCityFragment = new SelfSelectStockFragment();
        mAdapter.addTab(mMainFragment, "");
        mAdapter.addTab(mMultiCityFragment, "");
        mViewPager.setAdapter(mAdapter);
        FabVisibilityChangedListener fabVisibilityChangedListener = new FabVisibilityChangedListener();
        mTabLayout.setupWithViewPager(mViewPager, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mFab.isShown()) {
                    fabVisibilityChangedListener.position = position;
                    mFab.hide(fabVisibilityChangedListener);
                } else {
                    changeFabState(position);
                    mFab.show();
                }
            }
        });
    }

    private class FabVisibilityChangedListener extends FloatingActionButton.OnVisibilityChangedListener {

        private int position;

        @Override
        public void onHidden(FloatingActionButton fab) {
            changeFabState(position);
            fab.show();
        }
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
            mNavView.inflateHeaderView(R.layout.nav_header_main);
            ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    /**
     * 初始化 Icons
     */
    private void initIcon() {
        SharedPreferenceUtil.getInstance().putInt("BUY", R.mipmap.ic_event_available_black_36dp);
        SharedPreferenceUtil.getInstance().putInt("SELL", R.mipmap.ic_event_busy_black_36dp);
        SharedPreferenceUtil.getInstance().putInt("POS", R.mipmap.ic_event_note_black_36dp);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        RxDrawer.close(mDrawerLayout)
            .doOnNext(o -> {
                switch (item.getItemId()) {
                    case R.id.nav_set:
                        SettingActivity.launch(MainActivity.this);
                        break;
                    case R.id.nav_about:
                        AboutActivity.launch(MainActivity.this);
                        break;
                    case R.id.nav_city:
                        ChoiceCityActivity.launch(MainActivity.this);
                        break;
                    case R.id.nav_multi_cities:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            })
            .subscribe();
        return false;
    }

    private void changeFabState(int position) {
        if (position == 1) {
            mFab.setImageResource(R.drawable.ic_add_24dp);
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color
                .colorPrimary)));
            mFab.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChoiceCityActivity.class);
                intent.putExtra(C.MULTI_CHECK, true);
                CircularAnimUtil.startActivity(MainActivity.this, intent, mFab, R.color.colorPrimary);
            });
        } else {
            mFab.setImageResource(R.drawable.ic_favorite);
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color
                .colorAccent)));
            mFab.setOnClickListener(v -> showShareDialog());
        }
    }

    private void showShareDialog() {
        // wait to do
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//        else {
//            不退出
//            if (!DoubleClickExit.check()) {
//                ToastUtil.showShort(getString(R.string.double_exit));
//            } else {
//                finish();
//            }
//        }
    }
}
