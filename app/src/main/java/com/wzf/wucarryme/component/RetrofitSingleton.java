package com.wzf.wucarryme.component;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.wzf.wucarryme.BuildConfig;
import com.wzf.wucarryme.base.BaseApplication;
import com.wzf.wucarryme.common.C;
import com.wzf.wucarryme.common.utils.LogUtil;
import com.wzf.wucarryme.common.utils.RxUtil;
import com.wzf.wucarryme.common.utils.ToastUtil;
import com.wzf.wucarryme.common.utils.Util;
import com.wzf.wucarryme.modules.about.domain.Version;
import com.wzf.wucarryme.modules.main.domain.CityORM;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static final String TAG = RetrofitSingleton.class.getSimpleName();
    private static FounderService sApiService = null;
    private static Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttpClient = null;

    private void init() {
        initOkHttp();
        initRetrofit();
        sApiService = sRetrofit.create(FounderService.class);
    }

    private RetrofitSingleton() {
        init();
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
//        File cacheFile = new File(C.NET_CACHE);
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
//        Interceptor cacheInterceptor = chain -> {
//            Request request = chain.request();
//            if (!Util.isNetworkConnected(BaseApplication.getAppContext())) {
//                request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//            }
//            Response response = chain.proceed(request);
//            Response.Builder newBuilder = response.newBuilder();
//            if (Util.isNetworkConnected(BaseApplication.getAppContext())) {
//                int maxAge = 0;
//                // 有网络时 设置缓存超时时间0个小时
//                newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
//            } else {
//                // 无网络时，设置超时为4周
//                int maxStale = 60 * 60 * 24 * 28;
//                newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
//            }
//            return newBuilder.build();
//        };
//        builder.cache(cache).addInterceptor(cacheInterceptor);
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        sOkHttpClient = builder.build();
    }

    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
            .baseUrl(FounderService.HOST)
            .client(sOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }

    private static Consumer<Throwable> disposeFailureInfo(Throwable t) {
        return throwable -> {
            if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
                ToastUtil.showShort("网络问题");
            } else if (t.toString().contains("API没有")) {
                OrmLite.getInstance()
                    .delete(new WhereBuilder(CityORM.class).where("name=?", Util.replaceInfo(t.getMessage())));
                ToastUtil.showShort("错误: " + t.getMessage());
            }
            throwable.printStackTrace();
            PLog.w(t.getMessage());
        };
    }

    public Observable<List<StockResp.DataBean>> fetchStocks() {
        String random = String.valueOf(Math.random());
        String codes = "300443,300628,002460,600518,601668,601166,300725,1A0001,2A01,399006";
        String codeTypes = "4621,4621,4614,4353,4353,4353,4621,4352,4608,4608";
        // TODO: 2018/3/30 timeout异常处理
        return sApiService.listStocks(random, codes, codeTypes)
            .flatMap(resp -> {
                boolean status = resp.isSuccess();
                if (status) {
                    LogUtil.i(TAG, resp.toString());
                    return Observable.just(resp);
                }
                return Observable.error(new RuntimeException("出错了/(ㄒoㄒ)/"));
            })
            .map(StockResp::getData)
            .doOnError(RetrofitSingleton::disposeFailureInfo)
            .compose(RxUtil.io());
    }

    public Observable<Version> fetchVersion() {
        return sApiService.mVersionAPI(C.API_TOKEN)
            .doOnError(RetrofitSingleton::disposeFailureInfo)
            .compose(RxUtil.io());
    }
}
