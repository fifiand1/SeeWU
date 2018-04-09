package com.wzf.wucarryme.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

object StatusBarUtil {

    fun setImmersiveStatusBar(activity: Activity) {
        if (SdkUtil.sdkVersionGe21()) {
            activity.window.statusBarColor = Color.TRANSPARENT
        }
        if (SdkUtil.sdkVersionEq(19)) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        activity.window
                .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    fun setImmersiveStatusBarToolbar(toolbar: Toolbar, context: Context) {
        val toolLayoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
        toolLayoutParams.height = EnvUtil.statusBarHeight + EnvUtil.getActionBarSize(context)
        toolbar.layoutParams = toolLayoutParams
        toolbar.setPadding(0, EnvUtil.statusBarHeight, 0, 0)
        toolbar.requestLayout()
    }
}