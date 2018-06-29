package com.wzf.wucarryme.modules.care.domain

import com.litesuits.orm.db.annotation.*
import com.litesuits.orm.db.enums.AssignType

/**
 * @author wzf
 * @date 2018/6/29
 */
@Table("MAIL_CACHE")
class MailCacheORM(
    @field:NotNull
    @field:Column("BLOG_TIME")
    @field:UniqueCombine(1)
    var time: String?,
    @field:Column("DESC_")
    @field:UniqueCombine(1)
    var desc: String?,
    @field:Column("CONTENT_")
    var content: String?,
    @field:Column("LOG_TIME")
    var logTime: String?) {

    @Column("ID_")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    var id: Int = 0
}
