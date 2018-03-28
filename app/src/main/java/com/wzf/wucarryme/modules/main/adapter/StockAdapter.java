package com.wzf.wucarryme.modules.main.adapter;

import java.lang.reflect.Method;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseViewHolder;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.common.utils.Util;
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter;
import com.wzf.wucarryme.component.ImageLoader;
import com.wzf.wucarryme.component.PLog;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

public class StockAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder> {
    private static String TAG = StockAdapter.class.getSimpleName();

    private Context mContext;

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FORE = 3;

    private StockResp.DataBean mStock;

    public StockAdapter(StockResp.DataBean stock) {
        this.mStock = stock;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == StockAdapter.TYPE_ONE) {
            return StockAdapter.TYPE_ONE;
        }
        if (position == StockAdapter.TYPE_TWO) {
            return StockAdapter.TYPE_TWO;
        }
        if (position == StockAdapter.TYPE_THREE) {
            return StockAdapter.TYPE_THREE;
        }
        if (position == StockAdapter.TYPE_FORE) {
            return StockAdapter.TYPE_FORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case TYPE_ONE:
                return new NowStockViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_temperature, parent, false));
            case TYPE_TWO:
                return new HoursWeatherViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NowStockViewHolder) holder).bind(mStock);
                break;
            case TYPE_TWO:
                ((HoursWeatherViewHolder) holder).bind(mStock);
                break;
            default:
                break;
        }
        if (SharedPreferenceUtil.getInstance().getMainAnim()) {
            showItemAnim(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    /**
     * 当前情况
     */
    class NowStockViewHolder extends BaseViewHolder<StockResp.DataBean> {

        @BindView(R.id.weather_icon)
        ImageView weatherIcon;
        @BindView(R.id.temp_flu)
        TextView tempFlu;
        @BindView(R.id.temp_max)
        TextView tempMax;
        @BindView(R.id.temp_min)
        TextView tempMin;
        @BindView(R.id.temp_pm)
        TextView tempPm;
        @BindView(R.id.temp_quality)
        TextView tempQuality;

        NowStockViewHolder(View itemView) {
            super(itemView);
        }

        protected void bind(StockResp.DataBean stock) {
            try {
                tempFlu.setText(stock.getFormattedPrice());
                tempMax.setText(String.format("↑ %s", stock.getMaxPrice()));
                tempMin.setText(String.format("↓ %s", stock.getMinPrice()));

                tempPm.setText(String.format("开: %s ", Util.safeText(stock.getOpen())));
                tempQuality.setText(String.format("现：%s", Util.safeText(stock.getNewPrice())));
                ImageLoader.load(itemView.getContext(),
                    SharedPreferenceUtil.getInstance().getInt("stock.now.cond.txt", R.mipmap.none),
                    weatherIcon);
            } catch (Exception e) {
                PLog.e(TAG, e.toString());
            }
        }
    }

    /**
     * 买卖
     */
    private class HoursWeatherViewHolder extends BaseViewHolder<StockResp.DataBean> {
        private LinearLayout itemHourInfoLayout;
        private TextView[] mBuyPrice = new TextView[5];
        private TextView[] mBuyCount = new TextView[5];
        private TextView[] mSellPrice = new TextView[5];
        private TextView[] mSellCount = new TextView[5];

        HoursWeatherViewHolder(View itemView) {
            super(itemView);
            itemHourInfoLayout = itemView.findViewById(R.id.item_hour_info_linearlayout);

            for (int i = 0; i < 5; i++) {
                View view = View.inflate(mContext, R.layout.item_hour_info_line, null);
                mBuyPrice[i] = view.findViewById(R.id.one_clock);
                mBuyCount[i] = view.findViewById(R.id.one_temp);
                mSellPrice[i] = view.findViewById(R.id.one_humidity);
                mSellCount[i] = view.findViewById(R.id.one_wind);
                itemHourInfoLayout.addView(view);
            }
        }

        protected void bind(StockResp.DataBean stock) {

            try {
                for (int i = 0; i < 5; i++) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    Method method = stock.getClass().getMethod("getBuyPrice" + (i + 1));
                    Object invoke = method.invoke(stock);
                    mBuyPrice[i].setText(String.valueOf(invoke));

                    method = stock.getClass().getMethod("getBuyCount" + (i + 1));
                    invoke = method.invoke(stock);
                    mBuyCount[i].setText(String.valueOf(invoke));


                    method = stock.getClass().getMethod("getSellPrice" + (5 - i));
                    invoke = method.invoke(stock);
                    mSellPrice[i].setText(String.valueOf(invoke));


                    method = stock.getClass().getMethod("getSellCount" + (5 - i));
                    invoke = method.invoke(stock);
                    mSellCount[i].setText(String.valueOf(invoke));
                }
            } catch (Exception e) {
                PLog.e(e.toString());
            }
        }
    }


}
