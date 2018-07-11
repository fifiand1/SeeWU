package com.wzf.wucarryme.modules.main.domain

import info.debatty.java.stringsimilarity.JaroWinkler

/**
 * @author wzf
 * @date 2018/7/5
 */
class Sentence(var value: String) {

    override fun equals(other: Any?): Boolean {
        if (other is Sentence) {
            val jw = JaroWinkler()
            val similarity = jw.similarity(this.value, other.value)
            return similarity > 0.8
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}