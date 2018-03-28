package com.wzf.wucarryme.base;

import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected abstract void bind(StockResp.DataBean weather);
}
