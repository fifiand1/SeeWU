package com.wzf.wucarryme.modules.buysell.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import butterknife.BindView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.litesuits.orm.db.assit.QueryBuilder
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.ToolbarActivity
import com.wzf.wucarryme.common.utils.RxUtil
import com.wzf.wucarryme.component.OrmLite
import com.wzf.wucarryme.modules.care.domain.BuySellORM
import com.wzf.wucarryme.modules.service.CollectorService
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_position.*
import java.util.*
import java.util.regex.Pattern

class PositionActivity : ToolbarActivity() {
    @BindView(R.id.pie_chart)
    lateinit var mChart: PieChart

    private val colors = ArrayList<Int>()

    override fun layoutId(): Int {
        return R.layout.activity_position
    }

    override fun onStart() {
        super.onStart()
        for (c in ColorTemplate.MATERIAL_COLORS)
            colors.add(c)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        mChart.setUsePercentValues(true)
        mChart.description.isEnabled = false
        mChart.setExtraOffsets(5f, 10f, 5f, 5f)

        Observable.create(ObservableOnSubscribe<BuySellORM> { emitter ->
            val query = OrmLite.getInstance().query(QueryBuilder(BuySellORM::class.java).where("ACTION_ = ?",
                CollectorService.TYPE_POSITION)
                .appendOrderDescBy("LOG_TIME").limit("1"))
            emitter.onNext(query[0])
            emitter.onComplete()
        })
            .compose(RxUtil.io())
            .compose(RxUtil.activityLifecycle(this))
            .doOnNext {
                this@PositionActivity.initData(it)
            }
            .subscribe()
    }

    private fun initData(it: BuySellORM) {
        val entries = ArrayList<PieEntry>()

        //组装数据
        val desc = it.desc
//        val desc = "14:25 目前是50%仓位(5%券商.20%中药.25%化学制药)"
        val bracketPattern = Pattern.compile("(\\([^)]*\\))")
        val digitPattern = Pattern.compile("[^0-9]")
        val m = bracketPattern.matcher(desc)
        var str = ""
        while (m.find()) {
            val group = m.group()
            str = group.substring(1, group.length - 1)
        }
        val split = str.split(".")
        var rest = 100f
        for (i in split.indices) {
            val value = digitPattern.matcher(split[i])
            val trim = value.replaceAll("").trim()
            val toFloat = trim.toFloat()
            rest -= toFloat
            entries.add(PieEntry(toFloat, split[i].replace("$trim%", "")))
        }
        entries.add(PieEntry(rest, "空"))

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
//        data.setValueTypeface(mTfLight)
        mChart.data = data
        // undo all highlights
        mChart.highlightValues(null)

        mChart.invalidate()
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, PositionActivity::class.java))
        }
    }
}
