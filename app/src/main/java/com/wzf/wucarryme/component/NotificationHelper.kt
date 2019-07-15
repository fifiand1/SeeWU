package com.wzf.wucarryme.component

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES.O
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.wzf.wucarryme.R
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.modules.care.domain.BuySellORM
import com.wzf.wucarryme.modules.main.domain.StockResp
import com.wzf.wucarryme.modules.main.ui.MainActivity

object NotificationHelper {
    private var NOTIFICATION_ID_ALERT = 2333
    private var NOTIFICATION_ID = 233

    private val DO_NOTIFY = true

    //    public static void showWeatherNotification(Context context, @NonNull DataBean weather) {
    //        Intent intent = new Intent(context, MainActivity.class);
    //        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    //        PendingIntent pendingIntent =
    //            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //        Notification.Builder builder = new Notification.Builder(context);
    //        Notification notification = builder.setContentIntent(pendingIntent)
    //            .setContentTitle(weather.basic.city)
    //            .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
    //            .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none))
    //            .build();
    //        notification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
    //        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    //        manager.notify(NOTIFICATION_ID, notification);
    //    }

    fun showPositioningNotification(context: Context, buySell: BuySellORM) {
        if (!DO_NOTIFY) return
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val builder = Notification.Builder(context)
//        val action = buySell.action
//        val notification = builder.setContentIntent(pendingIntent)
//            .setContentTitle(action)
//            .setContentText(buySell.desc)
//            .setSmallIcon(SharedPreferenceUtil.instance.getInt(action!!, R.mipmap.none))
//            .build()
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        //        if ("POS".equals(action)) {
//        //todo 以后可以改成常驻
//        //            notification.flags = Notification.FLAG_AUTO_CANCEL;
//        //        }else{
//
//        notification.flags = Notification.FLAG_AUTO_CANCEL
//        //        }

        val notificationCompatBuilder = NotificationCompat.Builder(context, createNotificationChannel(context))
        val notification = notificationCompatBuilder
            .setContentTitle(buySell.action)
            .setContentText(buySell.desc)
            .setSmallIcon(R.mipmap.none)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.none))
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setCategory(Notification.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID++, notification)

//        val notificationId = NOTIFICATION_ID++
//        val clickIntent = Intent(context, MainActivity::class.java)
//        val mBuilder = NotificationCompat.Builder(context, notificationId.toString())
//        mBuilder.setContentTitle(buySell.action)
//            .setContentText(buySell.desc)
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.none))
//            .setOnlyAlertOnce(true)
//            .setDefaults(Notification.DEFAULT_ALL)
//            .setWhen(System.currentTimeMillis())
//            .setSmallIcon(R.mipmap.none).color = ContextCompat.getColor(context, R.color.sunny)
//        clickIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        val clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//        // 点击删除
////        val cancelIntent = Intent(context, NoticeCancelBroadcastReceiver::class.java)
////        cancelIntent.action = "notice_cancel"
////        cancelIntent.putExtra("id", notificationId)
////        val cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_ONE_SHOT)
//        mBuilder.setContentIntent(clickPendingIntent)
////        mBuilder.setDeleteIntent(cancelPendingIntent)
//        mBuilder.setAutoCancel(true)
//        manager.notify(notificationId, mBuilder.build())
    }

    fun showCustomNotification(context: Context, bean: StockResp.DataBean) {
        if (!DO_NOTIFY) return
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
        manager.notify(NOTIFICATION_ID_ALERT++, notification)
        LogUtil.wtf("WARNING", bean.toString())
    }

    private fun createNotificationChannel(context: Context): String {

        if (VERSION.SDK_INT >= O) {
            val channelId = "wucarryme"
            val channelName = "wucarryme"
            val channelDescription = "wucarryme important"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            // 设置描述 最长30字符
            notificationChannel.description = channelDescription
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true)
            // 设置显示模式
            notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            return channelId
        } else {
            return ""
        }
    }
}
