package com.wzf.wucarryme.modules.care.domain;


import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * @author wzf
 * @date 2018/3/22
 */
@Table("CARE_")
public class CareORM {
    @Column("ID_")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @NotNull
    @Column("BLOG_TIME")
    private String time;
    @Column("CATEGORY_")
    private String category;
    @Column("STOCK_NAME")
    private String stockName;
    @Column("DESC_")
    private String desc;
    @Column("LOG_TIME")
    private String logTime;

    public CareORM(String time, String category, String stockName, String desc, String logTime) {
        this.time = time;
        this.category = category;
        this.stockName = stockName;
        this.desc = desc;
        this.logTime = logTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
