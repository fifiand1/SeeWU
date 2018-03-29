package com.wzf.wucarryme.modules.main.ui;

import java.util.List;

import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseFragment;
import com.wzf.wucarryme.common.utils.RxUtil;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.common.utils.ToastUtil;
import com.wzf.wucarryme.common.utils.VersionUtil;
import com.wzf.wucarryme.component.RetrofitSingleton;
import com.wzf.wucarryme.component.RxBus;
import com.wzf.wucarryme.modules.main.adapter.StockAdapter;
import com.wzf.wucarryme.modules.main.domain.ChangeCityEvent;
import com.wzf.wucarryme.modules.main.domain.StockResp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by HugoXie on 16/7/9.
 * <p>
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.iv_erro)
    ImageView mIvError;

    private static StockResp.DataBean mWeather = new StockResp.DataBean();
    private StockAdapter mAdapter;

    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.content_main, container, false);
            ButterKnife.bind(this, view);
        }
        mIsCreateView = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        load();
//        VersionUtil.checkVersion(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getDefault()
            .toObservable(ChangeCityEvent.class)
            .observeOn(AndroidSchedulers.mainThread())
            .filter(event -> isVisible())
            .doOnNext(event -> {
                mRefreshLayout.setRefreshing(true);
                load();
            })
            .subscribe();
    }

    private void initView() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
            mRefreshLayout.setOnRefreshListener(
                () -> mRefreshLayout.postDelayed(this::load, 1000));
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new StockAdapter(mWeather);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void load() {
        if (1 == 1) {
            mRefreshLayout.setRefreshing(false);
            return;
        }
        fetchDataByNetWork()
            .doOnSubscribe(aLong -> mRefreshLayout.setRefreshing(true))
            .doOnError(throwable -> {
                mIvError.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                SharedPreferenceUtil.getInstance().setCityName("北京");
                safeSetTitle("找不到城市啦");
            })
            .doOnNext(weather -> {
                mIvError.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

//                mWeather.status = weather.status;
//                mWeather.aqi = weather.aqi;
//                mWeather.basic = weather.basic;
//                mWeather.suggestion = weather.suggestion;
//                mWeather.now = weather.now;
//                mWeather.dailyForecast = weather.dailyForecast;
//                mWeather.hourlyForecast = weather.hourlyForecast;
//                safeSetTitle(weather.basic.city);
//                mAdapter.notifyDataSetChanged();
//                NotificationHelper.showWeatherNotification(getActivity(), weather);
            })
            .doOnComplete(() -> {
                mRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                ToastUtil.showShort(getString(R.string.complete));
            })
            .subscribe();
    }

    /**
     * 从网络获取
     */
    private Observable<List<StockResp.DataBean>> fetchDataByNetWork() {
        String cityName = SharedPreferenceUtil.getInstance().getCityName();
        return RetrofitSingleton.getInstance()
            .fetchStocks()
            .compose(RxUtil.fragmentLifecycle(this));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    @Override
    protected void lazyLoad() {

    }

    public StockResp.DataBean getWeather() {
        return mWeather;
    }
}
