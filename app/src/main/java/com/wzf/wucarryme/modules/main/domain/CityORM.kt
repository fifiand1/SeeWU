package com.wzf.wucarryme.modules.main.domain

import com.litesuits.orm.db.annotation.NotNull
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

@Table("weather_city")
class CityORM(@field:NotNull
              val name: String) {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    val id: Int = 0
}
