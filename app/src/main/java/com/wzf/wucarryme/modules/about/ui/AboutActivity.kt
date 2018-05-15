package com.wzf.wucarryme.modules.about.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseActivity
import com.wzf.wucarryme.common.utils.StatusBarUtil
import com.wzf.wucarryme.common.utils.Util

class AboutActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar
    @BindView(R.id.toolbar_layout)
    lateinit var mToolbarLayout: CollapsingToolbarLayout
    @BindView(R.id.tv_version)
    lateinit var mTvVersion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setImmersiveStatusBar(this)
        StatusBarUtil.setImmersiveStatusBarToolbar(mToolbar, this)
        initView()
    }

    override fun layoutId(): Int {
        return R.layout.activity_about
    }

    private fun initView() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
//        mTvVersion.text = String.format("当前版本: %s (Build %s)", VersionUtil.getVersion(this), VersionUtil.getVersionCode(this))
        mToolbarLayout.isTitleEnabled = false
        mToolbar.title = getString(R.string.app_name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.bt_code, R.id.bt_blog, R.id.bt_pay, R.id.bt_share, R.id.bt_bug, R.id.bt_update)
    fun onClick(view: View) {
        when (view.id) {
            R.id.bt_code -> goToHtml(getString(R.string.app_html))
            R.id.bt_blog -> goToHtml("http://www.baidu.com")
            R.id.bt_pay -> Util.copyToClipboard(getString(R.string.alipay), this)
            R.id.bt_share -> {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt))
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)))
            }
            R.id.bt_bug -> goToHtml(getString(R.string.bugTableUrl))
            R.id.bt_update -> {
            }
        }//                VersionUtil.checkVersion(this, true);
    }

    private fun goToHtml(url: String) {
        val uri = Uri.parse(url)   //指定网址
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW           //指定Action
        intent.data = uri                            //设置Uri
        startActivity(intent)        //启动Activity
    }

    companion object {

        fun launch(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}
