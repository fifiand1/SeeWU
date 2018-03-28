package com.wzf.wucarryme.modules.main.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseFragment;
import com.wzf.wucarryme.common.utils.RxUtil;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.component.RetrofitSingleton;
import com.wzf.wucarryme.component.RxBus;
import com.wzf.wucarryme.modules.main.adapter.MultiCityAdapter;
import com.wzf.wucarryme.modules.main.domain.SelfSelectUpdateEvent;
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

    private MultiCityAdapter mAdapter;
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
            view = inflater.inflate(R.layout.fragment_multicity, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getDefault()
            .toObservable(SelfSelectUpdateEvent.class)
            .doOnNext(event -> multiLoad())
            .subscribe();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        multiLoad();
    }

    private void initView() {
        mStocks = new ArrayList<>();
        mAdapter = new MultiCityAdapter(mStocks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMultiCityClick(new MultiCityAdapter.onMultiCityClick() {
            @Override
            public void longClick(StockResp.DataBean stock) {
                new AlertDialog.Builder(getActivity())
                    .setMessage("是否删除该自选股?")
                    .setPositiveButton("删除", (dialog, which) -> {
                        // TODO: 2018/3/28 自选保存到数据库
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
            android.R.color.holo_green_light,
            android.R.color.holo_blue_bright
        );
        mRefreshLayout.setOnRefreshListener(() -> mRefreshLayout.postDelayed(this::multiLoad, 1000));
    }

    private void multiLoad() {
        if (disposable != null && !disposable.isDisposed()) {
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
                mStocks.clear();
                return RetrofitSingleton.getInstance().fetchStocks();
            })
            .compose(RxUtil.fragmentLifecycle(this))
            .doOnNext(stocks -> {
                mStocks.addAll(stocks);
//                Log.i(TAG, "multiLoad: onNext " + mStocks.size());
                mRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.isEmpty()) {
                    mLayout.setVisibility(View.VISIBLE);
                } else {
                    mLayout.setVisibility(View.GONE);
                }
            })
//            .doOnDispose(() -> Log.i(TAG, "doOnDispose: "))
//            .doOnComplete(() -> {
//            })
            .doOnError(error -> {
                if (mAdapter.isEmpty()) {
                    mLayout.setVisibility(View.VISIBLE);
                }
            })
            .subscribe();

    }
}
