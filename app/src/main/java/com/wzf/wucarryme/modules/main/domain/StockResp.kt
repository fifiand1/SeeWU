package com.wzf.wucarryme.modules.main.domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author wzf
 * @date 2018/2/7
 */

class StockResp constructor() : Parcelable {


    /**
     * success : true
     * data : [{"code":"300628","codeType":"4621","stockName":"亿联网络","newPrice":"102.07","risePrice":"0.0257",
     * "prevClose":"99.51","open":"102.39","maxPrice":"102.83","minPrice":"98.800","totalHand":"629732",
     * "sellPrice5":"102.15","sellPrice4":"102.14","sellPrice3":"102.100","sellPrice2":"102.09",
     * "sellPrice1":"102.08","sellCount5":"11","sellCount4":"1","sellCount3":"8","sellCount2":"2","sellCount1":"2",
     * "buyPrice1":"101.500","buyPrice2":"101.31","buyPrice3":"101.300","buyPrice4":"100.58","buyPrice5":"100.51",
     * "buyCount1":"19","buyCount2":"4","buyCount3":"5","buyCount4":"7","buyCount5":"100","upPrice":"109.46",
     * "downPrice":"89.56","weiBi":"0.6981","weiCha":"111","inside":"359512","outside":"270220","swing":"0.0405",
     * "totalAmount":"6.3366772E7","hand":"1.69","shares_per_hand":"100","marketType":"hs","stopFlag":"0"},
     * {"code":"002460","codeType":"4614","stockName":"赣锋锂业","newPrice":"53.000","risePrice":"-0.0093",
     * "prevClose":"53.500","open":"55.500","maxPrice":"55.500","minPrice":"50.18","totalHand":"16333563",
     * "sellPrice5":"53.17","sellPrice4":"53.15","sellPrice3":"53.14","sellPrice2":"53.12","sellPrice1":"53.100",
     * "sellCount5":"1","sellCount4":"2","sellCount3":"5","sellCount2":"11","sellCount1":"21","buyPrice1":"53.04",
     * "buyPrice2":"53.01","buyPrice3":"53.000","buyPrice4":"52.99","buyPrice5":"52.98","buyCount1":"8",
     * "buyCount2":"7","buyCount3":"3","buyCount4":"3","buyCount5":"4","upPrice":"58.85","downPrice":"48.15",
     * "weiBi":"-0.2308","weiCha":"-15","inside":"8045531","outside":"8288032","swing":"0.0994",
     * "totalAmount":"8.627072E8","hand":"3.14","shares_per_hand":"100","marketType":"hs","stopFlag":"0"},
     * {"code":"2A01","codeType":"4608","stockName":"深证成指","newPrice":"10370.58","risePrice":"-7.0E-4",
     * "prevClose":"10377.609","open":"10563.313","maxPrice":"10599.536","minPrice":"10173.662",
     * "totalHand":"120942816","sellPrice5":"0.000","sellPrice4":"0.000","sellPrice3":"0.000","sellPrice2":"0.000",
     * "sellPrice1":"0.000","sellCount5":"0","sellCount4":"0","sellCount3":"0","sellCount2":"0","sellCount1":"0",
     * "buyPrice1":"0.000","buyPrice2":"0.000","buyPrice3":"0.000","buyPrice4":"0.000","buyPrice5":"0.000",
     * "buyCount1":"0","buyCount2":"0","buyCount3":"0","buyCount4":"0","buyCount5":"0","upPrice":"0.000",
     * "downPrice":"0.000","weiBi":"0.0000","weiCha":"0","inside":"60471408","outside":"60471408","swing":"0.041",
     * "totalAmount":"1.47732283E11","hand":null,"shares_per_hand":"1","marketType":"zs","stopFlag":null},
     * {"code":"1A0001","codeType":"4352","stockName":"上证指数","newPrice":"3343.44","risePrice":"-0.0081",
     * "prevClose":"3370.652","open":"3412.744","maxPrice":"3425.537","minPrice":"3315.683","totalHand":"164040432",
     * "sellPrice5":"0.000","sellPrice4":"0.000","sellPrice3":"0.000","sellPrice2":"0.000","sellPrice1":"0.000",
     * "sellCount5":"0","sellCount4":"0","sellCount3":"0","sellCount2":"0","sellCount1":"0","buyPrice1":"0.000",
     * "buyPrice2":"0.000","buyPrice3":"0.000","buyPrice4":"0.000","buyPrice5":"0.000","buyCount1":"0",
     * "buyCount2":"0","buyCount3":"0","buyCount4":"0","buyCount5":"0","upPrice":"0.000","downPrice":"0.000",
     * "weiBi":"0.0000","weiCha":"0","inside":"82020216","outside":"82020216","swing":"0.0326",
     * "totalAmount":"1.82784672E11","hand":null,"shares_per_hand":"1","marketType":"zs","stopFlag":null}]
     */

    var success: Boolean = false
    lateinit var data: List<DataBean>

    constructor(parcel: Parcel) : this() {
        success = parcel.readByte() != 0.toByte()
        data = parcel.createTypedArrayList(DataBean.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (success) 1 else 0).toByte())
        dest.writeTypedList(data)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("success: ").append(success).append(", data size: ").append(data.size).append("\n")
        for (datum in data) {
            sb.append(datum.toString()).append("\n")
        }
        return sb.toString()
    }

    class DataBean : Parcelable {

        /**
         * code : 300628
         * codeType : 4621
         * stockName : 亿联网络
         * newPrice : 102.07
         * risePrice : 0.0257
         * prevClose : 99.51
         * open : 102.39
         * maxPrice : 102.83
         * minPrice : 98.800
         * totalHand : 629732
         * sellPrice5 : 102.15
         * sellPrice4 : 102.14
         * sellPrice3 : 102.100
         * sellPrice2 : 102.09
         * sellPrice1 : 102.08
         * sellCount5 : 11
         * sellCount4 : 1
         * sellCount3 : 8
         * sellCount2 : 2
         * sellCount1 : 2
         * buyPrice1 : 101.500
         * buyPrice2 : 101.31
         * buyPrice3 : 101.300
         * buyPrice4 : 100.58
         * buyPrice5 : 100.51
         * buyCount1 : 19
         * buyCount2 : 4
         * buyCount3 : 5
         * buyCount4 : 7
         * buyCount5 : 100
         * upPrice : 109.46
         * downPrice : 89.56
         * weiBi : 0.6981
         * weiCha : 111
         * inside : 359512
         * outside : 270220
         * swing : 0.0405
         * totalAmount : 6.3366772E7
         * hand : 1.69
         * shares_per_hand : 100
         * marketType : hs
         * stopFlag : 0
         */

        var code: String? = null
        var codeType: String? = null
        var stockName: String? = null
        var newPrice: String? = null
        var risePrice: String? = null
        var prevClose: String? = null
        var open: String? = null
        var maxPrice: String? = null
        var minPrice: String? = null
        var totalHand: String? = null
        var sellPrice5: String? = null
        var sellPrice4: String? = null
        var sellPrice3: String? = null
        var sellPrice2: String? = null
        var sellPrice1: String? = null
        var sellCount5: String? = null
        var sellCount4: String? = null
        var sellCount3: String? = null
        var sellCount2: String? = null
        var sellCount1: String? = null
        var buyPrice1: String? = null
        var buyPrice2: String? = null
        var buyPrice3: String? = null
        var buyPrice4: String? = null
        var buyPrice5: String? = null
        var buyCount1: String? = null
        var buyCount2: String? = null
        var buyCount3: String? = null
        var buyCount4: String? = null
        var buyCount5: String? = null
        var upPrice: String? = null
        var downPrice: String? = null
        var weiBi: String? = null
        var weiCha: String? = null
        var inside: String? = null
        var outside: String? = null
        var swing: String? = null
        var totalAmount: String? = null
        var hand: String? = null
        var shares_per_hand: String? = null
        var marketType: String? = null
        var stopFlag: String? = null

        val formattedRise: String?
            get() {
                if (risePrice == null) {
                    return null
                }
                val start = if (risePrice!!.startsWith("-")) "" else "+"
                return start + String.format(Locale.CHINA,
                    "%.2f",
                    java.lang.Float.parseFloat(if (risePrice == null) "-99.00" else risePrice) * 100) + "%"
            }

        constructor(parcel: Parcel) {
            code = parcel.readString()
            codeType = parcel.readString()
            stockName = parcel.readString()
            newPrice = parcel.readString()
            risePrice = parcel.readString()
            prevClose = parcel.readString()
            open = parcel.readString()
            maxPrice = parcel.readString()
            minPrice = parcel.readString()
            totalHand = parcel.readString()
            sellPrice5 = parcel.readString()
            sellPrice4 = parcel.readString()
            sellPrice3 = parcel.readString()
            sellPrice2 = parcel.readString()
            sellPrice1 = parcel.readString()
            sellCount5 = parcel.readString()
            sellCount4 = parcel.readString()
            sellCount3 = parcel.readString()
            sellCount2 = parcel.readString()
            sellCount1 = parcel.readString()
            buyPrice1 = parcel.readString()
            buyPrice2 = parcel.readString()
            buyPrice3 = parcel.readString()
            buyPrice4 = parcel.readString()
            buyPrice5 = parcel.readString()
            buyCount1 = parcel.readString()
            buyCount2 = parcel.readString()
            buyCount3 = parcel.readString()
            buyCount4 = parcel.readString()
            buyCount5 = parcel.readString()
            upPrice = parcel.readString()
            downPrice = parcel.readString()
            weiBi = parcel.readString()
            weiCha = parcel.readString()
            inside = parcel.readString()
            outside = parcel.readString()
            swing = parcel.readString()
            totalAmount = parcel.readString()
            hand = parcel.readString()
            shares_per_hand = parcel.readString()
            marketType = parcel.readString()
            stopFlag = parcel.readString()
        }

        constructor()


        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(code)
            dest.writeString(codeType)
            dest.writeString(stockName)
            dest.writeString(newPrice)
            dest.writeString(risePrice)
            dest.writeString(prevClose)
            dest.writeString(open)
            dest.writeString(maxPrice)
            dest.writeString(minPrice)
            dest.writeString(totalHand)
            dest.writeString(sellPrice5)
            dest.writeString(sellPrice4)
            dest.writeString(sellPrice3)
            dest.writeString(sellPrice2)
            dest.writeString(sellPrice1)
            dest.writeString(sellCount5)
            dest.writeString(sellCount4)
            dest.writeString(sellCount3)
            dest.writeString(sellCount2)
            dest.writeString(sellCount1)
            dest.writeString(buyPrice1)
            dest.writeString(buyPrice2)
            dest.writeString(buyPrice3)
            dest.writeString(buyPrice4)
            dest.writeString(buyPrice5)
            dest.writeString(buyCount1)
            dest.writeString(buyCount2)
            dest.writeString(buyCount3)
            dest.writeString(buyCount4)
            dest.writeString(buyCount5)
            dest.writeString(upPrice)
            dest.writeString(downPrice)
            dest.writeString(weiBi)
            dest.writeString(weiCha)
            dest.writeString(inside)
            dest.writeString(outside)
            dest.writeString(swing)
            dest.writeString(totalAmount)
            dest.writeString(hand)
            dest.writeString(shares_per_hand)
            dest.writeString(marketType)
            dest.writeString(stopFlag)
        }

        override fun toString(): String {
            /*
             * stockName : 亿联网络
             * newPrice : 102.07
             * risePrice : 0.0257*/
            return stockName + "\t" + newPrice + "\t" + formattedRise
        }

        override fun equals(other: Any?): Boolean {
            return other is DataBean && other.stockName == this.stockName
        }

        override fun hashCode(): Int {
            var result = code?.hashCode() ?: 0
            result = 31 * result + (codeType?.hashCode() ?: 0)
            result = 31 * result + (stockName?.hashCode() ?: 0)
            result = 31 * result + (newPrice?.hashCode() ?: 0)
            result = 31 * result + (risePrice?.hashCode() ?: 0)
            result = 31 * result + (prevClose?.hashCode() ?: 0)
            result = 31 * result + (open?.hashCode() ?: 0)
            result = 31 * result + (maxPrice?.hashCode() ?: 0)
            result = 31 * result + (minPrice?.hashCode() ?: 0)
            result = 31 * result + (totalHand?.hashCode() ?: 0)
            result = 31 * result + (sellPrice5?.hashCode() ?: 0)
            result = 31 * result + (sellPrice4?.hashCode() ?: 0)
            result = 31 * result + (sellPrice3?.hashCode() ?: 0)
            result = 31 * result + (sellPrice2?.hashCode() ?: 0)
            result = 31 * result + (sellPrice1?.hashCode() ?: 0)
            result = 31 * result + (sellCount5?.hashCode() ?: 0)
            result = 31 * result + (sellCount4?.hashCode() ?: 0)
            result = 31 * result + (sellCount3?.hashCode() ?: 0)
            result = 31 * result + (sellCount2?.hashCode() ?: 0)
            result = 31 * result + (sellCount1?.hashCode() ?: 0)
            result = 31 * result + (buyPrice1?.hashCode() ?: 0)
            result = 31 * result + (buyPrice2?.hashCode() ?: 0)
            result = 31 * result + (buyPrice3?.hashCode() ?: 0)
            result = 31 * result + (buyPrice4?.hashCode() ?: 0)
            result = 31 * result + (buyPrice5?.hashCode() ?: 0)
            result = 31 * result + (buyCount1?.hashCode() ?: 0)
            result = 31 * result + (buyCount2?.hashCode() ?: 0)
            result = 31 * result + (buyCount3?.hashCode() ?: 0)
            result = 31 * result + (buyCount4?.hashCode() ?: 0)
            result = 31 * result + (buyCount5?.hashCode() ?: 0)
            result = 31 * result + (upPrice?.hashCode() ?: 0)
            result = 31 * result + (downPrice?.hashCode() ?: 0)
            result = 31 * result + (weiBi?.hashCode() ?: 0)
            result = 31 * result + (weiCha?.hashCode() ?: 0)
            result = 31 * result + (inside?.hashCode() ?: 0)
            result = 31 * result + (outside?.hashCode() ?: 0)
            result = 31 * result + (swing?.hashCode() ?: 0)
            result = 31 * result + (totalAmount?.hashCode() ?: 0)
            result = 31 * result + (hand?.hashCode() ?: 0)
            result = 31 * result + (shares_per_hand?.hashCode() ?: 0)
            result = 31 * result + (marketType?.hashCode() ?: 0)
            result = 31 * result + (stopFlag?.hashCode() ?: 0)
            return result
        }

        companion object CREATOR : Parcelable.Creator<DataBean> {
            override fun createFromParcel(parcel: Parcel): DataBean {
                return DataBean(parcel)
            }

            override fun newArray(size: Int): Array<DataBean?> {
                return arrayOfNulls(size)
            }
        }

    }

    companion object CREATOR : Parcelable.Creator<StockResp> {
        override fun createFromParcel(parcel: Parcel): StockResp {
            return StockResp(parcel)
        }

        override fun newArray(size: Int): Array<StockResp?> {
            return arrayOfNulls(size)
        }
    }

}
