package com.wzf.wucarryme.common.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import com.wzf.wucarryme.base.BaseApplication

object EnvUtil {

    private val TAG = EnvUtil::class.java.simpleName
    private var sStatusBarHeight: Int = 0

    val statusBarHeight: Int
        get() {
            if (sStatusBarHeight == 0) {
                val resourceId = BaseApplication.appContext!!.resources.getIdentifier("status_bar_height",
                    "dimen",
                    "android")
                if (resourceId > 0) {
                    sStatusBarHeight = BaseApplication.appContext!!.resources.getDimensionPixelSize(resourceId)
                }
            }
            return sStatusBarHeight
        }

    fun getActionBarSize(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else DensityUtil.dp2px(44f)
    }

    /**
     * 获取底部 navigation bar 高度
     */
    fun getNavigationBarHeight(mActivity: Activity): Int {
        val resources = mActivity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        LogUtil.i(TAG, "Nav height:" + height)
        return height
    }

}