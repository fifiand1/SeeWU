package com.wzf.wucarryme.modules.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.litesuits.orm.db.assit.QueryBuilder
import com.wzf.wucarryme.common.utils.LogUtil
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil
import com.wzf.wucarryme.common.utils.StringUtil
import com.wzf.wucarryme.common.utils.StringUtil.banKuai
import com.wzf.wucarryme.common.utils.TimeUtil
import com.wzf.wucarryme.component.MailHelper
import com.wzf.wucarryme.component.NotificationHelper
import com.wzf.wucarryme.component.OrmLite
import com.wzf.wucarryme.component.RetrofitSingleton
import com.wzf.wucarryme.modules.care.domain.BuySellORM
import com.wzf.wucarryme.modules.care.domain.CareORM
import com.wzf.wucarryme.modules.main.domain.StockResp
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

//6月2日10:00

class CollectorService : Service() {
    private val TAG = CollectorService::class.java.simpleName
    private var mDisposable: Disposable? = null
    private var mIsUnSubscribed = true

    private var todayDate = ""
    private var todayURL = ""
    private lateinit var doc: Document
    private var storedBought: MutableList<String> = ArrayList()
    private var storedSold: MutableList<String> = ArrayList()
    private var storedSpace: MutableList<String> = ArrayList()
    private var storedCare: MutableList<String> = ArrayList()
    private var storedNormal: MutableList<String> = ArrayList()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        synchronized(this) {
            unSubscribed()
            if (mIsUnSubscribed) {
                unSubscribed()
                val autoRefresh = SharedPreferenceUtil.instance.blogAutoRefresh
                if (autoRefresh != 0) {
                    mDisposable = Observable.interval(0, autoRefresh.toLong(), TimeUnit.MINUTES)
                        .observeOn(Schedulers.io())
                        .doOnSubscribe { disposable ->
                            LogUtil.d(TAG, "jsoup TodayURL " + Thread.currentThread().name)
                            if (!jsoupTodayURL()) {
                                disposable.dispose()
                            }
                        }
                        .doOnNext { aLong ->
                            LogUtil.d(TAG, aLong.toString() + "jsoup Article " + Thread.currentThread().name)
                            mIsUnSubscribed = false
                            if (TimeUtil.isKP || storedNormal.size == 0) {
                                //app居然一直在后台没被杀死
                                if (todayDate != TimeUtil.nowYueRi) {
                                    jsoupTodayURL()
                                }
                                jsoupArticle()
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
            }
        }
        return Service.START_REDELIVER_INTENT
    }

    private fun jsoupTodayURL(): Boolean {
        try {
            todayDate = TimeUtil.nowYueRi
//            todayDate = "5月8日"
            doc = Jsoup.connect(url).get()
            val elementsByAttributeValue = doc.getElementsByAttributeValue("class", "blog_title_h")
            for (element in elementsByAttributeValue) {
                val a = element.getElementsByTag("a")
                val element1 = a[0]
                val data = element1.text()

                //5月17日wu2198股市直播
                if (data == todayDate + TITLE) {
                    todayURL = element1.attr("href")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        LogUtil.i(TAG, "today URL -> $todayURL")
        return "" != todayURL
    }

    private fun jsoupArticle() {
        try {

            //                LogUtil.i(TAG, "get one-> " + text);
            doc = Jsoup.connect(todayURL).get()
            val content = doc.getElementById(CONTENT_ID)
            val p = content.getElementsByTag("p")
            for (element in p) {
                val text = element.text()
                //                LogUtil.i(TAG, "get one-> " + text);

                val care = important(text, CARE_REG)

                //卖出
                if (important(text, SELL_REG) != null) {
                    if (!storedSold.contains(text)) {
                        storedSold.add(text)

                        val insertExcelSELL = insertExcelSELL(text)
                        val blogTime = StringUtil.getBlogTime(text)
                        for (item in insertExcelSELL) {
                            // TODO: 2018/5/9 仓位%暂时不管
                            //找出当时买的
                            val query = OrmLite.getInstance().query(QueryBuilder(BuySellORM::class.java)
                                .where("CATEGORY_ = ? AND ACTION_ = ?", item.category, CollectorService.TYPE_BUY)
                                .appendOrderDescBy("ID_").limit("1"))

                            if (query.size > 0 && query[0].stock1 != null) {
                                //得到现在价格
                                val subscribeOn1 = RetrofitSingleton.instance.fetchStockByNameCN(query[0].stock1!!)
                                val subscribeOn2 = RetrofitSingleton.instance.fetchStockByNameCN(query[0].stock2!!)

                                Observable.zip(subscribeOn1, subscribeOn2, BiFunction<StockResp.DataBean, StockResp
                                .DataBean, BuySellORM> { t1, t2 ->
                                    item.stock1 = t1.stockName
                                    item.stock1Price = t1.newPrice
                                    item.stock2 = t2.stockName
                                    item.stock2Price = t2.newPrice
                                    //计算收益
                                    val lastBuyPrice = query[0].stock1Price
                                    if (lastBuyPrice != null) {
                                        item.stock1Return = String.format(Locale.CHINA,
                                            "%.2f",
                                            (t1.newPrice!!.toFloat() - lastBuyPrice.toFloat())
                                                / lastBuyPrice.toFloat() * 100) + "%"
                                    }
                                    val lastBuyPrice2 = query[0].stock2Price
                                    if (lastBuyPrice2 != null) {
                                        item.stock2Return = String.format(Locale.CHINA,
                                            "%.2f",
                                            (t2.newPrice!!.toFloat() - lastBuyPrice2.toFloat())
                                                / lastBuyPrice2.toFloat() * 100) + "%"
                                    }
                                    return@BiFunction item
                                }).subscribe {
                                    //插入数据
                                    print(TYPE_SELL, text)
                                    val x = "$blogTime************************卖出参考************************"
                                    print(TYPE_SELL, x)
                                    print(TYPE_SELL, "************************" + item.stock1 + item.stock1Price +
                                        "************************" + item.stock1Return)
                                    print(TYPE_SELL, "************************" + item.stock2 + item.stock2Price +
                                        "************************" + item.stock2Return)
                                    print(TYPE_SELL, x)
                                    insertBuySell(item)
                                }
                            } else {
                                //没找到之前记录 直接插入
                                print(TYPE_SELL, text)
                                insertBuySell(item)
                            }

                        }
                    }
                } else if (important(text, BUY_REG) != null) {
                    //买入
                    if (!storedBought.contains(text)) {
                        storedBought.add(text)
                        print(TYPE_BUY, text)
                        val strings = searchMaybeBought(text)
                        val blogTime = StringUtil.getBlogTime(text)
                        for (item in strings) {
                            val subscribeOn1 = RetrofitSingleton.instance.fetchStockByNameCN(item.stock1!!)
                            val subscribeOn2 = RetrofitSingleton.instance.fetchStockByNameCN(item.stock2!!)

                            Observable.zip(subscribeOn1, subscribeOn2, BiFunction<StockResp.DataBean, StockResp
                            .DataBean, BuySellORM> { t1, t2 ->
                                item.stock1Price = t1.newPrice
                                item.stock2Price = t2.newPrice
                                return@BiFunction item
                            }).subscribe {
                                val x = "$blogTime************************买入选择************************"
                                print(TYPE_BUY, x)
                                print(TYPE_BUY, "************************" + item.stock1 + item.stock1Price +
                                    "************************" + item.count1)
                                print(TYPE_BUY, "************************" + item.stock2 + item.stock2Price +
                                    "************************" + item.count2)
                                print(TYPE_BUY, x)
                                insertBuySell(item)
                            }

                        }
                    }
                } else if (important(text, SPACE_REG) != null) {
                    //仓位
                    if (!storedSpace.contains(text)) {
                        storedSpace.add(text)
                        print(TYPE_POSITION, text)
                        insertPosition(text)
                    }
                } else if (care != null) {
                    if (!storedCare.contains(text)) {
                        storedCare.add(text)
                        print(TYPE_CARE, text)
                        insertCare(text, care)
                    }
                } else {
                    if (!storedNormal.contains(text)) {
                        storedNormal.add(text)
                        LogUtil.w(TAG, text)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            // jsoup 的 Exception 类型都是 Error
            e.printStackTrace()
        }

    }

    private fun insertPosition(text: String) {
        val important = important(text, SPACE_REG)
        val substring = important!!.substring(0, important.length - 1)
        val chars = substring.toCharArray()
        val position = StringBuilder()
        if (Character.isDigit(chars[chars.size - 3])) {
            position.append(chars[chars.size - 3])
        }
        position.append(chars[chars.size - 2])
        position.append(chars[chars.size - 1])

        val buySellORM = BuySellORM()

        val blogTime = StringUtil.getBlogTime(text)
        val nowYMDHMSTime = TimeUtil.nowYMDHMSTime
        buySellORM.action = TYPE_POSITION
        buySellORM.stock1 = position.toString()
        buySellORM.blogTime = todayDate + blogTime
        buySellORM.logTime = nowYMDHMSTime
        buySellORM.desc = text
        insertBuySell(buySellORM)
        NotificationHelper.showPositioningNotification(this@CollectorService, buySellORM)

    }

    private fun insertBuySell(it: BuySellORM) {
        try {
            OrmLite.getInstance().insert(it)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Observable.create(ObservableOnSubscribe<Any> { emitter ->
            //发个邮件吧
            MailHelper.sendWarningMail(it.action!!, it.toString())
            emitter.onComplete()
        })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun searchMaybeBought(s: String): List<BuySellORM> {
        val p = Pattern.compile("(%[^股]*股)")
        val m = p.matcher(s)
        val bankuaiList = ArrayList<String>()
        val result = ArrayList<BuySellORM>()
        while (m.find()) {
            val group = m.group()
            //            result.put(group.substring(1,group.length()-1),null);
            bankuaiList.add(group.substring(1, group.length - 1))
        }
        if (bankuaiList.size == 0) {
            bankuaiList.add(s.substring(s.indexOf("%") + 1))
        }
        val blogTime = StringUtil.getBlogTime(s)
        val nowYMDHMSTime = TimeUtil.nowYMDHMSTime


        for (i in bankuaiList.indices) {
            val bankuai = bankuaiList[i]

            val buySellORM = BuySellORM()
            buySellORM.action = TYPE_BUY
            buySellORM.blogTime = todayDate + blogTime
            buySellORM.category = bankuai
            buySellORM.logTime = nowYMDHMSTime
            buySellORM.desc = s
            try {

                var like = "CATEGORY_ like '%$bankuai'"
                var sql = "SELECT STOCK_NAME,count(*) as c FROM CARE_ where STOCK_NAME in(" +
                    "   select DISTINCT STOCK_NAME FROM CARE_ where " + like + ")" +
                    "GROUP BY STOCK_NAME ORDER BY c DESC limit 2"
                LogUtil.d(TAG, "searchMaybeBought() sql = [$sql]")
                var readableDatabase = OrmLite.getInstance().readableDatabase
                var cursor = readableDatabase.rawQuery(sql, null)
                if (cursor.count < 1) {
                    like = StringUtil.genLike("CATEGORY_", bankuai)
                    sql = "SELECT STOCK_NAME,count(*) as c FROM CARE_ where STOCK_NAME in(" +
                        "   select DISTINCT STOCK_NAME FROM CARE_ where " + like + ")" +
                        "GROUP BY STOCK_NAME ORDER BY c DESC limit 2"
                    LogUtil.d(TAG, "searchMaybeBought() sql = [$sql]")
                    readableDatabase = OrmLite.getInstance().readableDatabase
                    cursor = readableDatabase.rawQuery(sql, null)
                }

                var success = cursor.moveToNext()
                if (success) {
                    val name = cursor.getString(0)
                    val count = cursor.getInt(1)
                    buySellORM.stock1 = name
                    buySellORM.count1 = count
                }

                success = cursor.moveToNext()
                if (success) {
                    val name2 = cursor.getString(0)
                    val count2 = cursor.getInt(1)
                    buySellORM.stock2 = name2
                    buySellORM.count2 = count2
                }
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            result.add(buySellORM)
            NotificationHelper.showPositioningNotification(this@CollectorService, buySellORM)
        }

        return result
    }

    private fun insertCare(p: String, group: String) {
        var p = p
        var group = group
        try {
            group = group.substring(2, group.length - 1)
            val category = banKuai(group)

            val blogTime = StringUtil.getBlogTime(p)
            p = p.substring(p.indexOf(" ") + 1)
            val cnNames = p.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val list = ArrayList<CareORM>()
            val nowYMDHMSTime = TimeUtil.nowYMDHMSTime
            for (cnName in cnNames) {
                val formated = StringUtil.formatCNName(cnName)

                val care = CareORM(todayDate + blogTime,
                    category, formated, "", nowYMDHMSTime)
                list.add(care)

            }
            val result = OrmLite.getInstance().insert(list)
            LogUtil.d(TAG, "add care $category: $result")
            //            long l = OrmLite.getInstance().queryCount(CareORM.class);
            //            LogUtil.d(TAG, "after care: " + l);
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun print(type: String, text: String) {
        LogUtil.e(TAG, "$type -> $text")
    }

    private fun insertExcelSELL(s: String): ArrayList<BuySellORM> {
        var p = Pattern.compile("(%[^股]*股)")
        var m = p.matcher(s)
        val bankuaiList = ArrayList<String>()
        while (m.find()) {
            val group = m.group()
            bankuaiList.add(banKuai(group.substring(1, group.length - 1)))
        }

        val blogTime = StringUtil.getBlogTime(s)

        val list = ArrayList<BuySellORM>()
        val nowYMDHMSTime = TimeUtil.nowYMDHMSTime
        if (bankuaiList.size == 0) {
            p = Pattern.compile("(%[^,]*,)")
            m = p.matcher(s)
            while (m.find()) {
                val group = m.group()
                bankuaiList.add(banKuai(group.substring(1, group.length - 1)))
            }
        }
        try {
            for (i in bankuaiList.indices) {
                val bankuai = bankuaiList[i]

                val buySell = BuySellORM(todayDate + blogTime,
                    bankuai, TYPE_SELL, "", "", s, nowYMDHMSTime)
                list.add(buySell)
                NotificationHelper.showPositioningNotification(this@CollectorService, buySell)
            }
            return list
//            OrmLite.getInstance().insert(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun important(p: String, reg: Array<String>): String? {
        for (key in reg) {
            // 编译正则表达式
            val pattern = Pattern.compile(key)
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            val matcher = pattern.matcher(p)
            // 查找字符串中是否有匹配正则表达式的字符/字符串
            val rs = matcher.find()
            if (rs) {
                return matcher.group()
            }
        }
        return null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun unSubscribed() {
        mIsUnSubscribed = true
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    companion object {
        const val TITLE = "wu2198股市直播"
        const val url = "http://blog.sina.com.cn/u/1216826604"
        const val CONTENT_ID = "sina_keyword_ad_area2"
        val BUY_REG = arrayOf("买入.*%", "买.*%", "买进.*%", "增持.*%", "增仓.*%", "回补.*%", "加仓.*%")
        val SELL_REG = arrayOf("卖出.*%", "卖.*%", "兑现.*%", "T出.*%", "减仓.*%", "走了.*%", "走掉.*%", "砍掉.*%", "减掉.*%")
        val SPACE_REG = arrayOf("目前.*帐户.?.?.?.?%", ".*帐户.?.?.?.?%", "目前.?.?.?.?%", "现在.?.?.?.?%", "仓位是零", "仓位暂时是零")
        val CARE_REG = arrayOf("领先.*股", "领先.*板", "等.*股继续领先", "出现冲涨停", "目前具有上涨", "目前涨停", "目前领先", "出现涨停", "改写了新高", "等领先")

        val collectDate = arrayOf<String>()
        val collectURL = arrayOf<String>()
        //可能关心的,例如xxxx,xxxx等领先xx股
        const val TYPE_CARE = "CARE"
        //买入
        const val TYPE_BUY = "BUY"
        //卖出
        const val TYPE_SELL = "SELL"
        //持仓
        const val TYPE_POSITION = "POS"
    }

}
