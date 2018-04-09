package com.wzf.wucarryme.modules.setting.ui

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseApplication
import com.wzf.wucarryme.common.C
import com.wzf.wucarryme.common.utils.FileSizeUtil
import com.wzf.wucarryme.common.utils.FileUtil
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.component.ImageLoader
import com.wzf.wucarryme.component.RxBus
import com.wzf.wucarryme.modules.main.domain.ChangeCityEvent
import com.wzf.wucarryme.modules.main.ui.MainActivity
import com.wzf.wucarryme.modules.service.AutoUpdateService
import io.reactivex.Observable
import java.io.File
import java.util.Locale

class SettingFragment : PreferenceFragment(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private var mSharedPreferenceUtil: SharedPreferenceUtil? = null
    private var mChangeIcons: Preference? = null
    private var mChangeUpdate: Preference? = null
    private var mClearCache: Preference? = null
    private var mNotificationType: CheckBoxPreference? = null
    private var mAnimationOnOff: CheckBoxPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
        mSharedPreferenceUtil = SharedPreferenceUtil.instance

        mChangeIcons = findPreference(SharedPreferenceUtil.CHANGE_ICONS)
        mChangeUpdate = findPreference(SharedPreferenceUtil.AUTO_UPDATE)
        mClearCache = findPreference(SharedPreferenceUtil.CLEAR_CACHE)

        mAnimationOnOff = findPreference(SharedPreferenceUtil.ANIM_START) as CheckBoxPreference
        mNotificationType = findPreference(SharedPreferenceUtil.NOTIFICATION_MODEL) as CheckBoxPreference

        mNotificationType!!.isChecked = SharedPreferenceUtil.instance.notificationModel == Notification.FLAG_ONGOING_EVENT
        mAnimationOnOff!!.isChecked = SharedPreferenceUtil.instance.mainAnim
        mChangeIcons!!.summary = resources.getStringArray(R.array.icons)[mSharedPreferenceUtil!!.iconType]

        mChangeUpdate!!.summary = if (mSharedPreferenceUtil!!.autoUpdate == 0) "禁止刷新" else "每" + mSharedPreferenceUtil!!.autoUpdate + "小时更新"
        mClearCache!!.summary = FileSizeUtil.getAutoFileOrFilesSize(C.NET_CACHE)

        mChangeIcons!!.onPreferenceClickListener = this
        mChangeUpdate!!.onPreferenceClickListener = this
        mClearCache!!.onPreferenceClickListener = this
        mNotificationType!!.onPreferenceChangeListener = this
        mAnimationOnOff!!.onPreferenceChangeListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (mChangeIcons === preference) {
            showIconDialog()
        } else if (mClearCache === preference) {
            ImageLoader.clear(activity)
            Observable.just(FileUtil.delete(File(C.NET_CACHE)))
                    .filter { aBoolean -> aBoolean }
                    .compose(RxUtil.io())
                    .doOnNext { aBoolean ->
                        mClearCache!!.summary = FileSizeUtil.getAutoFileOrFilesSize(C.NET_CACHE)
                        Snackbar.make(view!!, "缓存已清除", Snackbar.LENGTH_SHORT).show()
                    }
                    .subscribe()
        } else if (mChangeUpdate === preference) {
            showUpdateDialog()
        }
        return true
    }

    private fun showIconDialog() {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_icon, activity.findViewById<View>(R.id.dialog_root) as ViewGroup)
        val builder = AlertDialog.Builder(activity).setView(dialogLayout)
        val alertDialog = builder.create()

        val layoutTypeOne = dialogLayout.findViewById<View>(R.id.layout_one) as LinearLayout
        layoutTypeOne.isClickable = true
        val radioTypeOne = dialogLayout.findViewById<View>(R.id.radio_one) as RadioButton
        val layoutTypeTwo = dialogLayout.findViewById<View>(R.id.layout_two) as LinearLayout
        layoutTypeTwo.isClickable = true
        val radioTypeTwo = dialogLayout.findViewById<View>(R.id.radio_two) as RadioButton
        val done = dialogLayout.findViewById<View>(R.id.done) as TextView

        radioTypeOne.isClickable = false
        radioTypeTwo.isClickable = false

        alertDialog.show()

        when (mSharedPreferenceUtil!!.iconType) {
            0 -> {
                radioTypeOne.isChecked = true
                radioTypeTwo.isChecked = false
            }
            1 -> {
                radioTypeOne.isChecked = false
                radioTypeTwo.isChecked = true
            }
        }

        layoutTypeOne.setOnClickListener { v ->
            radioTypeOne.isChecked = true
            radioTypeTwo.isChecked = false
        }

        layoutTypeTwo.setOnClickListener { v ->
            radioTypeOne.isChecked = false
            radioTypeTwo.isChecked = true
        }

        done.setOnClickListener { v ->
            mSharedPreferenceUtil!!.iconType = if (radioTypeOne.isChecked) 0 else 1
            val iconsText = resources.getStringArray(R.array.icons)
            mChangeIcons!!.summary = if (radioTypeOne.isChecked)
                iconsText[0]
            else
                iconsText[1]
            alertDialog.dismiss()
            Snackbar.make(view!!, "切换成功,重启应用生效",
                    Snackbar.LENGTH_INDEFINITE).setAction("重启"
            ) { v1 ->
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity.startActivity(intent)
                activity.finish()
                RxBus.default.post(ChangeCityEvent())
            }.show()
        }
    }

    private fun showUpdateDialog() {
        //将 SeekBar 放入 Dialog 的方案 http://stackoverflow.com/questions/7184104/how-do-i-put-a-seek-bar-in-an-alert-dialog
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_update, activity.findViewById<View>(
                R.id.dialog_root) as ViewGroup)
        val builder = AlertDialog.Builder(activity)
                .setView(dialogLayout)
        val alertDialog = builder.create()

        val mSeekBar = dialogLayout.findViewById<View>(R.id.time_seekbar) as SeekBar
        val tvShowHour = dialogLayout.findViewById<View>(R.id.tv_showhour) as TextView
        val tvDone = dialogLayout.findViewById<View>(R.id.done) as TextView

        mSeekBar.max = 24
        mSeekBar.progress = mSharedPreferenceUtil!!.autoUpdate
        tvShowHour.setText(String.format("每%s小时", mSeekBar.progress))
        alertDialog.show()

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tvShowHour.setText(String.format("每%s小时", mSeekBar.progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        tvDone.setOnClickListener { v ->
            mSharedPreferenceUtil!!.autoUpdate = mSeekBar.progress
            mChangeUpdate!!.summary = if (mSharedPreferenceUtil!!.autoUpdate == 0)
                "禁止刷新"
            else
                String.format(Locale.CHINA, "每%d小时更新", mSharedPreferenceUtil!!.autoUpdate)
            //            getActivity().startService(new Intent(getActivity(), AutoUpdateService.class));
            alertDialog.dismiss()
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference === mAnimationOnOff) {
            SharedPreferenceUtil.instance.mainAnim = newValue as Boolean
        } else if (mNotificationType === preference) {
            SharedPreferenceUtil.instance.notificationModel = if (newValue as Boolean) Notification.FLAG_ONGOING_EVENT else Notification.FLAG_AUTO_CANCEL
        }

        return true
    }

    companion object {
        private val TAG = SettingFragment::class.java!!.getSimpleName()
    }
}
