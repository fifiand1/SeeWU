package com.wzf.wucarryme.modules.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wzf.wucarryme.common.utils.LogUtil;
import com.wzf.wucarryme.common.utils.SharedPreferenceUtil;
import com.wzf.wucarryme.common.utils.StringUtil;
import com.wzf.wucarryme.common.utils.TimeUtil;
import com.wzf.wucarryme.component.NotificationHelper;
import com.wzf.wucarryme.component.OrmLite;
import com.wzf.wucarryme.modules.care.domain.BuySellORM;
import com.wzf.wucarryme.modules.care.domain.CareORM;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.wzf.wucarryme.common.utils.StringUtil.banKuai;

public class CollectorService extends Service {
    private final String TAG = CollectorService.class.getSimpleName();
    private Disposable mDisposable;
    private boolean mIsUnSubscribed = true;


    public static final String TITLE = "wu2198股市直播";
    static final String url = "http://blog.sina.com.cn/u/1216826604";
    public static final String CONTENT_ID = "sina_keyword_ad_area2";
    public static final String[] BUY_REG = {"买入.*%", "买.*%", "买进.*%", "增持.*%", "增仓.*%", "回补.*%"};
    public static final String[] SELL_REG = {"卖出.*%", "卖.*%", "兑现.*%", "T出.*%", "减仓.*%", "走了.*%", "走掉.*%", "砍掉.*%",
        "减掉.*%"};
    public static final String[] SPACE_REG = {"目前.*帐户.?.?.?.?%", ".*帐户.?.?.?.?%", "目前.?.?.?.?%", "现在.?.?.?.?%"};
    public static final String[] CARE_REG = {"领先.*股", "出现冲涨停", "目前具有上涨", "改写了新高"};

    public static final String[] collectDate = {};
    public static final String[] collectURL = {};
    //可能关心的,例如xxxx,xxxx等领先xx股
    public static final String TYPE_CARE = "CARE";
    //买入
    public static final String TYPE_BUY = "BUY";
    //卖出
    public static final String TYPE_SELL = "SELL";
    //持仓
    public static final String TYPE_POSITION = "POSITION";

    String todayDate = "";
    String todayURL = "";
    Document doc;
    List<String> storedBought = new ArrayList<>();
    List<String> storedSold = new ArrayList<>();
    List<String> storedSpace = new ArrayList<>();
    List<String> storedCare = new ArrayList<>();
    List<String> storedNormal = new ArrayList<>();

    //6月2日10:00

    public CollectorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unSubscribed();
            if (mIsUnSubscribed) {
                unSubscribed();
                int autoRefresh = SharedPreferenceUtil.getInstance().getBlogAutoRefresh();
                if (autoRefresh != 0) {
                    mDisposable = Observable.interval(0, autoRefresh, TimeUnit.MINUTES)
                        .observeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            LogUtil.d(TAG, "jsoup TodayURL " + Thread.currentThread().getName());
                            jsoupTodayURL();
                        })
                        .doOnNext(aLong -> {
                            LogUtil.d(TAG, "jsoup Article " + Thread.currentThread().getName());
                            mIsUnSubscribed = false;
                            if (TimeUtil.isKP()) {
                                jsoupArticle();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    private void jsoupTodayURL() {
        try {
            todayDate = TimeUtil.getNowYueRi();
//            todayDate = "3月22日";
            doc = Jsoup.connect(url).get();
            Elements elementsByAttributeValue = doc.getElementsByAttributeValue("class", "blog_title_h");
            for (Element element : elementsByAttributeValue) {
                Elements a = element.getElementsByTag("a");
                Element element1 = a.get(0);
                String data = element1.text();

                //5月17日wu2198股市直播
                if (data.equals(todayDate + TITLE)) {
                    todayURL = element1.attr("href");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "URL " + todayURL);
    }

    private void jsoupArticle() {
        try {

            doc = Jsoup.connect(todayURL).get();
            Element content = doc.getElementById(CONTENT_ID);
            Elements p = content.getElementsByTag("p");
            for (Element element : p) {
                String text = element.text();
//                Log.i(TAG, "get one-> " + text);

                String care = important(text, CARE_REG);
                //卖出
                if (important(text, SELL_REG) != null) {
                    if (!storedSold.contains(text)) {
                        storedSold.add(text);
                        print(TYPE_SELL, text);
                        insertExcelSELL(text);
                    }
                } else if (important(text, BUY_REG) != null) {
                    //买入
                    if (!storedBought.contains(text)) {
                        storedBought.add(text);
                        print(TYPE_BUY, text);
                        List<BuySellORM> strings = searchMaybeBought(text);
                        if (strings != null) {
                            for (BuySellORM item : strings) {
                                String x = "************************买入选择************************";
                                print(TYPE_BUY, x);
                                print(TYPE_BUY, "************************" + item.getStock1() +
                                    "************************" + item.getCount1());
                                print(TYPE_BUY, "************************" + item.getStock2() +
                                    "************************" + item.getCount2());
                                print(TYPE_BUY, x);
                            }
                        }
                        insertExcelBUY(strings, text);
                    }
                } else if (important(text, SPACE_REG) != null) {
                    //仓位
                    if (!storedSpace.contains(text)) {
                        storedSpace.add(text);
                        print(TYPE_POSITION, text);
                        insertPosition(text);
                    }
                } else if (care != null) {
                    if (!storedCare.contains(text)) {
                        storedCare.add(text);
                        print(TYPE_CARE, text);
                        insertCare(text, care);
                    }
                } else {
                    if (!storedNormal.contains(text)) {
                        storedNormal.add(text);
                        LogUtil.v(TAG, text);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertPosition(String text) {
        String important = important(text, SPACE_REG);
        String substring = important.substring(0, important.length() - 1);
        char[] chars = substring.toCharArray();
        StringBuilder position = new StringBuilder();
        if (Character.isDigit(chars[chars.length - 3])) {
            position.append(chars[chars.length - 3]);
        }
        position.append(chars[chars.length - 2]);
        position.append(chars[chars.length - 1]);

        BuySellORM buySellORM = new BuySellORM();

        String blogTime = StringUtil.getBlogTime(text);
        String nowYMDHMSTime = TimeUtil.getNowYMDHMSTime();
        buySellORM.setAction("POS");
        buySellORM.setStock1(position.toString());
        buySellORM.setBlogTime(todayDate + blogTime);
        buySellORM.setLogTime(nowYMDHMSTime);
        buySellORM.setDesc(text);
        OrmLite.getInstance().insert(buySellORM);
        NotificationHelper.showPositioningNotification(CollectorService.this, buySellORM);

    }

    private void insertExcelBUY(List<BuySellORM> strings, String p) {
        try {
            OrmLite.getInstance().insert(strings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<BuySellORM> searchMaybeBought(String s) {
        Pattern p = Pattern.compile("(%[^股]*股)");
        Matcher m = p.matcher(s);
        List<String> bankuaiList = new ArrayList<>();
        List<BuySellORM> result = new ArrayList<>();
        while (m.find()) {
            String group = m.group();
//            result.put(group.substring(1,group.length()-1),null);
            bankuaiList.add(group.substring(1, group.length() - 1));
        }
        if (bankuaiList.size() == 0) {
            bankuaiList.add(s.substring(s.indexOf("%") + 1));
        }
        String blogTime = StringUtil.getBlogTime(s);
        String nowYMDHMSTime = TimeUtil.getNowYMDHMSTime();


        for (int i = 0; i < bankuaiList.size(); i++) {
            String bankuai = bankuaiList.get(i);

            BuySellORM buySellORM = new BuySellORM();
            buySellORM.setAction("BUY");
            buySellORM.setBlogTime(todayDate + blogTime);
            buySellORM.setCategory(bankuai);
            buySellORM.setLogTime(nowYMDHMSTime);
            buySellORM.setDesc(s);
            try {
                String like = StringUtil.genLike("CATEGORY_", bankuai);
                String sql = "SELECT STOCK_NAME,count(*) as c FROM CARE_ where STOCK_NAME in(" +
                    "   select DISTINCT STOCK_NAME FROM CARE_ where " + like + ")" +
                    "GROUP BY STOCK_NAME ORDER BY c DESC limit 2";
                SQLiteDatabase readableDatabase = OrmLite.getInstance().getReadableDatabase();
                Cursor cursor = readableDatabase.rawQuery(sql, null);

                boolean success = cursor.moveToNext();
                if (success) {
                    String name = cursor.getString(0);
                    int count = cursor.getInt(1);
                    buySellORM.setStock1(name);
                    buySellORM.setCount1(count);
                }

                success = cursor.moveToNext();
                if (success) {
                    String name2 = cursor.getString(0);
                    int count2 = cursor.getInt(1);
                    buySellORM.setStock2(name2);
                    buySellORM.setCount2(count2);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(buySellORM);
            NotificationHelper.showPositioningNotification(CollectorService.this, buySellORM);
        }

        return result;
    }


    private void insertCare(String p, String group) {
        try {
            group = group.substring(2, group.length() - 1);
            String category = banKuai(group);


            String blogTime = StringUtil.getBlogTime(p);
            p = p.substring(p.indexOf(" ") + 1);
            String[] cnNames = p.split("\\.");
            List<CareORM> list = new ArrayList<>();
            String nowYMDHMSTime = TimeUtil.getNowYMDHMSTime();
            for (String cnName : cnNames) {
                cnName = StringUtil.formatCNName(cnName);

                CareORM care = new CareORM(todayDate + blogTime,
                    category, cnName, "", nowYMDHMSTime);
                list.add(care);

            }
            int result = OrmLite.getInstance().insert(list);
            Log.d(TAG, "add care: " + result);
//            long l = OrmLite.getInstance().queryCount(CareORM.class);
//            Log.d(TAG, "after care: " + l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(String type, String text) {
        Log.i(TAG, type + " -> " + text);
    }

    private void insertExcelSELL(String s) {
        Pattern p = Pattern.compile("(%[^股]*股)");
        Matcher m = p.matcher(s);
        List<String> bankuaiList = new ArrayList<>();
        while (m.find()) {
            String group = m.group();
            bankuaiList.add(banKuai(group.substring(1, group.length() - 1)));
        }

        String blogTime = StringUtil.getBlogTime(s);
        try {

            List<BuySellORM> list = new ArrayList<>();
            String nowYMDHMSTime = TimeUtil.getNowYMDHMSTime();
            for (int i = 0; i < bankuaiList.size(); i++) {
                String bankuai = bankuaiList.get(i);

                BuySellORM buySell = new BuySellORM(todayDate + blogTime,
                    bankuai, "SELL", "", "", s, nowYMDHMSTime);
                list.add(buySell);
                NotificationHelper.showPositioningNotification(CollectorService.this, buySell);
            }
            OrmLite.getInstance().insert(list);
        } catch (Exception ignored) {

        }
    }

    private String important(String p, String[] reg) {
        for (String key : reg) {
            // 编译正则表达式
            Pattern pattern = Pattern.compile(key);
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(p);
            // 查找字符串中是否有匹配正则表达式的字符/字符串
            boolean rs = matcher.find();
            if (rs) {
                return matcher.group();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void unSubscribed() {
        mIsUnSubscribed = true;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}
