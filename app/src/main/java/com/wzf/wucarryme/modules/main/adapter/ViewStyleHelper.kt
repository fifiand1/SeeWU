package com.wzf.wucarryme.modules.main.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.modules.main.domain.StockResp

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

    internal fun applyStatus(stock: StockResp.DataBean, view: View) {
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

    internal fun applyColor(stock: StockResp.DataBean?, view: TextView) {
        val rise = java.lang.Float.parseFloat(stock!!.risePrice!!)
        if (rise > 0) {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.price_rise))
        } else if (rise < 0) {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.price_fall))
        }
    }

}
