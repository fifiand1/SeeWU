package com.wzf.wucarryme.component

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.wzf.wucarryme.R
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.modules.main.domain.StockResp
import com.wzf.wucarryme.modules.main.ui.MainActivity

object NotificationHelper {
    private var NOTIFICATION_ID = 2333

    fun showCustomNotification(context: Context, bean: StockResp.DataBean) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = Notification.Builder(context)
        val notification = builder.setContentIntent(pendingIntent)
            .setContentTitle("WARNING")
            .setContentText(bean.stockName + "-----" + bean.newPrice)
            .setSmallIcon(SharedPreferenceUtil.instance.getInt("POS", R.mipmap.none))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification.flags = Notification.FLAG_AUTO_CANCEL
        manager.notify(NOTIFICATION_ID + 998, notification)
        LogUtil.wtf("WARNING", bean.toString())
    }
}
