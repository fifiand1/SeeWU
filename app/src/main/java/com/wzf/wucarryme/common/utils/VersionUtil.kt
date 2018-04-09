package com.wzf.wucarryme.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.wzf.wucarryme.R
import com.wzf.wucarryme.component.RetrofitSingleton
import com.wzf.wucarryme.modules.about.domain.Version

object VersionUtil {

    fun getVersion(context: Context?): String {
        try {
            val manager = context!!.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return context!!.getString(R.string.can_not_find_version_name)
        }

    }

    /**
     * @return 版本号
     */
    fun getVersionCode(context: Context): Int {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

    }

    fun checkVersion(context: Context) {
        RetrofitSingleton.instance
                .fetchVersion()
                .doOnNext({ version ->
                    val firVersionName = version.versionShort
                    val currentVersionName = VersionUtil.getVersion(context)
                    if (currentVersionName.compareTo(firVersionName!!) < 0) {
                        if (SharedPreferenceUtil.instance.getString("version", "") != version.versionShort) {
                            showUpdateDialog(version, context)
                        }
                    }
                })
                .subscribe()
    }

    fun checkVersion(context: Context, force: Boolean) {
        RetrofitSingleton.instance
                .fetchVersion()
                .doOnNext({ version ->
                    val firVersionName = version.versionShort
                    val currentVersionName = VersionUtil.getVersion(context)
                    if (currentVersionName.compareTo(firVersionName!!) < 0) {
                        showUpdateDialog(version, context)
                    } else {
                        ToastUtil.showShort("已经是最新版本(⌐■_■)")
                    }
                })
                .subscribe()
    }

    private fun showUpdateDialog(versionAPI: Version, context: Context) {
        val title = "发现新版" + versionAPI.name + "版本号：" + versionAPI.versionShort

        AlertDialog.Builder(context).setTitle(title)
                .setMessage(versionAPI.changelog)
                .setPositiveButton("下载") { dialog, which ->
                    val uri = Uri.parse(versionAPI.updateUrl)
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = uri
                    context.startActivity(intent)
                }
                .setNegativeButton("跳过此版本"
                ) { dialog, which -> SharedPreferenceUtil.instance.putString("version", versionAPI.versionShort) }
                .show()
    }
}
