package com.wzf.wucarryme.modules.main.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseViewHolder;
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter;
import com.wzf.wucarryme.component.PLog;
import com.wzf.wucarryme.common.utils.Util;
import com.wzf.wucarryme.modules.main.domain.StockResp;
import java.util.List;

public class MultiStockAdapter extends AnimRecyclerViewAdapter<MultiStockAdapter.MultiStockViewHolder> {
    private Context mContext;
    private List<StockResp.DataBean> mStockList;
    private onMultiCityClick mMultiCityClick;

    public void setMultiCityClick(onMultiCityClick multiCityClick) {
        this.mMultiCityClick = multiCityClick;
    }

    public MultiStockAdapter(List<StockResp.DataBean> stockList) {
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

        @BindView(R.id.dialog_name)
        TextView mDialogName;
        @BindView(R.id.dialog_rise)
        TextView mDialogRise;
        @BindView(R.id.dialog_price)
        TextView mDialogPrice;
        @BindView(R.id.cardView)
        CardView mCardView;

        public MultiStockViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(StockResp.DataBean stock) {

            try {
                mDialogName.setText(Util.safeText(stock.getStockName()));
                mDialogRise.setText(stock.getFormattedRise());
                mDialogPrice.setText(stock.getNewPrice());
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
            ViewStyleHelper.applyColor(stock, mDialogRise);
        }
    }

    public interface onMultiCityClick {
        void longClick(StockResp.DataBean city);

        void click(StockResp.DataBean stock);
    }
}
