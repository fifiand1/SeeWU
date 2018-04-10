package com.wzf.wucarryme.component

import com.wzf.wucarryme.modules.about.domain.Version
import com.wzf.wucarryme.modules.main.domain.StockResp

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FounderService {

    @GET("market_webapp/quote/getRealTimeDatas")
    fun listStocks(
        @Query("random") random: String,
        @Query("codes") codes: String,
        @Query("codeTypes")
        codeTypes: String): Observable<StockResp>

    //按简拼或代码查询接口
    //https://h5hq.foundersc.com/market_webapp/quote/wizard?code=

    //日数据
    //https://h5hq.foundersc.com/market_webapp/quote/getDayDataBigInt?stockCode=399006&codeType=4608&period=day&day=1290&type=hs

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/xxxxxx")
    fun mVersionAPI(@Query("api_token") api_token: String): Observable<Version>

    companion object {
        const val HOST = "https://h5hq.foundersc.com/"
    }
}
