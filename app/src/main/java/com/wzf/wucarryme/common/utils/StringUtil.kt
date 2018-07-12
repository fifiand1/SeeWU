package com.wzf.wucarryme.common.utils

import java.util.*

/**
 * @author wzf
 * @date 2018/3/22
 */

object StringUtil {

    fun banKuai(str: String): String {
        var str = str

        str = str.replace("类".toRegex(), "")
        str = str.replace("概念".toRegex(), "")
        str = str.replace("房地产".toRegex(), "地产")
        str = str.replace("次新".toRegex(), "新")
        val index = str.indexOf("股")
        if (index > -1) {
            str = str.substring(0, index)
        }
        return str
    }

    fun getBlogTime(p: String): String? {
        val blogTime: String?
        p.replace("[\\u00A0]+", " ")
//        val endIndex = if (p.contains(" ")) p.indexOf(" ") else p.indexOf(160)
        val endIndex = p.indexOf(" ")
        blogTime = try {
            p.substring(0, endIndex)
        } catch (e: Exception) {
            null
        }
        return blogTime
    }

    fun commaStr2List(commaStr: String): List<String> {
        return Arrays.asList(*commaStr.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())
    }

    fun list2commaStr(list: List<String>): String {
        var result = StringBuilder()
        for (i in list.indices) {
            result.append(list[i]).append(",")
        }
        if (result.toString().endsWith(",")) {
            result = StringBuilder(result.substring(0, result.length - 1))
        }
        return result.toString()
    }

    fun formatCNName(cn_name: String): String {
        var cn_name = cn_name
        cn_name = cn_name.trim { it <= ' ' }
        if (cn_name.length > 4) {
            cn_name = cn_name.substring(0, 4)
            if (cn_name.endsWith("等")) {
                cn_name = cn_name.substring(0, 3)
            }
        }
        return cn_name
    }

    fun genLike(colName: String, bankuai: String): String {
        val list = ArrayList<String>()
        list.add(bankuai)
        run {
            var i = 0
            while (i + 2 <= bankuai.length) {
                list.add(bankuai.substring(i, i + 2))
                i += 2
            }
        }
        val result = StringBuilder()
        for (i in list.indices) {
            val like = list[i]
            result.append(" ").append(colName).append(" like '%").append(like).append("%'")
            if (i < list.size - 1) {
                result.append(" or")
            }
        }
        return result.toString()
    }
}
