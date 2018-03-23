package com.wzf.wucarryme.component;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.modules.care.domain.BuySellORM;
import com.wzf.wucarryme.modules.main.domain.Weather;
import com.wzf.wucarryme.modules.main.ui.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by HugoXie on 2017/5/29.
 * <p>
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info: 通知栏
 */

public class NotificationHelper {
    private static int NOTIFICATION_ID = 233;

    public static void showWeatherNotification(Context context, @NonNull Weather weather) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setContentIntent(pendingIntent)
            .setContentTitle(weather.basic.city)
            .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
            .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none))
            .build();
        notification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    public static void showPositioningNotification(Context context, @NonNull BuySellORM buySell) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        String action = buySell.getAction();
        Notification notification = builder.setContentIntent(pendingIntent)
            .setContentTitle(action)
            .setContentText(buySell.getDesc())
            .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(action, R.mipmap.none))
            .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if ("POS".equals(action)) {
//todo 以后可以改成常驻
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//        }else{

            notification.flags = Notification.FLAG_AUTO_CANCEL;
//        }
        manager.notify(NOTIFICATION_ID++, notification);
    }
}
