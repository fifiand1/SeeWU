package com.wzf.wucarryme.modules.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseViewHolder;
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter;
import com.wzf.wucarryme.component.PLog;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.common.utils.Util;
import com.wzf.wucarryme.modules.main.domain.StockResp;
import java.util.List;

public class MultiCityAdapter extends AnimRecyclerViewAdapter<MultiCityAdapter.MultiStockViewHolder> {
    private Context mContext;
    private List<StockResp.DataBean> mStockList;
    private onMultiCityClick mMultiCityClick;

    public void setMultiCityClick(onMultiCityClick multiCityClick) {
        this.mMultiCityClick = multiCityClick;
    }

    public MultiCityAdapter(List<StockResp.DataBean> stockList) {
        mStockList = stockList;
    }

    @Override
    public MultiStockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MultiStockViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_stock, parent, false));
    }

    @Override
    public void onBindViewHolder(MultiStockViewHolder holder, int position) {

        holder.bind(mStockList.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            mMultiCityClick.longClick(mStockList.get(holder.getAdapterPosition()));
            return true;
        });
        holder.itemView.setOnClickListener(v -> mMultiCityClick.click(mStockList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

    public boolean isEmpty() {
        return 0 == getItemCount();
    }

    class MultiStockViewHolder extends BaseViewHolder<StockResp.DataBean> {

        @BindView(R.id.dialog_city)
        TextView mDialogCity;
        @BindView(R.id.dialog_temp)
        TextView mDialogTemp;
        @BindView(R.id.cardView)
        CardView mCardView;

        public MultiStockViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(StockResp.DataBean stock) {

            try {
                mDialogCity.setText(Util.safeText(stock.getStockName()));
                mDialogTemp.setText(stock.getFormattedPrice());
            } catch (NullPointerException e) {
                PLog.e(e.getMessage());
            }

//            Glide.with(mContext)
//                .load(SharedPreferenceUtil.getInstance().getInt("none", R.mipmap.none))
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        mDialogIcon.setImageBitmap(resource);
//                        mDialogIcon.setColorFilter(Color.WHITE);
//                    }
//                });

//            ViewStyleHelper.applyStatus(stock, mCardView);
            ViewStyleHelper.applyColor(stock, mDialogTemp);
        }
    }

    public interface onMultiCityClick {
        void longClick(StockResp.DataBean city);

        void click(StockResp.DataBean stock);
    }
}
