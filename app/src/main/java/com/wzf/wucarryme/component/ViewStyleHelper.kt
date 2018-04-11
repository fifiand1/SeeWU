package com.wzf.wucarryme.component

import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.TextView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.modules.main.domain.StockResp

/*
* 用于设置view样式
*/
object ViewStyleHelper {

    val SUNNY_CODE = 100
    val RAINY_CODE = 300
    val CLOUDY_CODE = 500

    private val BIG_SWING_VALUE = 3

    //    private static SparseIntArray sMap = new SparseIntArray();
    //
    //    static {
    //        // 其他
    //        sMap.put(SUNNY_CODE, R.mipmap.city_other_sunny);
    //        sMap.put(RAINY_CODE, R.mipmap.city_other_rainy);
    //        sMap.put(CLOUDY_CODE, R.mipmap.city_other_cloudy);
    //    }

    fun applyStatus(stock: StockResp.DataBean, view: View) {
        val rise = java.lang.Float.parseFloat(stock.risePrice)
        val code: Int
        if (rise >= -BIG_SWING_VALUE && rise <= BIG_SWING_VALUE) {
            code = CLOUDY_CODE
        } else if (rise < -BIG_SWING_VALUE) {
            code = RAINY_CODE
        } else {
            code = SUNNY_CODE
        }
        //        Integer mipRes = sMap.get(code);
        //        view.setBackground(ContextCompat.getDrawable(view.getContext(), mipRes));
    }

    fun applyColor(stock: StockResp.DataBean, view: TextView) {
        if (stock.risePrice != null) {
            val rise = java.lang.Float.parseFloat(stock.risePrice)
            if (rise > 0) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.price_rise))
            } else if (rise < 0) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.price_fall))
            }
        }
    }

    fun setSwipeRefreshColor(mRefreshLayout: SwipeRefreshLayout) {
        mRefreshLayout.setColorSchemeResources(
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_green_dark,
            android.R.color.holo_blue_dark
        )
    }
}
