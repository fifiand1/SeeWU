package com.wzf.wucarryme.modules.main.domain;

import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wzf
 * @date 2018/2/7
 */

public class StockResp implements Parcelable {


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

    private boolean success;
    private List<DataBean> data;

    protected StockResp(Parcel in) {
        success = in.readByte() != 0;
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<StockResp> CREATOR = new Creator<StockResp>() {
        @Override
        public StockResp createFromParcel(Parcel in) {
            return new StockResp(in);
        }

        @Override
        public StockResp[] newArray(int size) {
            return new StockResp[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DataBean datum : data) {
            sb.append(datum.toString()).append("\n");
        }
        return sb.toString();
    }

    public static class DataBean implements Parcelable {
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

        private String code;
        private String codeType;
        private String stockName;
        private String newPrice;
        private String risePrice = "-99";
        private String prevClose;
        private String open;
        private String maxPrice;
        private String minPrice;
        private String totalHand;
        private String sellPrice5;
        private String sellPrice4;
        private String sellPrice3;
        private String sellPrice2;
        private String sellPrice1;
        private String sellCount5;
        private String sellCount4;
        private String sellCount3;
        private String sellCount2;
        private String sellCount1;
        private String buyPrice1;
        private String buyPrice2;
        private String buyPrice3;
        private String buyPrice4;
        private String buyPrice5;
        private String buyCount1;
        private String buyCount2;
        private String buyCount3;
        private String buyCount4;
        private String buyCount5;
        private String upPrice;
        private String downPrice;
        private String weiBi;
        private String weiCha;
        private String inside;
        private String outside;
        private String swing;
        private String totalAmount;
        private String hand;
        private String shares_per_hand;
        private String marketType;
        private String stopFlag;

        public DataBean(){

        }

        protected DataBean(Parcel in) {
            code = in.readString();
            codeType = in.readString();
            stockName = in.readString();
            newPrice = in.readString();
            risePrice = in.readString();
            prevClose = in.readString();
            open = in.readString();
            maxPrice = in.readString();
            minPrice = in.readString();
            totalHand = in.readString();
            sellPrice5 = in.readString();
            sellPrice4 = in.readString();
            sellPrice3 = in.readString();
            sellPrice2 = in.readString();
            sellPrice1 = in.readString();
            sellCount5 = in.readString();
            sellCount4 = in.readString();
            sellCount3 = in.readString();
            sellCount2 = in.readString();
            sellCount1 = in.readString();
            buyPrice1 = in.readString();
            buyPrice2 = in.readString();
            buyPrice3 = in.readString();
            buyPrice4 = in.readString();
            buyPrice5 = in.readString();
            buyCount1 = in.readString();
            buyCount2 = in.readString();
            buyCount3 = in.readString();
            buyCount4 = in.readString();
            buyCount5 = in.readString();
            upPrice = in.readString();
            downPrice = in.readString();
            weiBi = in.readString();
            weiCha = in.readString();
            inside = in.readString();
            outside = in.readString();
            swing = in.readString();
            totalAmount = in.readString();
            hand = in.readString();
            shares_per_hand = in.readString();
            marketType = in.readString();
            stopFlag = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getNewPrice() {
            return newPrice;
        }

        public void setNewPrice(String newPrice) {
            this.newPrice = newPrice;
        }

        public String getRisePrice() {
            return risePrice;
        }

        public void setRisePrice(String risePrice) {
            this.risePrice = risePrice;
        }

        public String getPrevClose() {
            return prevClose;
        }

        public void setPrevClose(String prevClose) {
            this.prevClose = prevClose;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getTotalHand() {
            return totalHand;
        }

        public void setTotalHand(String totalHand) {
            this.totalHand = totalHand;
        }

        public String getSellPrice5() {
            return sellPrice5;
        }

        public void setSellPrice5(String sellPrice5) {
            this.sellPrice5 = sellPrice5;
        }

        public String getSellPrice4() {
            return sellPrice4;
        }

        public void setSellPrice4(String sellPrice4) {
            this.sellPrice4 = sellPrice4;
        }

        public String getSellPrice3() {
            return sellPrice3;
        }

        public void setSellPrice3(String sellPrice3) {
            this.sellPrice3 = sellPrice3;
        }

        public String getSellPrice2() {
            return sellPrice2;
        }

        public void setSellPrice2(String sellPrice2) {
            this.sellPrice2 = sellPrice2;
        }

        public String getSellPrice1() {
            return sellPrice1;
        }

        public void setSellPrice1(String sellPrice1) {
            this.sellPrice1 = sellPrice1;
        }

        public String getSellCount5() {
            return sellCount5;
        }

        public void setSellCount5(String sellCount5) {
            this.sellCount5 = sellCount5;
        }

        public String getSellCount4() {
            return sellCount4;
        }

        public void setSellCount4(String sellCount4) {
            this.sellCount4 = sellCount4;
        }

        public String getSellCount3() {
            return sellCount3;
        }

        public void setSellCount3(String sellCount3) {
            this.sellCount3 = sellCount3;
        }

        public String getSellCount2() {
            return sellCount2;
        }

        public void setSellCount2(String sellCount2) {
            this.sellCount2 = sellCount2;
        }

        public String getSellCount1() {
            return sellCount1;
        }

        public void setSellCount1(String sellCount1) {
            this.sellCount1 = sellCount1;
        }

        public String getBuyPrice1() {
            return buyPrice1;
        }

        public void setBuyPrice1(String buyPrice1) {
            this.buyPrice1 = buyPrice1;
        }

        public String getBuyPrice2() {
            return buyPrice2;
        }

        public void setBuyPrice2(String buyPrice2) {
            this.buyPrice2 = buyPrice2;
        }

        public String getBuyPrice3() {
            return buyPrice3;
        }

        public void setBuyPrice3(String buyPrice3) {
            this.buyPrice3 = buyPrice3;
        }

        public String getBuyPrice4() {
            return buyPrice4;
        }

        public void setBuyPrice4(String buyPrice4) {
            this.buyPrice4 = buyPrice4;
        }

        public String getBuyPrice5() {
            return buyPrice5;
        }

        public void setBuyPrice5(String buyPrice5) {
            this.buyPrice5 = buyPrice5;
        }

        public String getBuyCount1() {
            return buyCount1;
        }

        public void setBuyCount1(String buyCount1) {
            this.buyCount1 = buyCount1;
        }

        public String getBuyCount2() {
            return buyCount2;
        }

        public void setBuyCount2(String buyCount2) {
            this.buyCount2 = buyCount2;
        }

        public String getBuyCount3() {
            return buyCount3;
        }

        public void setBuyCount3(String buyCount3) {
            this.buyCount3 = buyCount3;
        }

        public String getBuyCount4() {
            return buyCount4;
        }

        public void setBuyCount4(String buyCount4) {
            this.buyCount4 = buyCount4;
        }

        public String getBuyCount5() {
            return buyCount5;
        }

        public void setBuyCount5(String buyCount5) {
            this.buyCount5 = buyCount5;
        }

        public String getUpPrice() {
            return upPrice;
        }

        public void setUpPrice(String upPrice) {
            this.upPrice = upPrice;
        }

        public String getDownPrice() {
            return downPrice;
        }

        public void setDownPrice(String downPrice) {
            this.downPrice = downPrice;
        }

        public String getWeiBi() {
            return weiBi;
        }

        public void setWeiBi(String weiBi) {
            this.weiBi = weiBi;
        }

        public String getWeiCha() {
            return weiCha;
        }

        public void setWeiCha(String weiCha) {
            this.weiCha = weiCha;
        }

        public String getInside() {
            return inside;
        }

        public void setInside(String inside) {
            this.inside = inside;
        }

        public String getOutside() {
            return outside;
        }

        public void setOutside(String outside) {
            this.outside = outside;
        }

        public String getSwing() {
            return swing;
        }

        public void setSwing(String swing) {
            this.swing = swing;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getHand() {
            return hand;
        }

        public void setHand(String hand) {
            this.hand = hand;
        }

        public String getShares_per_hand() {
            return shares_per_hand;
        }

        public void setShares_per_hand(String shares_per_hand) {
            this.shares_per_hand = shares_per_hand;
        }

        public String getMarketType() {
            return marketType;
        }

        public void setMarketType(String marketType) {
            this.marketType = marketType;
        }

        public String getStopFlag() {
            return stopFlag;
        }

        public void setStopFlag(String stopFlag) {
            this.stopFlag = stopFlag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(code);
            dest.writeString(codeType);
            dest.writeString(stockName);
            dest.writeString(newPrice);
            dest.writeString(risePrice);
            dest.writeString(prevClose);
            dest.writeString(open);
            dest.writeString(maxPrice);
            dest.writeString(minPrice);
            dest.writeString(totalHand);
            dest.writeString(sellPrice5);
            dest.writeString(sellPrice4);
            dest.writeString(sellPrice3);
            dest.writeString(sellPrice2);
            dest.writeString(sellPrice1);
            dest.writeString(sellCount5);
            dest.writeString(sellCount4);
            dest.writeString(sellCount3);
            dest.writeString(sellCount2);
            dest.writeString(sellCount1);
            dest.writeString(buyPrice1);
            dest.writeString(buyPrice2);
            dest.writeString(buyPrice3);
            dest.writeString(buyPrice4);
            dest.writeString(buyPrice5);
            dest.writeString(buyCount1);
            dest.writeString(buyCount2);
            dest.writeString(buyCount3);
            dest.writeString(buyCount4);
            dest.writeString(buyCount5);
            dest.writeString(upPrice);
            dest.writeString(downPrice);
            dest.writeString(weiBi);
            dest.writeString(weiCha);
            dest.writeString(inside);
            dest.writeString(outside);
            dest.writeString(swing);
            dest.writeString(totalAmount);
            dest.writeString(hand);
            dest.writeString(shares_per_hand);
            dest.writeString(marketType);
            dest.writeString(stopFlag);
        }

        @Override
        public String toString() {
            /*
             * stockName : 亿联网络
             * newPrice : 102.07
             * risePrice : 0.0257*/
            return stockName + "\t" + newPrice + "\t" + getFormattedPrice();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DataBean && ((DataBean) obj).getStockName().equals(this.stockName);
        }

        public String getFormattedPrice() {
            String start = risePrice.startsWith("-") ? "" : "+";
            return start + String.format(Locale.CHINA, "%.2f", (Float.parseFloat
                (risePrice == null ? "-99.00" : risePrice) * 100)) + "%";
        }
    }
}
