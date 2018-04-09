package com.wzf.wucarryme.modules.setting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity

class SettingActivity : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar!!.title = "设置"
        fragmentManager.beginTransaction().replace(R.id.frameLayout, SettingFragment()).commit()
    }

    override fun layoutId(): Int {
        return R.layout.activity_setting
    }

    override fun onPause() {
        super.onPause()
    }

    override fun canBack(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {

        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }
}
