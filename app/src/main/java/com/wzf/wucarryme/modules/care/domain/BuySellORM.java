package com.wzf.wucarryme.modules.care.domain;


import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * @author wzf
 * @date 2018/3/22
 */
@Table("BUY_SELL")
public class BuySellORM {
    @Column("ID_")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @NotNull
    @Column("BLOG_TIME")
    private String blogTime;
    @Column("CATEGORY_")
    private String category;
    @NotNull
    @Column("ACTION_")
    private String action;
    @Column("STOCK1_")
    private String stock1;
    @Column("STOCK2_")
    private String stock2;
    @Column("DESC_")
    private String desc;
    @Column("LOG_TIME")
    private String logTime;
    @Ignore
    private int count1;
    @Ignore
    private int count2;

    public BuySellORM(String blogTime, String category, String action, String stock1, String stock2, String desc,
                      String logTime) {
        this.blogTime = blogTime;
        this.category = category;
        this.action = action;
        this.stock1 = stock1;
        this.stock2 = stock2;
        this.desc = desc;
        this.logTime = logTime;
    }

    public BuySellORM(String blogTime, String logTime) {
        this.blogTime = blogTime;
        this.logTime = logTime;
    }

    public BuySellORM() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStock1() {
        return stock1;
    }

    public void setStock1(String stock1) {
        this.stock1 = stock1;
    }

    public String getStock2() {
        return stock2;
    }

    public void setStock2(String stock2) {
        this.stock2 = stock2;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }
}
