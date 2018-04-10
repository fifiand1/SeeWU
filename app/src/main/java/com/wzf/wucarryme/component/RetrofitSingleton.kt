package com.wzf.wucarryme.component

import com.litesuits.orm.db.assit.WhereBuilder
import com.wzf.wucarryme.common.C
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.common.utils.ToastUtil
import com.wzf.wucarryme.common.utils.Util
import com.wzf.wucarryme.modules.about.domain.Version
import com.wzf.wucarryme.modules.main.domain.CityORM
import com.wzf.wucarryme.modules.main.domain.StockResp
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class RetrofitSingleton private constructor() {

    private fun init() {
        initOkHttp()
        initRetrofit()
        sApiService = sRetrofit.create(FounderService::class.java)
    }

    init {
        init()
    }

    private object SingletonHolder {
        internal val INSTANCE = RetrofitSingleton()
    }

    fun fetchStocks(): Observable<List<StockResp.DataBean>> {
        val random = Math.random().toString()
        val codes = "300443,300628,002460,600518,601668,601166,300583,300725,1A0001,2A01,399006"
        val codeTypes = "4621,4621,4614,4353,4353,4353,4621,4621,4352,4608,4608"
        return try {
            sApiService.listStocks(random, codes, codeTypes)
                .map { stockResp ->
                    LogUtil.i(TAG, stockResp.toString())
                    stockResp.data
                }
                .doOnError { t -> disposeFailureInfo(t) }
                .compose(RxUtil.io())
        } catch (e: Exception) {
            e.printStackTrace()
            Observable.error(e)
        }

    }

    fun fetchVersion(): Observable<Version> {
        return sApiService.mVersionAPI(C.API_TOKEN)
            .doOnError { disposeFailureInfo(it) }
            .compose(RxUtil.io())
    }

    companion object {

        private val TAG = RetrofitSingleton::class.java.simpleName
        private lateinit var sApiService: FounderService
        private lateinit var sRetrofit: Retrofit
        private lateinit var sOkHttpClient: OkHttpClient
        private var totalReconnect: Int = 0

        val instance: RetrofitSingleton
            get() = SingletonHolder.INSTANCE

        private fun initOkHttp() {
            val builder = OkHttpClient.Builder()
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
            //        if (BuildConfig.DEBUG) {
            //            builder.addNetworkInterceptor(new StethoInterceptor());
            //        }
            builder.addInterceptor { chain ->
                val request = chain.request()
                var proceed: Response? = null
                while (proceed == null) {
                    try {
                        proceed = chain.proceed(request)
                        val content = proceed.body()!!.string()
                        val mediaType = proceed.body()!!.contentType()
                        LogUtil.d(TAG, "okhttp body: $content")
                        return@addInterceptor proceed.newBuilder()
                            .body(ResponseBody.create(mediaType, content))
                            .build()
                    } catch (e: SocketTimeoutException) {
                        LogUtil.d(TAG, "okhttp [" + e.message + "], rej8try..." + (++totalReconnect))
                    }

                }
                proceed
            }
            //设置超时
            builder.connectTimeout(5, TimeUnit.SECONDS)
            builder.readTimeout(5, TimeUnit.SECONDS)
            builder.writeTimeout(5, TimeUnit.SECONDS)
            //错误重连
            // builder.retryOnConnectionFailure(true);
            sOkHttpClient = builder.build()
        }

        private fun initRetrofit() {
            sRetrofit = Retrofit.Builder()
                .baseUrl(FounderService.HOST)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        private fun disposeFailureInfo(t: Throwable): Consumer<Throwable> {
            return Consumer { throwable ->
                if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                    t.toString().contains("UnknownHostException")) {
                    ToastUtil.showShort("网络问题")
                } else if (t.toString().contains("API没有")) {
                    OrmLite.getInstance()!!.delete(WhereBuilder(CityORM::class.java).where("name=?",
                        Util.replaceInfo(t.message)))
                    ToastUtil.showShort("错误: " + t.message)
                }
                throwable.printStackTrace()
                PLog.w(t.message!!)
            }
        }
    }
}
