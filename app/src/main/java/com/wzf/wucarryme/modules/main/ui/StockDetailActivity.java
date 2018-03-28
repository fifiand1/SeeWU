package com.wzf.wucarryme.modules.main.ui;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.ToolbarActivity;
import com.wzf.wucarryme.common.IntentKey;
import com.wzf.wucarryme.modules.main.adapter.StockAdapter;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

public class StockDetailActivity extends ToolbarActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected int layoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewWithData();
    }

    private void initViewWithData() {
        Intent intent = getIntent();
        StockResp.DataBean stock =  intent.getParcelableExtra(IntentKey.STOCK);
        if (stock == null) {
            finish();
        }
        safeSetTitle(stock.getStockName());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        StockAdapter mAdapter = new StockAdapter(stock);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void launch(Context context, StockResp.DataBean stock) {
        Intent intent = new Intent(context, StockDetailActivity.class);
        intent.putExtra(IntentKey.STOCK, stock);
        context.startActivity(intent);
    }
}
