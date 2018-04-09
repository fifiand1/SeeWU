package com.wzf.wucarryme.modules.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.component.NotificationHelper
import com.wzf.wucarryme.component.RetrofitSingleton
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class AutoUpdateService : Service() {

    private val TAG = AutoUpdateService::class.java!!.getSimpleName()
    private var mDisposable: Disposable? = null
    private var mIsUnSubscribed = true

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        synchronized(this) {
            unSubscribed()
            if (mIsUnSubscribed) {
                unSubscribed()
                if (SharedPreferenceUtil.instance.autoUpdate != 0) {
                    mDisposable = Observable.interval(SharedPreferenceUtil.instance.autoUpdate.toLong(), TimeUnit.HOURS)
                            .doOnNext { aLong ->
                                mIsUnSubscribed = false
                                fetchDataByNetWork()
                            }
                            .subscribe()
                }
            }
        }
        return Service.START_REDELIVER_INTENT
    }

    private fun unSubscribed() {
        mIsUnSubscribed = true
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    override fun stopService(name: Intent): Boolean {
        return super.stopService(name)
    }

    private fun fetchDataByNetWork() {
        //        String cityName = SharedPreferenceUtil.getInstance().getCityName();
        //        RetrofitSingleton.getInstance()
        //            .fetchStocks()
        //            .subscribe(weather -> NotificationHelper.showWeatherNotification(AutoUpdateService.this, weather));
    }
}
