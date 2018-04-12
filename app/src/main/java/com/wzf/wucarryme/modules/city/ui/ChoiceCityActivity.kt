package com.wzf.wucarryme.modules.city.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity
import com.wzf.wucarryme.common.C
import com.wzf.wucarryme.common.Irrelevant
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.common.utils.Util
import com.wzf.wucarryme.component.OrmLite
import com.wzf.wucarryme.component.RxBus
import com.wzf.wucarryme.modules.city.adapter.CityAdapter
import com.wzf.wucarryme.modules.city.domain.City
import com.wzf.wucarryme.modules.city.domain.Province
import com.wzf.wucarryme.modules.main.domain.CityORM
import com.wzf.wucarryme.modules.main.domain.SelfSelectUpdateEvent
import com.wzf.wucarryme.modules.main.domain.StockResp
import io.reactivex.*
import io.reactivex.Observable
import java.util.*

class ChoiceCityActivity : ToolbarActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null

    private val dataList = ArrayList<StockResp.DataBean>()
    private var selectedProvince: Province? = null
    private val provincesList = ArrayList<Province>()
    private val cityList: List<City>? = null
    private var mAdapter: CityAdapter? = null
    private var currentLevel: Int = 0

    private var isChecked = false

    override fun canBack(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        Observable.create(ObservableOnSubscribe<Any> { emitter ->
            emitter.onNext(Irrelevant.INSTANCE)
            emitter.onComplete()
        })
                .compose(RxUtil.io())
                .compose(RxUtil.activityLifecycle(this))
                .doOnNext {
                    this@ChoiceCityActivity.initRecyclerView()
                    this@ChoiceCityActivity.queryProvinces()
                }
                .subscribe()

        val intent = intent
        isChecked = intent.getBooleanExtra(C.MULTI_CHECK, false)
        if (isChecked && SharedPreferenceUtil.instance.getBoolean("Tips", true)) {
            showTips()
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_choice_city
    }

    private fun initView() {
        mRecyclerView = findViewById(R.id.recyclerview)
        mProgressBar = findViewById(R.id.progressBar)
        if (mProgressBar != null) {
            mProgressBar!!.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setHasFixedSize(true)
        mAdapter = CityAdapter(this, dataList)
        mRecyclerView!!.adapter = mAdapter

        mAdapter!!.setOnItemClickListener(object : CityAdapter.OnRecyclerViewItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provincesList[pos]
                    mRecyclerView!!.smoothScrollToPosition(0)
                    this@ChoiceCityActivity.queryCities()
                } else if (currentLevel == LEVEL_CITY) {
                    val city = Util.replaceCity(cityList!![pos].mCityName)
                    if (isChecked) {
                        OrmLite.getInstance().save(CityORM(city))
                        RxBus.default.post(SelfSelectUpdateEvent())
                    } else {
//                        SharedPreferenceUtil.instance.setCityName(city)
//                        RxBus.default.post(ChangeCityEvent())
                    }
                    this@ChoiceCityActivity.quit()
                }
            }
        })
    }

    /**
     * 查询全国所有的省，从数据库查询
     */
    private fun queryProvinces() {
        toolbar!!.title = "选择省份"
        Flowable.create(FlowableOnSubscribe<String> { emitter ->
            if (provincesList.isEmpty()) {
                //                provincesList.addAll(WeatherDB.loadProvinces(DBManager.getInstance().getDatabase()));
            }
            dataList.clear()
            for (province in provincesList) {
                emitter.onNext(province.mProName)
            }
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
                .compose(RxUtil.ioF())
                .compose(RxUtil.activityLifecycleF(this))
                //            .doOnNext(proName -> dataList.add(proName))
                .doOnComplete {
                    mProgressBar!!.visibility = View.GONE
                    currentLevel = LEVEL_PROVINCE
                    mAdapter!!.notifyDataSetChanged()
                }
                .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.multi_city_menu, menu)
        menu.getItem(0).isChecked = isChecked
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.multi_check) {
            item.isChecked = !isChecked
            isChecked = item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 查询选中省份的所有城市，从数据库查询
     */
    private fun queryCities() {
        toolbar!!.title = "选择城市"
        dataList.clear()
        mAdapter!!.notifyDataSetChanged()

        Flowable.create(FlowableOnSubscribe<String> { emitter ->
            //            cityList = WeatherDB.loadCities(DBManager.getInstance().getDatabase(), selectedProvince.mProSort);
            for (city in cityList!!) {
                emitter.onNext(city.mCityName)
            }
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
                .compose(RxUtil.ioF())
                .compose(RxUtil.activityLifecycleF(this))
                //            .doOnNext(proName -> dataList.add(proName))
                .doOnComplete {
                    currentLevel = LEVEL_CITY
                    mAdapter!!.notifyDataSetChanged()
                    mRecyclerView!!.smoothScrollToPosition(0)
                }
                .subscribe()
    }

    override fun onBackPressed() {
        if (currentLevel == LEVEL_PROVINCE) {
            quit()
        } else {
            queryProvinces()
            mRecyclerView!!.smoothScrollToPosition(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //        DBManager.getInstance().closeDatabase();
    }

    private fun showTips() {
        AlertDialog.Builder(this)
                .setTitle("多城市管理模式")
                .setMessage("您现在是多城市管理模式,直接点击即可新增城市.如果暂时不需要添加," + "在右上选项中关闭即可像往常一样操作.\n因为 api 次数限制的影响,多城市列表最多三个城市.(๑′ᴗ‵๑)")
            .setPositiveButton("明白") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("不再提示") { _, _ -> SharedPreferenceUtil.instance.putBoolean("Tips", false) }
                .show()
    }

    private fun quit() {
        this@ChoiceCityActivity.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {

        const val LEVEL_PROVINCE = 1
        const val LEVEL_CITY = 2

        fun launch(context: Context) {
            context.startActivity(Intent(context, ChoiceCityActivity::class.java))
        }
    }
}
