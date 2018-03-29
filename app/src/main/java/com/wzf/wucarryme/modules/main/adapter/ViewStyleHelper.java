package com.wzf.wucarryme.modules.main.adapter;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

public class ViewStyleHelper {

    public static final int SUNNY_CODE = 100;
    public static final int RAINY_CODE = 300;
    public static final int CLOUDY_CODE = 500;

    private static final int BIG_SWING_VALUE = 3;

//    private static SparseIntArray sMap = new SparseIntArray();
//
//    static {
//        // 其他
//        sMap.put(SUNNY_CODE, R.mipmap.city_other_sunny);
//        sMap.put(RAINY_CODE, R.mipmap.city_other_rainy);
//        sMap.put(CLOUDY_CODE, R.mipmap.city_other_cloudy);
//    }

    static void applyStatus(StockResp.DataBean stock, View view) {
        float rise = Float.parseFloat(stock.getRisePrice());
        int code;
        if (rise >= -BIG_SWING_VALUE && rise <= BIG_SWING_VALUE) {
            code = CLOUDY_CODE;
        } else if (rise < -BIG_SWING_VALUE) {
            code = RAINY_CODE;
        } else {
            code = SUNNY_CODE;
        }
//        Integer mipRes = sMap.get(code);
//        view.setBackground(ContextCompat.getDrawable(view.getContext(), mipRes));
    }

    static void applyColor(StockResp.DataBean stock, TextView view) {
        float rise = Float.parseFloat(stock.getRisePrice());
        if (rise > 0) {
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.price_rise));
        } else if (rise < 0) {
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.price_fall));
        }
    }

}
