package com.wzf.wucarryme.component;

import com.wzf.wucarryme.modules.about.domain.Version;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FounderService {

    String HOST = "https://h5hq.foundersc.com/";

    @GET("market_webapp/quote/getRealTimeDatas")
    Observable<StockResp> listStocks(@Query("random") String random, @Query("codes") String codes, @Query("codeTypes")
        String codeTypes);

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    @GET("http://api.fir.im/apps/latest/xxxxxx")
    Observable<Version> mVersionAPI(@Query("api_token") String api_token);
}
