package com.wzf.wucarryme.modules.main.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseFragment
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.common.utils.TimeUtil
import com.wzf.wucarryme.component.RetrofitSingleton
import com.wzf.wucarryme.component.RxBus
import com.wzf.wucarryme.component.ViewStyleHelper
import com.wzf.wucarryme.modules.main.adapter.MultiStockAdapter
import com.wzf.wucarryme.modules.main.domain.StockResp
import com.wzf.wucarryme.modules.main.domain.StockUpdateEvent
import com.wzf.wucarryme.modules.main.domain.StockUpdateFinishEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class SelfSelectStockFragment : BaseFragment() {
    @BindView(R.id.recyclerview)
    lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swiprefresh)
    lateinit var mRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.empty)
    lateinit var mLayout: LinearLayout

    private var mAdapter: MultiStockAdapter? = null
    private var mStocks: MutableList<StockResp.DataBean>? = null
    private var disposable: Disposable? = null

    private var mView: View? = null
    private var notify: StockResp.DataBean? = null

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    override fun lazyLoad() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_self_select_stocks, container, false)
            ButterKnife.bind(this, mView!!)
        }
        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: 2018/3/29 leaks here
        RxBus.default
            .toObservable(StockUpdateEvent::class.java)
            .doOnNext {
                notify = it.stock
                multiLoad()
            }
            .subscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        multiLoad()
    }

    private fun initView() {
        mStocks = ArrayList()
        mAdapter = MultiStockAdapter(mStocks as ArrayList<StockResp.DataBean>)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.adapter = mAdapter
        mAdapter!!.setMultiCityClick(object : MultiStockAdapter.OnMultiStockClick {
            override fun longClick(dataBean: StockResp.DataBean) {
                AlertDialog.Builder(this@SelfSelectStockFragment.activity!!.applicationContext)
                    .setMessage("是否删除该自选股?")
                    .setPositiveButton("删除") { dialog, which ->
                        // TODO: 2018/3/28 自选保存到数据库或sp
                        // OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", city));
                        multiLoad()
                        Snackbar.make(view!!, String.format(Locale.CHINA, "已经将%s删掉了 Ծ‸ Ծ", dataBean.stockName),
                            Snackbar
                                .LENGTH_LONG)
                            .setAction("撤销",
                                {
                                    // OrmLite.getInstance().save(new CityORM(city));
                                    multiLoad()
                                }).show()
                    }
                    .show()
            }

            override fun click(dataBean: StockResp.DataBean, vararg clicked: Pair<View, String>) {
                notify = dataBean
                StockDetailActivity.launch(activity!!, dataBean, *clicked)
            }
        })

        ViewStyleHelper.setSwipeRefreshColor(mRefreshLayout)
        mRefreshLayout.setOnRefreshListener({ this.multiLoad() })
    }

    private fun multiLoad() {
        if (disposable != null) {
            disposable!!.dispose()
        }
        val autoRefresh = SharedPreferenceUtil.instance.stockAutoRefresh
        disposable = Observable.interval(0, autoRefresh.toLong(), TimeUnit.SECONDS)

            //            .doOnSubscribe(subscription -> mRefreshLayout.setRefreshing(true))
            //            .map(city -> Util.replaceCity(city.getName()))
            //            .distinct()
            //            .flatMap(cityName -> RetrofitSingleton.getInstance().fetchStocks(cityName))
            //            .filter(weather -> !C.UNKNOWN_CITY.equals(weather.status))
            //            .take(3)
            .doOnSubscribe { mRefreshLayout.isRefreshing = true }
            .flatMap<Any> { aLong ->
                LogUtil.d(TAG, Thread.currentThread().name + " flatMap " + aLong)
                if (TimeUtil.isKP || mStocks!!.size == 0) {
                    return@flatMap RetrofitSingleton.instance.fetchStocks()
                }
                return@flatMap Observable.just(aLong)
            }
            .compose(RxUtil.fragmentLifecycle(this))
            .doOnNext { stocks ->
                LogUtil.i(TAG, Thread.currentThread().name + " onNext " + stocks)
                mRefreshLayout.isRefreshing = false
                if (stocks is ArrayList<*>) {
                    val stocks1 = stocks as ArrayList<StockResp.DataBean>
                    if (mStocks!!.size == 0) {
                        mStocks!!.addAll(stocks1)
                    } else {
                        Collections.copy<StockResp.DataBean>(mStocks!!, stocks1)
                    }
                    mAdapter!!.notifyDataSetChanged()
                }
                if (mAdapter!!.isEmpty) {
                    mLayout.visibility = View.VISIBLE
                } else {
                    mLayout.visibility = View.GONE
                }
                if (notify != null) {
                    val filter = mStocks!!.filter { it == notify }
                    RxBus.default.post(StockUpdateFinishEvent(filter[0]))
                }
            }
            .doOnDispose { LogUtil.i(TAG, "doOnDispose: ") }
            .doOnComplete { LogUtil.i(TAG, "doOnComplete: ") }
            .doOnError {
                LogUtil.i(TAG, Thread.currentThread().name + " doOnError ")

                mRefreshLayout.isRefreshing = false
                if (mAdapter!!.isEmpty) {
                    mLayout.visibility = View.VISIBLE
                }
            }
            .subscribe()

    }

    companion object {

        private val TAG = SelfSelectStockFragment::class.java.simpleName
    }
}
