package com.wzf.wucarryme.base

import com.wzf.wucarryme.modules.main.domain.StockResp

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        ButterKnife.bind(this, itemView)
    }

    protected abstract fun bind(dataBean: StockResp.DataBean)
}
