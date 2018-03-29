package com.wzf.wucarryme.modules.city.adapter;

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
import com.wzf.wucarryme.modules.main.domain.StockResp;

import java.util.ArrayList;

public class CityAdapter extends AnimRecyclerViewAdapter<CityAdapter.CityViewHolder> {

    private Context mContext;
    private ArrayList<StockResp.DataBean> mDataList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public CityAdapter(Context context, ArrayList<StockResp.DataBean> dataList) {
        mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stock, parent, false));
    }

    @Override
    public void onBindViewHolder(final CityViewHolder holder, final int position) {

        holder.bind(mDataList.get(position));
        holder.mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }

    class CityViewHolder extends BaseViewHolder<StockResp.DataBean> {

        @BindView(R.id.item_city)
        TextView mItemCity;
        @BindView(R.id.cardView)
        CardView mCardView;

        public CityViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(StockResp.DataBean s) {
            mItemCity.setText(s.toString());
        }
    }
}
