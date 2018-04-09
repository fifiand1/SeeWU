package com.wzf.wucarryme.modules.city.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseViewHolder
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter
import com.wzf.wucarryme.modules.main.domain.StockResp

import java.util.ArrayList

class CityAdapter(private val mContext: Context, private val mDataList: ArrayList<StockResp.DataBean>) : AnimRecyclerViewAdapter<CityAdapter.CityViewHolder>() {
    private var mOnItemClickListener: OnRecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stock, parent, false))
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {

        holder.bind(mDataList[position])
        holder.mCardView!!.setOnClickListener { v -> mOnItemClickListener!!.onItemClick(v, position) }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.mOnItemClickListener = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View, pos: Int)
    }

    inner class CityViewHolder(itemView: View) : BaseViewHolder<StockResp.DataBean>(itemView) {

        @BindView(R.id.item_city)
        lateinit var mItemCity: TextView
        @BindView(R.id.cardView)
        lateinit var mCardView: CardView

        public override fun bind(dataBean: StockResp.DataBean) {
            mItemCity.text = dataBean.toString()
        }
    }
}
