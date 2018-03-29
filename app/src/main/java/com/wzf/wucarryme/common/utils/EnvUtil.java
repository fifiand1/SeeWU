package com.wzf.wucarryme.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import com.wzf.wucarryme.base.BaseApplication;

public class EnvUtil {

    private static final String TAG = EnvUtil.class.getSimpleName();
    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return DensityUtil.dp2px(44);
    }

    public static int getStatusBarHeight() {
        if (sStatusBarHeight == 0) {
            int resourceId =
                BaseApplication.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = BaseApplication.getAppContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }

    /**
     * 获取底部 navigation bar 高度
     */
    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogUtil.i(TAG, "Nav height:" + height);
        return height;
    }

}