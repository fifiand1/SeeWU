package com.wzf.wucarryme.common.utils

object DoubleClickExit {
    /**
     * 双击退出检测, 阈值 1000ms
     */
    var mLastClick = 0L
    private val THRESHOLD = 2000// 1000ms

    fun check(): Boolean {
        val now = System.currentTimeMillis()
        val b = now - mLastClick < THRESHOLD
        mLastClick = now
        return b
    }
}
