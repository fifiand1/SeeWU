package com.wzf.wucarryme.modules.launch;

import java.util.concurrent.TimeUnit;

import com.wzf.wucarryme.common.utils.LogUtil;
import com.wzf.wucarryme.component.OrmLite;
import com.wzf.wucarryme.modules.main.ui.MainActivity;

import android.app.Activity;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class FirstActivity extends Activity {
    private static final String TAG = FirstActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);

        Observable<Boolean> dbInit = Observable.create(
            (ObservableOnSubscribe<Boolean>) e -> {
                long start = System.currentTimeMillis();
                OrmLite.checkDB();
                LogUtil.d(TAG, "check DB cost: " + (System.currentTimeMillis() - start));
                e.onNext(true);
            }).subscribeOn(Schedulers.io());

        Observable<Long> wait = Observable.timer(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io());

        Observable.zip(dbInit, wait,
            (BiFunction<Boolean, Long, Object>) (aBoolean, aLong) -> aLong)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(o -> {
                MainActivity.launch(this);
                finish();
            });

    }
}