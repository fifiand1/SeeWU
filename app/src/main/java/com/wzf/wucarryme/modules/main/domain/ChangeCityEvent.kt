package com.wzf.wucarryme.modules.main.domain

class ChangeCityEvent {

    internal lateinit var city: String
    internal var isSetting: Boolean = false

    constructor() {}

    constructor(isSetting: Boolean) {
        this.isSetting = isSetting
    }

    constructor(city: String) {
        this.city = city
    }
}
