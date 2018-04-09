package com.wzf.wucarryme.modules.care.domain


import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.Ignore
import com.litesuits.orm.db.annotation.NotNull
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

/**
 * @author wzf
 * @date 2018/3/22
 */
@Table("BUY_SELL")
class BuySellORM {
    @Column("ID_")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    var id: Int = 0
    @NotNull
    @Column("BLOG_TIME")
    var blogTime: String? = null
    @Column("CATEGORY_")
    var category: String? = null
    @NotNull
    @Column("ACTION_")
    var action: String? = null
    @Column("STOCK1_")
    var stock1: String? = null
    @Column("STOCK2_")
    var stock2: String? = null
    @Column("DESC_")
    var desc: String? = null
    @Column("LOG_TIME")
    var logTime: String? = null
    @Ignore
    var count1: Int = 0
    @Ignore
    var count2: Int = 0

    constructor(blogTime: String, category: String, action: String, stock1: String, stock2: String, desc: String,
                logTime: String) {
        this.blogTime = blogTime
        this.category = category
        this.action = action
        this.stock1 = stock1
        this.stock2 = stock2
        this.desc = desc
        this.logTime = logTime
    }

    constructor(blogTime: String, logTime: String) {
        this.blogTime = blogTime
        this.logTime = logTime
    }

    constructor() {

    }
}
