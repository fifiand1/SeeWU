package com.wzf.wucarryme.base

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BaseFragment : RxFragment() {

    protected var mIsCreateView = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && mIsCreateView) {
            lazyLoad()
        }
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    protected abstract fun lazyLoad()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {
            lazyLoad()
        }
    }

    protected fun safeSetTitle(title: String) {
        val appBarLayout = (activity as AppCompatActivity).supportActionBar
        if (appBarLayout != null) {
            appBarLayout.title = title
        }
    }
}
