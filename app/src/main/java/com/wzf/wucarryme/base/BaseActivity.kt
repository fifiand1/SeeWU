package com.wzf.wucarryme.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        ButterKnife.bind(this)
    }

    protected abstract fun layoutId(): Int

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setTheme(activity: AppCompatActivity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        activity.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        activity.recreate()
    }

    companion object {
        private val TAG = BaseActivity::class.java.simpleName

        fun setDayTheme(activity: AppCompatActivity) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
            activity.delegate.setLocalNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
            // 调用 recreate() 使设置生效
            activity.recreate()
        }

        fun setNightTheme(activity: AppCompatActivity) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
            activity.delegate.setLocalNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
            // 调用 recreate() 使设置生效
            activity.recreate()
        }

    }
}
