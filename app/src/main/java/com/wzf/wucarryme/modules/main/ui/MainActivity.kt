package com.wzf.wucarryme.modules.main.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseActivity
import com.wzf.wucarryme.common.C
import com.wzf.wucarryme.common.utils.CircularAnimUtil
import com.wzf.wucarryme.common.utils.RxDrawer
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.modules.about.ui.AboutActivity
import com.wzf.wucarryme.modules.buysell.ui.PositionActivity
import com.wzf.wucarryme.modules.city.ui.ChoiceCityActivity
import com.wzf.wucarryme.modules.main.adapter.HomePagerAdapter
import com.wzf.wucarryme.modules.service.CollectorService
import com.wzf.wucarryme.modules.setting.ui.SettingActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.viewPager)
    lateinit var mViewPager: ViewPager
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar
    @BindView(R.id.tabLayout)
    lateinit var mTabLayout: TabLayout
    @BindView(R.id.fab)
    lateinit var mFab: FloatingActionButton
    @BindView(R.id.nav_view)
    lateinit var mNavView: NavigationView
    @BindView(R.id.drawer_layout)
    lateinit var mDrawerLayout: DrawerLayout

    private var mMainFragment: MainFragment? = null
    private var mMultiCityFragment: SelfSelectStockFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initDrawer()
        initIcon()
        //        startService(new Intent(this, AutoUpdateService.class));
        startService(Intent(this, CollectorService::class.java))
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun onRestart() {
        super.onRestart()
        initIcon()
    }

    /**
     * 初始化基础View
     */
    private fun initView() {
        setSupportActionBar(mToolbar)
        mFab.setOnClickListener { showShareDialog() }
        val mAdapter = HomePagerAdapter(supportFragmentManager)
        mMainFragment = MainFragment()
        mMultiCityFragment = SelfSelectStockFragment()
        mAdapter.addTab(mMainFragment!!, "blank")
        mAdapter.addTab(mMultiCityFragment!!, "list")
        mViewPager.adapter = mAdapter
        val fabVisibilityChangedListener = FabVisibilityChangedListener()
        mTabLayout.setupWithViewPager(mViewPager, false)
        mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (mFab.isShown) {
                    fabVisibilityChangedListener.position = position
                    mFab.hide(fabVisibilityChangedListener)
                } else {
                    changeFabState(position)
                    mFab.show()
                }
            }
        })
    }

    private inner class FabVisibilityChangedListener : FloatingActionButton.OnVisibilityChangedListener() {

        var position: Int = 0

        override fun onHidden(fab: FloatingActionButton?) {
            changeFabState(position)
            fab!!.show()
        }
    }

    /**
     * 初始化抽屉
     */
    private fun initDrawer() {
        mNavView.setNavigationItemSelectedListener(this)
        mNavView.inflateHeaderView(R.layout.nav_header_main)
        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    /**
     * 初始化 Icons
     */
    private fun initIcon() {
        SharedPreferenceUtil.instance.putInt("BUY", R.mipmap.ic_event_available_black_36dp)
        SharedPreferenceUtil.instance.putInt("SELL", R.mipmap.ic_event_busy_black_36dp)
        SharedPreferenceUtil.instance.putInt("POS", R.mipmap.ic_event_note_black_36dp)
        SharedPreferenceUtil.instance.putString(SharedPreferenceUtil.ALERT_STOCK_NAME + "创业板指", "<1456")
        SharedPreferenceUtil.instance.putString(SharedPreferenceUtil.ALERT_STOCK_NAME + "上证指数", "<2856")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        RxDrawer.close(this.mDrawerLayout)
            .doOnNext {
                when (item.itemId) {
                    R.id.nav_set          -> SettingActivity.launch(this@MainActivity)
                    R.id.nav_about        -> AboutActivity.launch(this@MainActivity)
                    R.id.nav_city         -> ChoiceCityActivity.launch(this@MainActivity)
                    R.id.nav_position     -> PositionActivity.launch(this@MainActivity)
                    R.id.nav_multi_cities -> mViewPager.currentItem = 1
                }
            }
            .subscribe()
        return false
    }

    private fun changeFabState(position: Int) {
        if (position == 1) {
            mFab.setImageResource(R.drawable.ic_add_24dp)
            mFab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color
                .colorPrimary))
            mFab.setOnClickListener {
                val intent = Intent(this@MainActivity, ChoiceCityActivity::class.java)
                intent.putExtra(C.MULTI_CHECK, true)
                CircularAnimUtil.startActivity(this@MainActivity, intent, mFab, R.color.colorPrimary)
            }
        } else {
            mFab.setImageResource(R.drawable.ic_favorite)
            mFab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color
                .colorAccent))
            mFab.setOnClickListener { showShareDialog() }
        }
    }

    private fun showShareDialog() {
        // wait to do
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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

    companion object {

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
