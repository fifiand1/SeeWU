package com.wzf.wucarryme.modules.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity
import com.wzf.wucarryme.common.IntentKey
import com.wzf.wucarryme.modules.main.adapter.StockAdapter
import com.wzf.wucarryme.modules.main.domain.StockResp

class StockDetailActivity : ToolbarActivity() {

    @BindView(R.id.recyclerview)
    lateinit var mRecyclerView: RecyclerView

    override fun layoutId(): Int {
        return R.layout.activity_detail
    }

    override fun canBack(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewWithData()
    }

    private fun initViewWithData() {
        val intent = intent
        val stock = intent.getParcelableExtra<StockResp.DataBean>(IntentKey.STOCK)
        if (stock == null) {
            finish()
        }
        safeSetTitle(stock.stockName!!)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        val mAdapter = StockAdapter(stock)
        mRecyclerView.adapter = mAdapter
    }

    companion object {

        fun launch(context: Activity, stock: StockResp.DataBean, sharedElement: View, sharedElementName: String) {
            val intent = Intent(context, StockDetailActivity::class.java)
            intent.putExtra(IntentKey.STOCK, stock)
//            context.startActivity(intent)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, sharedElement,
                sharedElementName)
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }
}
