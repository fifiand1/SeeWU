package com.wzf.wucarryme.component

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.wzf.wucarryme.R
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.modules.care.domain.BuySellORM
import com.wzf.wucarryme.modules.main.domain.StockResp
import com.wzf.wucarryme.modules.main.ui.MainActivity

object NotificationHelper {
    private var NOTIFICATION_ID_ALERT = 2333
    private var NOTIFICATION_ID = 233

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
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = Notification.Builder(context)
        val action = buySell.action
        val notification = builder.setContentIntent(pendingIntent)
            .setContentTitle(action)
            .setContentText(buySell.desc)
            .setSmallIcon(SharedPreferenceUtil.instance.getInt(action!!, R.mipmap.none))
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //        if ("POS".equals(action)) {
        //todo 以后可以改成常驻
        //            notification.flags = Notification.FLAG_AUTO_CANCEL;
        //        }else{

        notification.flags = Notification.FLAG_AUTO_CANCEL
        //        }
        manager.notify(NOTIFICATION_ID++, notification)
    }

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
        manager.notify(NOTIFICATION_ID_ALERT++, notification)
        LogUtil.wtf("WARNING", bean.toString())
    }
}
