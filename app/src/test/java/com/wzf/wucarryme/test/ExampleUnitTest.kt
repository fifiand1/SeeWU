package com.wzf.wucarryme.test

import com.wzf.wucarryme.modules.service.CollectorService.Companion.SELL_REG
import org.junit.Test
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
//        val p = "9:30 借高开兑现20%医药股(挂单成交),昨日冲涨停今天高看吃肉走人.买进20%通信股"
        val p = "13:25 T出30%券商股,T出10%银行股,T出10%四高股"
        for (key in SELL_REG) {
//        for (key in BUY_REG) {
//            System.out.println("key:$key")
            // 编译正则表达式
            val pattern = Pattern.compile(key)
            // 忽略大小写的写法
            // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            val matcher = pattern.matcher(p)
            // 查找字符串中是否有匹配正则表达式的字符/字符串
            while (matcher.find()) {

                System.out.println(matcher.group())
//                System.out.println(matcher.group(1))
//                System.out.println(matcher.group(2))
//                System.out.println(matcher.group(3))
            }
        }
    }
}