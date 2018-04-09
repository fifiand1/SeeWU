package com.wzf.wucarryme.modules.care.domain


import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.NotNull
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

/**
 * @author wzf
 * @date 2018/3/22
 */
@Table("CARE_")
class CareORM(@field:NotNull
              @field:Column("BLOG_TIME")
              var time: String?, @field:Column("CATEGORY_")
              var category: String?, @field:Column("STOCK_NAME")
              var stockName: String?, @field:Column("DESC_")
              var desc: String?, @field:Column("LOG_TIME")
              var logTime: String?) {
    @Column("ID_")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    var id: Int = 0
}
