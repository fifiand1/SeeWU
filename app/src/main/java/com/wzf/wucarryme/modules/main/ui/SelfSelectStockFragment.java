package com.wzf.wucarryme.modules.main.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseFragment;
import com.wzf.wucarryme.common.utils.LogUtil;
import com.wzf.wucarryme.common.utils.RxUtil;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.common.utils.TimeUtil;
import com.wzf.wucarryme.component.RetrofitSingleton;
import com.wzf.wucarryme.modules.main.adapter.MultiStockAdapter;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class SelfSelectStockFragment extends BaseFragment {

    private static final String TAG = SelfSelectStockFragment.class.getSimpleName();
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty)
    LinearLayout mLayout;

    private MultiStockAdapter mAdapter;
    private List<StockResp.DataBean> mStocks;
    private Disposable disposable;

    private View view;

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
        savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_self_select_stocks, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2018/3/29 leaks here
//        RxBus.getDefault()
//            .toObservable(SelfSelectUpdateEvent.class)
//            .doOnNext(event -> multiLoad())
//            .subscribe();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        multiLoad();
    }

    private void initView() {
        mStocks = new ArrayList<>();
        mAdapter = new MultiStockAdapter(mStocks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMultiCityClick(new MultiStockAdapter.onMultiCityClick() {
            @Override
            public void longClick(StockResp.DataBean stock) {
                new AlertDialog.Builder(getActivity())
                    .setMessage("是否删除该自选股?")
                    .setPositiveButton("删除", (dialog, which) -> {
                        // TODO: 2018/3/28 自选保存到数据库或sp
//                        OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", city));
                        multiLoad();
                        Snackbar.make(getView(), String.format(Locale.CHINA, "已经将%s删掉了 Ծ‸ Ծ", stock.getStockName()),
                            Snackbar
                                .LENGTH_LONG)
                            .setAction("撤销",
                                v -> {
//                                    OrmLite.getInstance().save(new CityORM(city));
                                    multiLoad();
                                }).show();
                    })
                    .show();
            }

            @Override
            public void click(StockResp.DataBean stock) {
                StockDetailActivity.launch(getActivity(), stock);
            }
        });

        mRefreshLayout.setColorSchemeResources(
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_green_dark,
            android.R.color.holo_blue_dark
        );
        mRefreshLayout.setOnRefreshListener(this::multiLoad);
    }

    private void multiLoad() {
        if (disposable != null) {
            disposable.dispose();
        }
        int autoRefresh = SharedPreferenceUtil.getInstance().getStockAutoRefresh();
        disposable = Observable.interval(0, autoRefresh, TimeUnit.SECONDS)

//            .doOnSubscribe(subscription -> mRefreshLayout.setRefreshing(true))
//            .map(city -> Util.replaceCity(city.getName()))
//            .distinct()
//            .flatMap(cityName -> RetrofitSingleton.getInstance().fetchStocks(cityName))
//            .filter(weather -> !C.UNKNOWN_CITY.equals(weather.status))
//            .take(3)
            .doOnSubscribe(disposable -> mRefreshLayout.setRefreshing(true))
            .flatMap(aLong -> {
                LogUtil.i(TAG, Thread.currentThread().getName() + " flatMap " + aLong);
                if (TimeUtil.isKP() || mStocks.size() == 0) {
                    return RetrofitSingleton.getInstance().fetchStocks();
                } else {
                    return Observable.just(aLong);
                }
            })
            .compose(RxUtil.fragmentLifecycle(this))
            .doOnNext(stocks -> {
                LogUtil.i(TAG, Thread.currentThread().getName() + " onNext " + stocks);
                mRefreshLayout.setRefreshing(false);
                if (stocks instanceof ArrayList) {
                    ArrayList<StockResp.DataBean> stocks1 = (ArrayList<StockResp.DataBean>) stocks;
                    if (mStocks.size() == 0) {
                        mStocks.addAll(stocks1);
                    } else {
                        Collections.copy(mStocks, stocks1);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (mAdapter.isEmpty()) {
                    mLayout.setVisibility(View.VISIBLE);
                } else {
                    mLayout.setVisibility(View.GONE);
                }
            })
            .doOnDispose(() -> LogUtil.i(TAG, "doOnDispose: "))
            .doOnComplete(() -> {
                LogUtil.i(TAG, "doOnComplete: ");
            })
            .doOnError(error -> {
                mRefreshLayout.setRefreshing(false);
                if (mAdapter.isEmpty()) {
                    mLayout.setVisibility(View.VISIBLE);
                }
            })
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe();

    }
}
