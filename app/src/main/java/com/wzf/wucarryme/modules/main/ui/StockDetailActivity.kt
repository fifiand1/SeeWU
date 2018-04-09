package com.wzf.wucarryme.modules.main.ui

import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity
import com.wzf.wucarryme.common.IntentKey
import com.wzf.wucarryme.modules.main.adapter.StockAdapter
import com.wzf.wucarryme.modules.main.domain.StockResp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import butterknife.BindView

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
        safeSetTitle(stock!!.stockName!!)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        val mAdapter = StockAdapter(stock)
        mRecyclerView!!.adapter = mAdapter
    }

    companion object {

        fun launch(context: Context, stock: StockResp.DataBean) {
            val intent = Intent(context, StockDetailActivity::class.java)
            intent.putExtra(IntentKey.STOCK, stock)
            context.startActivity(intent)
        }
    }
}
