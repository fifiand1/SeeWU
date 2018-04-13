package com.wzf.wucarryme.base

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import com.wzf.wucarryme.R

abstract class ToolbarActivity : BaseActivity() {

    private var mAppBar: AppBarLayout? = null
    var toolbar: Toolbar? = null
    private var mIsHidden = false

    private fun onToolbarClick() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppBar = findViewById(R.id.appbar_layout)
        toolbar = findViewById(R.id.toolbar)
        if (toolbar == null || mAppBar == null) {
            throw IllegalStateException(
                "The subclass of ToolbarActivity must contain a toolbar.")
        }
        toolbar!!.setOnClickListener { onToolbarClick() }
        setSupportActionBar(toolbar)
        if (canBack()) {
            val actionBar = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar!!.elevation = 10.6f
        }
    }

    open fun canBack(): Boolean {
        return true
    }

    protected fun setAppBarAlpha(alpha: Float) {
        mAppBar!!.alpha = alpha
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    protected fun hideOrShowToolbar() {
        mAppBar!!.animate()
            .translationY((if (mIsHidden) 0 else -mAppBar!!.height).toFloat())
            .setInterpolator(DecelerateInterpolator(2f))
            .start()
        mIsHidden = !mIsHidden
    }

    protected fun safeSetTitle(title: String) {
        val appBarLayout = supportActionBar
        if (appBarLayout != null) {
            appBarLayout.title = title
        }
    }
}
