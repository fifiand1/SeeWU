package com.wzf.wucarryme.common.utils

import android.widget.Toast
import com.wzf.wucarryme.base.BaseApplication

object ToastUtil {

    fun showShort(msg: String) {
        Toast.makeText(BaseApplication.appContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLong(msg: String) {
        Toast.makeText(BaseApplication.appContext, msg, Toast.LENGTH_LONG).show()
    }
}
