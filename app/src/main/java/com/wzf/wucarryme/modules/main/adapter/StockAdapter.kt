package com.wzf.wucarryme.modules.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseViewHolder
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.common.utils.Util
import com.wzf.wucarryme.component.AnimRecyclerViewAdapter
import com.wzf.wucarryme.component.ImageLoader
import com.wzf.wucarryme.component.PLog
import com.wzf.wucarryme.modules.main.domain.StockResp

class StockAdapter(private val mStock: StockResp.DataBean) : AnimRecyclerViewAdapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null

    override fun getItemViewType(position: Int): Int {
        if (position == StockAdapter.TYPE_ONE) {
            return StockAdapter.TYPE_ONE
        }
        if (position == StockAdapter.TYPE_TWO) {
            return StockAdapter.TYPE_TWO
        }
        if (position == StockAdapter.TYPE_THREE) {
            return StockAdapter.TYPE_THREE
        }
        return if (position == StockAdapter.TYPE_FORE) {
            StockAdapter.TYPE_FORE
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return when (viewType) {
            TYPE_ONE -> NowStockViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_stock_detail, parent, false))
        //            TYPE_TWO -> return HoursWeatherViewHolder(
        //                    LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false))
            else     -> HoursWeatherViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_stock_five_gear, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        when (itemType) {
            TYPE_ONE -> (holder as NowStockViewHolder).bind(mStock)
            TYPE_TWO -> (holder as HoursWeatherViewHolder).bind(mStock)
            else     -> {
            }
        }
        if (SharedPreferenceUtil.instance.mainAnim) {
            showItemAnim(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    /**
     * 当前情况
     */
    internal inner class NowStockViewHolder(itemView: View) : BaseViewHolder<StockResp.DataBean>(itemView) {

        //        @BindView(R.id.weather_icon)
        lateinit var weatherIcon: ImageView
        //        @BindView(R.id.stock_flu)
        lateinit var stockFlu: TextView
        @BindView(R.id.stock_max)
        lateinit var stockMax: TextView

        @BindView(R.id.stock_min)
        lateinit var stockMin: TextView

        //        @BindView(R.id.stock_pm)
        lateinit var stockPm: TextView
        @BindView(R.id.stock_now)
        lateinit var stockNewPrice: TextView

        public override fun bind(dataBean: StockResp.DataBean) = try {
            stockFlu.text = dataBean.formattedRise
            stockMax.text = String.format("↑ %s", dataBean.maxPrice)
            stockMin.text = String.format("↓ %s", dataBean.minPrice)

            stockPm.text = String.format("开: %s ", Util.safeText(dataBean.open))
            stockNewPrice.text = String.format("现：%s", Util.safeText(dataBean.newPrice))
            ImageLoader.load(itemView.context,
                SharedPreferenceUtil.instance.getInt("stock.now.cond.txt", R.mipmap.none),
                weatherIcon)
        } catch (e: Exception) {
            PLog.e(TAG, e.toString())
        }
    }

    /**
     * 买卖
     */
    private inner class HoursWeatherViewHolder internal constructor(itemView: View) :
        BaseViewHolder<StockResp.DataBean>(itemView) {


        private val itemHourInfoLayout: LinearLayout = itemView.findViewById(R.id.item_stock_five_range_linear_layout)
        private val mBuyPrice = arrayOfNulls<TextView>(5)
        private val mBuyCount = arrayOfNulls<TextView>(5)
        private val mSellPrice = arrayOfNulls<TextView>(5)
        private val mSellCount = arrayOfNulls<TextView>(5)

        init {

            for (i in 0..4) {
                val view = View.inflate(mContext, R.layout.item_stock_five_gear_line, null)
//                mBuyPrice[i] = view.findViewById(R.id.one_clock)
//                mBuyCount[i] = view.findViewById(R.id.one_temp)
//                mSellPrice[i] = view.findViewById(R.id.one_humidity)
//                mSellCount[i] = view.findViewById(R.id.one_wind)
                itemHourInfoLayout.addView(view)
            }
        }

        public override fun bind(dataBean: StockResp.DataBean) {

            try {
                LogUtil.i(TAG, "HoursWeatherViewHolder bind: $dataBean")
                for (i in 0..4) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    var method = dataBean.javaClass.getMethod("getBuyPrice" + (i + 1))
                    var invoke = method.invoke(dataBean)
                    mBuyPrice[i]!!.text = invoke!!.toString()

                    method = dataBean.javaClass.getMethod("getBuyCount" + (i + 1))
                    invoke = method.invoke(dataBean)
                    mBuyCount[i]!!.text = invoke!!.toString()


                    method = dataBean.javaClass.getMethod("getSellPrice" + (5 - i))
                    invoke = method.invoke(dataBean)
                    mSellPrice[i]!!.text = invoke!!.toString()


                    method = dataBean.javaClass.getMethod("getSellCount" + (5 - i))
                    invoke = method.invoke(dataBean)
                    mSellCount[i]!!.text = invoke!!.toString()
                }
            } catch (e: Exception) {
                PLog.e(e.toString())
            }

        }
    }

    companion object {
        private val TAG = StockAdapter::class.java.simpleName

        private const val TYPE_ONE = 0
        private const val TYPE_TWO = 1
        private const val TYPE_THREE = 2
        private const val TYPE_FORE = 3
    }


}
