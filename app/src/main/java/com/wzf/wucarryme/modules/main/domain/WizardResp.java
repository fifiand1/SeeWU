package com.wzf.wucarryme.modules.main.domain;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Wzf on 2018/5/8
 */
public class WizardResp {
    /**
     * success : true
     * data : [{"stockCode":"002870","stockName":"香山股份","codeType":"4614","marketType":"hs"},{"stockCode":"600291",
     * "stockName":"西水股份","codeType":"4353","marketType":"hs"},{"stockCode":"600540","stockName":"新赛股份",
     * "codeType":"4353","marketType":"hs"},{"stockCode":"603305","stockName":"旭升股份","codeType":"4353",
     * "marketType":"hs"},{"stockCode":"834112","stockName":"兴塑股份","codeType":"7176","marketType":"hs"},
     * {"stockCode":"837094","stockName":"旭晟股份","codeType":"7176","marketType":"hs"},{"stockCode":"837184",
     * "stockName":"新时股份","codeType":"7176","marketType":"hs"},{"stockCode":"870867","stockName":"鑫盛股份",
     * "codeType":"7176","marketType":"hs"}]
     */

    private boolean success;
    private ConcurrentLinkedQueue<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ConcurrentLinkedQueue<DataBean> getData() {
        return data;
    }

    public void setData(ConcurrentLinkedQueue<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * stockCode : 002870
         * stockName : 香山股份
         * codeType : 4614
         * marketType : hs
         */

        private String stockCode;
        private String stockName;
        private String codeType;
        private String marketType;

        public String getStockCode() {
            return stockCode;
        }

        public void setStockCode(String stockCode) {
            this.stockCode = stockCode;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getMarketType() {
            return marketType;
        }

        public void setMarketType(String marketType) {
            this.marketType = marketType;
        }
    }
    //{"success":true,"data":[{"stockCode":"002870","stockName":"香山股份","codeType":"4614","marketType":"hs"},
    // {"stockCode":"600291","stockName":"西水股份","codeType":"4353","marketType":"hs"},{"stockCode":"600540",
    // "stockName":"新赛股份","codeType":"4353","marketType":"hs"},{"stockCode":"603305","stockName":"旭升股份",
    // "codeType":"4353","marketType":"hs"},{"stockCode":"834112","stockName":"兴塑股份","codeType":"7176",
    // "marketType":"hs"},{"stockCode":"837094","stockName":"旭晟股份","codeType":"7176","marketType":"hs"},
    // {"stockCode":"837184","stockName":"新时股份","codeType":"7176","marketType":"hs"},{"stockCode":"870867",
    // "stockName":"鑫盛股份","codeType":"7176","marketType":"hs"}]}

}
