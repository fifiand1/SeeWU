package com.wzf.wucarryme.modules.launch

import android.app.Activity
import android.os.Bundle
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.component.OrmLite
import com.wzf.wucarryme.modules.main.ui.MainActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FirstActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onCreate(savedInstanceState)

        val dbInit = Observable.create(
                ObservableOnSubscribe<Boolean> { e ->
                    val start = System.currentTimeMillis()
                    OrmLite.checkDB()
                    LogUtil.d(TAG, "check DB cost: " + (System.currentTimeMillis() - start))
                    e.onNext(true)
                }).subscribeOn(Schedulers.io())

        val wait = Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())

        Observable.zip(dbInit, wait,
                BiFunction<Boolean, Long, Any> { _, aLong -> aLong })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    MainActivity.launch(this@FirstActivity)
                    this@FirstActivity.finish()
                }

    }

    companion object {
        private val TAG = FirstActivity::class.java.simpleName
    }
}