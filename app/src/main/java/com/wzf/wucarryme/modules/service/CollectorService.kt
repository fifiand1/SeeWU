package com.wzf.wucarryme.modules.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.JsonParser
import com.litesuits.orm.db.assit.QueryBuilder
import com.litesuits.orm.db.model.ConflictAlgorithm
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
import com.wzf.wucarryme.modules.care.domain.MailCacheORM
import com.wzf.wucarryme.modules.main.domain.Sentence
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
import kotlin.collections.HashMap

//6月2日10:00

class CollectorService : Service() {
    private val TAG = CollectorService::class.java.simpleName
    private var mDisposable: Disposable? = null
    private var mIsUnSubscribed = true

    private var todayDate = ""
    private var todayURL = ""
    private lateinit var doc: Document
    private var storedBought: MutableList<Sentence> = ArrayList()
    private var storedSold: MutableList<Sentence> = ArrayList()
    private var storedSpace: MutableList<Sentence> = ArrayList()
    private var storedCare: MutableList<Sentence> = ArrayList()
    private var storedNormal: MutableList<Sentence> = ArrayList()

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
                            jsoupTodayURL()
                            /*有时开盘了还没有开始直播
                            if (!jsoupTodayURL()) {
                                disposable.dispose()
                            }*/
                        }
                        .doOnNext { aLong ->
                            LogUtil.d(TAG, aLong.toString() + "jsoup Article " + Thread.currentThread().name)
                            mIsUnSubscribed = false
                            if (todayURL == "") {
                                jsoupTodayURL()
                            } else if (TimeUtil.isKP || storedNormal.size == 0) {
                                //app居然一直在后台没被杀死
                                if (todayDate != TimeUtil.nowYueRi) {
                                    jsoupTodayURL()
                                }
                                jsoupArticle()
                            }
                            jsoupWeiboArticle()
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
            }
        }
        return START_REDELIVER_INTENT
    }

    private fun jsoupTodayURL(): Boolean {
        try {
            todayDate = TimeUtil.nowYueRi
//            todayDate = "7月12日"
            doc = Jsoup.connect(url).get()
            val elementsByAttributeValue = doc.getElementsByAttributeValue("class", "blog_title")
            for (element in elementsByAttributeValue) {
                val a = element.getElementsByTag("a")
                var nextElementSibling = element.nextElementSibling()
                while (nextElementSibling.tagName() != "span") {
                    nextElementSibling = nextElementSibling.nextElementSibling()
                }
                val textNodes = nextElementSibling.textNodes()
                val time = textNodes[0].text()
                val element1 = a[0]
                val title = element1.text()
                //<span class="time SG_txtc">(2019-03-29 14:47)</span>
                LogUtil.d(TAG, "blog title: $title $time")
                val it = BuySellORM(time, "", TYPE_CARE, "", "", title + a.attr("href"), todayDate)
                sendMail(it)

                //5月17日wu2198股市直播
                if (title == todayDate + TITLE) {
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
                val text = Sentence(element.text())
                //                LogUtil.i(TAG, "get one-> " + text);
                val sell = important(text.value, SELL_REG)
                val buy = important(text.value, BUY_REG)

                // FIXME: 2018/7/12 单条信息同时包含buy sell，简单处理
                //"9:30 借高开兑现20%医药股(挂单成交),昨日冲涨停今天高看吃肉走人.买进20%通信股"
                if (buy != null && sell != null) {
                    val split = text.value.split(".")
                    var blogTime1 = ""
                    split.forEach {
                        val blogTime = StringUtil.getBlogTime(it)
                        var fixedText = it
                        //后面的会获取不到时间，强行把内容变成两条
                        if (blogTime == null) {
                            fixedText = "$blogTime1 $fixedText"
                        } else {
                            blogTime1 = blogTime
                        }
                        analyses(Sentence(fixedText))
                    }
                } else {
                    analyses(text)
                }
            }
        } catch (e: Exception) {
            LogUtil.w(TAG, "jsoupArticle exception: " + e.message)
        } catch (e: Error) {
            // jsoup 的 Exception 类型都是 Error
            LogUtil.w(TAG, "jsoupArticle error: " + e.message)
        }

    }


    private fun jsoupWeiboArticle() {
        try {
            val map = HashMap<String, String>()
            LogUtil.d(TAG, "current:${System.currentTimeMillis()}")
            //FIXME 和currentTimeMillis有关，是否会过期待验证
            map.put("Apache","2019607141922.4148.1557193686510")
            map.put("ULV","1557193686590:2:2:2:2019607141922.4148.1557193686510:1557126737039")
            map.put("TC-Page-G0", "c4376343b8c98031e29230e0923842a5|1557193685|1557193685")

            //这3个不变
            map.put("SINAGLOBAL","8447668177794.232.1557126736964")
            map.put("SUB","_2AkMrk1F4f8NxqwJRmPoTy2nka4VzzQ_EieKdz6CjJRMxHRl-yj9jqlAatRB6ABN_lyXCaJZ9LTNCbDriTZc7K-eoCKcN")
            map.put("SUBP","0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5zN12sA.aziaowF8KXvfMS")
            doc = Jsoup.connect(weibourl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                .cookies(map).get()

            val scripts = doc.getElementsByTag("script")
            for (script in scripts) {
                val text = script.data()
                if (text.startsWith("FM.view")) {
                    val jsonStr = text.substring(8, text.lastIndexOf(")"))
                    val asJsonObject = JsonParser().parse(jsonStr).asJsonObject
                    if (asJsonObject.has("ns") &&
                        asJsonObject.get("ns").asString == "pl.content.homeFeed.index") {
                        doc.body().append(asJsonObject.get("html").asString)
                    }
                }
            }

            val divs = doc.getElementsByClass(weiboDivClass)
            divs.reverse()
            for (div in divs) {
                var time = div.getElementsByClass("WB_from S_txt2")[0].child(0).attr("title")
                val detail = div.getElementsByClass("WB_text W_f14")[0].textNodes()[0].text()
                if (time.contains(TimeUtil.nowYMD)) {
                    time = time.substring(11)
                    val text = Sentence("$time weibo->$detail")
                    analyses(text)
                }
            }

        } catch (e: Exception) {
            LogUtil.w(TAG, "jsoupWeiboArticle exception: " + e.message)
        } catch (e: Error) {
            // jsoup 的 Exception 类型都是 Error
            LogUtil.w(TAG, "jsoupWeiboArticle error: " + e.message)
        }

    }

    private fun analyses(text: Sentence) {
        var normalInfo = true
        val care = important(text.value, CARE_REG)
        val sell = important(text.value, SELL_REG)
        val buy = important(text.value, BUY_REG)
        //卖出
        if (sell != null) {
            normalInfo = false
            if (!storedSold.contains(text)) {
                storedSold.add(text)
                print(TYPE_SELL, text.value)

                val insertExcelSELL = insertExcelSELL(text.value)
                val blogTime = StringUtil.getBlogTime(text.value)
                for (item in insertExcelSELL) {
                    // TODO: 2018/5/9 仓位%暂时不管
                    //找出当时买的
                    val query = OrmLite.getInstance().query(QueryBuilder(BuySellORM::class.java)
                        .where("CATEGORY_ = ? AND ACTION_ = ?", item.category, TYPE_BUY)
                        .appendOrderDescBy("ID_").limit("1"))

                    if (query.size > 0 && query[0].stock1 != null) {
                        //得到现在价格
                        val subscribeOn1 = RetrofitSingleton.instance.fetchStockByNameCN(query[0].stock1!!)
                        val subscribeOn2 = RetrofitSingleton.instance.fetchStockByNameCN(query[0].stock2!!)

                        val subscribe = Observable.zip(subscribeOn1,subscribeOn2,
                            BiFunction<StockResp.DataBean, StockResp
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
                                val x = "$blogTime************************卖出参考(${item
                                    .category})************************"
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
                        val x = "$blogTime************************卖出参考null(${item
                            .category})************************"
                        print(TYPE_SELL, x)
                        insertBuySell(item)
                    }

                }
            }
        }
        //买入
        if (buy != null) {
            normalInfo = false
            if (!storedBought.contains(text)) {
                storedBought.add(text)
                print(TYPE_BUY, text.value)
                val strings = searchMaybeBought(text.value)
                val blogTime = StringUtil.getBlogTime(text.value)
                for (item in strings) {
                    if (item.stock1 == null) {
                        insertBuySell(item)
                        continue
                    }
                    val subscribeOn1 = RetrofitSingleton.instance.fetchStockByNameCN(item.stock1!!)
                    val subscribeOn2 = RetrofitSingleton.instance.fetchStockByNameCN(item.stock2!!)

                    val subscribe = Observable.zip(subscribeOn1, subscribeOn2, BiFunction<StockResp.DataBean, StockResp
                        .DataBean, BuySellORM> { t1, t2 ->
                            item.stock1Price = t1.newPrice
                            item.stock2Price = t2.newPrice
                            return@BiFunction item
                        }).subscribe {
                            val x = "$blogTime************************买入选择(${item.category})************************"
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
        }
        //仓位
        if (important(text.value, SPACE_REG) != null) {
            normalInfo = false
            if (!storedSpace.contains(text)) {
                storedSpace.add(text)
                print(TYPE_POSITION, text.value)
                insertPosition(text.value)
            }
        }
        if (care != null) {
            normalInfo = false
            if (!storedCare.contains(text)) {
                storedCare.add(text)
                print(TYPE_CARE, text.value)
                insertCare(text.value, care)
            }
        }

        if (normalInfo && !storedNormal.contains(text)) {
            storedNormal.add(text)
            LogUtil.w(TAG, text.value)
        }
    }

    private fun insertPosition(text: String) {
        //TODO 2019-3-29 区分短/中长线账户仓位
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

        sendMail(it)
    }

    private fun sendMail(it: BuySellORM) {
        Observable.create(ObservableOnSubscribe<Any> { emitter ->
            //发个邮件吧
            val cache = MailCacheORM(it.blogTime, it.category + it.desc, it.toString(), TimeUtil.nowYMDHMSTime)
            val result = OrmLite.getInstance().insert(cache, ConflictAlgorithm.Ignore)
            LogUtil.d(TAG, "insert MailCache result:$result")
            if (result > -1) {
                MailHelper.sendWarningMail(it.action!!, it.toString())
            }
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
            var bankuai = group.substring(1, group.length - 1)
            if (bankuai.length > 4) {
                bankuai = bankuai.substring(bankuai.length - 4, bankuai.length)
            }
            bankuaiList.add(bankuai)
        }
        if (bankuaiList.size == 0) {
            val startIndex = s.indexOf("%") + 1
            bankuaiList.add(s.substring(startIndex, startIndex+2))
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

            val somedaysAgo = TimeUtil.beforeNowYMDHMSTime(-60)
            try {

                var like = "CATEGORY_ like '%$bankuai'"
                var sql = "SELECT STOCK_NAME,count(*) as c FROM CARE_ where STOCK_NAME in(" +
                    " select DISTINCT STOCK_NAME FROM CARE_ where " + like +
                    " and LOG_TIME >='" + somedaysAgo + "'" + ")" +
                    " and LOG_TIME >='" + somedaysAgo + "'" +
                    " GROUP BY STOCK_NAME ORDER BY c DESC limit 2"
                LogUtil.d(TAG, "searchMaybeBought() sql = [$sql]")
                var readableDatabase = OrmLite.getInstance().readableDatabase
                var cursor = readableDatabase.rawQuery(sql, null)
                if (cursor.count < 1) {
                    like = StringUtil.genLike("CATEGORY_", bankuai)
                    sql = "SELECT STOCK_NAME,count(*) as c FROM CARE_ where STOCK_NAME in(" +
                        " select DISTINCT STOCK_NAME FROM CARE_ where " + like +
                        " and LOG_TIME >='" + somedaysAgo + "'" + ")" +
                        " and LOG_TIME >='" + somedaysAgo + "'" +
                        " GROUP BY STOCK_NAME ORDER BY c DESC limit 2"
                    LogUtil.d(TAG, "searchMaybeBought() sql2 = [$sql]")
                    readableDatabase = OrmLite.getInstance().readableDatabase
                    cursor = readableDatabase.rawQuery(sql, null)
                }

                LogUtil.d(TAG, "searchMaybeBought() size = [${cursor.count}]")
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
            val cnNames = p.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
        const val weibourl = "https://www.weibo.com/wu2198?profile_ftype=1&is_all=1#_0"
        const val weiboDivClass = "WB_detail"
        const val CONTENT_ID = "sina_keyword_ad_area2"
        val BUY_REG = arrayOf("买入.*%", "买.*%", "买进.*%", "增持.*%", "增仓.*%", "回补.*%", "加仓.*%", "接回.*%")
        val SELL_REG = arrayOf("卖出.*%", "卖.*%", "兑现.*%", "T出.*%", "T掉.*%", "减仓.*%", "走了.*%", "走掉.*%", "砍掉.*%", "割掉.*%",
            "减掉" +
                ".*%")
        val SPACE_REG = arrayOf("目前.*帐户.?.?.?.?%", ".*帐户.*%", "仓位是零", "仓位暂时是零", "零仓位")
        val CARE_REG = arrayOf("领先.*股", "领先.*板", "等.*股继续领先", "冲涨停", "目前具有上涨", "目前涨停", "目前领先", "出现涨停", "改写了新高", "等领先")

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
