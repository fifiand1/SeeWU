package com.wzf.wucarryme.modules.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseViewHolder
import com.wzf.wucarryme.common.utils.Util
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter
import com.wzf.wucarryme.component.PLog
import com.wzf.wucarryme.modules.main.domain.StockResp

internal class MultiStockAdapter(private val mStockList: List<StockResp.DataBean>) : AnimRecyclerViewAdapter<MultiStockAdapter.MultiStockViewHolder>() {
    private var mContext: Context? = null
    private var mMultiCityClick: OnMultiStockClick? = null

    val isEmpty: Boolean
        get() = 0 == itemCount

    fun setMultiCityClick(multiCityClick: OnMultiStockClick) {
        this.mMultiCityClick = multiCityClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiStockViewHolder {
        mContext = parent.context
        return MultiStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_multi_stock,
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MultiStockViewHolder, position: Int) {

        holder.bind(mStockList[position])
        holder.itemView.setOnLongClickListener {
            mMultiCityClick!!.longClick(mStockList[holder.adapterPosition])
            true
        }
        holder.itemView.setOnClickListener {
            mMultiCityClick!!.click(mStockList[holder.adapterPosition],
                holder.mDialogRise)
        }
    }

    override fun getItemCount(): Int {
        return mStockList.size
    }

    internal inner class MultiStockViewHolder(itemView: View) : BaseViewHolder<StockResp.DataBean>(itemView) {

        @BindView(R.id.dialog_name)
        lateinit var mDialogName: TextView
        @BindView(R.id.dialog_rise)
        lateinit var mDialogRise: TextView
        @BindView(R.id.dialog_price)
        lateinit var mDialogPrice: TextView
//        @BindView(R.id.cardView)
//        lateinit var mCardView: CardView

        public override fun bind(dataBean: StockResp.DataBean) {

            try {
                mDialogName.text = Util.safeText(dataBean.stockName)
                mDialogRise.text = dataBean.formattedRise
                mDialogPrice.text = dataBean.newPrice
            } catch (e: NullPointerException) {
                PLog.e(e.toString())
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
            ViewStyleHelper.applyColor(dataBean, this.mDialogRise)
        }
    }

    interface OnMultiStockClick {
        fun longClick(dataBean: StockResp.DataBean)

        fun click(dataBean: StockResp.DataBean, clicked: View)
    }
}
