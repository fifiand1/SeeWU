package com.wzf.wucarryme.modules.main.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseFragment
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.common.utils.ToastUtil
import com.wzf.wucarryme.component.RetrofitSingleton
import com.wzf.wucarryme.component.RxBus
import com.wzf.wucarryme.modules.main.adapter.StockAdapter
import com.wzf.wucarryme.modules.main.domain.ChangeCityEvent
import com.wzf.wucarryme.modules.main.domain.StockResp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class MainFragment : BaseFragment() {

    @BindView(R.id.recyclerview)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swiprefresh)
    lateinit var mRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.progressBar)
    lateinit var mProgressBar: ProgressBar
    @BindView(R.id.iv_erro)
    lateinit var mIvError: ImageView
    private var mAdapter: StockAdapter? = null

    private var mView: View? = null

    val weather: StockResp.DataBean
        get() = mWeather

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater.inflate(R.layout.content_main, container, false)
            ButterKnife.bind(this, mView!!)
        }
        mIsCreateView = true
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
//        load()
        //        VersionUtil.checkVersion(getActivity());
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: 2018/4/4 leaks here
        RxBus.default
                .toObservable(ChangeCityEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter({ isVisible })
                .doOnNext({
                    mRefreshLayout.isRefreshing = true
                    load()
                })
                .subscribe()
    }

    private fun initView() {
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)
        mRefreshLayout.setOnRefreshListener { mRefreshLayout.postDelayed({ this.load() }, 1000) }

        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter = StockAdapter(mWeather)
        mRecyclerView.adapter = mAdapter
    }

    private fun load() {
        if (1 == 1) {
            mRefreshLayout.isRefreshing = false
            return
        }
        fetchDataByNetWork()
            .doOnSubscribe { mRefreshLayout.isRefreshing = true }
                .doOnError {
                    mIvError.visibility = View.VISIBLE
                    mRecyclerView.visibility = View.GONE
                    SharedPreferenceUtil.instance.cityName = "北京"
                    safeSetTitle("找不到城市啦")
                }
                .doOnNext {
                    mIvError.visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE

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
                }
                .doOnComplete {
                    mRefreshLayout.isRefreshing = false
                    mProgressBar.visibility = View.GONE
                    ToastUtil.showShort(getString(R.string.complete))
                }
                .subscribe()
    }

    /**
     * 从网络获取
     */
    private fun fetchDataByNetWork(): Observable<List<StockResp.DataBean>> {
        val cityName = SharedPreferenceUtil.instance.cityName
        return RetrofitSingleton.instance
                .fetchStocks()
                .compose(RxUtil.fragmentLifecycle<List<StockResp.DataBean>>(this))
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    override fun lazyLoad() {

    }

    companion object {

        private val mWeather = StockResp.DataBean()
    }
}
