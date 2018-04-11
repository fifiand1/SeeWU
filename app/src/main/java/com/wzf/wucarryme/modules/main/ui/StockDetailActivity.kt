package com.wzf.wucarryme.modules.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity
import com.wzf.wucarryme.common.IntentKey
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.Util
import com.wzf.wucarryme.component.PLog
import com.wzf.wucarryme.component.RxBus
import com.wzf.wucarryme.component.ViewStyleHelper
import com.wzf.wucarryme.modules.main.domain.StockResp
import com.wzf.wucarryme.modules.main.domain.StockUpdateEvent
import com.wzf.wucarryme.modules.main.domain.StockUpdateFinishEvent
import io.reactivex.android.schedulers.AndroidSchedulers

class StockDetailActivity : ToolbarActivity() {

    @BindView(R.id.swipeRefresh)
    lateinit var mRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.stock_previous_close)
    lateinit var stockPreviousClose: TextView
    @BindView(R.id.stock_rise)
    lateinit var stockRise: TextView
    @BindView(R.id.stock_max)
    lateinit var stockMax: TextView
    @BindView(R.id.stock_min)
    lateinit var stockMin: TextView
    @BindView(R.id.stock_open)
    lateinit var stockOpen: TextView
    @BindView(R.id.stock_now)
    lateinit var stockNewPrice: TextView
    @BindView(R.id.item_stock_five_range_linear_layout)
    lateinit var itemHourInfoLayout: LinearLayout
    private val mTradePrice = arrayOfNulls<TextView>(10)
    private val mTradeCount = arrayOfNulls<TextView>(10)

    private lateinit var stock: StockResp.DataBean

    override fun layoutId(): Int {
        return R.layout.activity_detail
    }

    override fun canBack(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)

        for (i in 0..9) {
            val view = View.inflate(this, R.layout.item_stock_five_gear_line, null)
            mTradePrice[i] = view.findViewById(R.id.one_price)
            mTradeCount[i] = view.findViewById(R.id.one_hand)
            itemHourInfoLayout.addView(view)
        }

        stock = intent.getParcelableExtra(IntentKey.STOCK)

        ViewStyleHelper.setSwipeRefreshColor(mRefreshLayout)
        mRefreshLayout.setOnRefreshListener({
            RxBus.default.post(StockUpdateEvent(stock))
            mRefreshLayout.isRefreshing = false
        })

        RxBus.default
            .toObservable(StockUpdateFinishEvent::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext({
                initViewWithData(it.stock)
            })
            .subscribe()

        RxBus.default.post(StockUpdateFinishEvent(stock))
    }

    private fun initViewWithData(stock: StockResp.DataBean) {
        safeSetTitle(stock.stockName!!)

        try {
            stockRise.text = stock.formattedRise
            stockMax.text = String.format("↑ %s", stock.maxPrice)
            stockMin.text = String.format("↓ %s", stock.minPrice)
            stockOpen.text = String.format("开: %s ", Util.safeText(stock.open))
            stockNewPrice.text = stock.newPrice
            stockPreviousClose.text = stock.prevClose

            ViewStyleHelper.applyColor(stock, stockNewPrice)
            ViewStyleHelper.applyColor(stock, stockRise)
//            ImageLoader.load(this,
//                SharedPreferenceUtil.instance.getInt("stock.now.cond.txt", R.mipmap.none),
//                weatherIcon)
        } catch (e: Exception) {
            PLog.e(TAG, e.toString())
        }

        try {
            LogUtil.i(TAG, "HoursWeatherViewHolder bind: $stock")

            for (i in 0..4) {
                var method = stock.javaClass.getMethod("getSellPrice" + (5 - i))
                var invoke = method.invoke(stock)
                mTradePrice[i]!!.text = invoke!!.toString()

                method = stock.javaClass.getMethod("getSellCount" + (5 - i))
                invoke = method.invoke(stock)
                mTradeCount[i]!!.text = invoke!!.toString()
            }
            for (i in 5..9) {

                var method = stock.javaClass.getMethod("getBuyPrice" + (i - 4))
                var invoke = method.invoke(stock)
                mTradePrice[i]!!.text = invoke!!.toString()

                method = stock.javaClass.getMethod("getBuyCount" + (i - 4))
                invoke = method.invoke(stock)
                mTradeCount[i]!!.text = invoke!!.toString()
            }
        } catch (e: Exception) {
            PLog.e(e.toString())
        }

        mRefreshLayout.isRefreshing = false
    }

    companion object {
        val TAG = StockDetailActivity::class.java.simpleName!!

        fun launch(context: Activity, stock: StockResp.DataBean, vararg sharedElements: Pair<View, String>) {
            val intent = Intent(context, StockDetailActivity::class.java)
            intent.putExtra(IntentKey.STOCK, stock)
//            context.startActivity(intent)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, *sharedElements)
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }
}
