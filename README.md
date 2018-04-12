# WU carry me!
[![Build Status](https://travis-ci.org/xcc3641/SeeWeather.svg?branch=master)](https://travis-ci.org/xcc3641/SeeWeather)

通过jsoup采集博客直播内容, 跟踪仓位变化, 以及猜测自选

----

### 简介
fork from就看天气 ——是一款遵循 **Material Design** 风格的只看天气的APP。


----

权限说明

```xml
    <!-- 获取运营商信息，用于支持提供运营商信息相关的接口 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<!--用于访问网络，网络定位需要上网-->
	<uses-permission android:name="android.permission.INTERNET"/>
	<!--用于读取手机当前的状态-->
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	<!--写入扩展存储，向扩展卡写入数据，用于写入缓存定位数据-->
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

```

### 下载地址

[更新日志](changeLog.md)


### TODO

- [x] excel-->SQLite-->assets-->copy to database
- [x] spaceChange-->LocalNotification
- [x] collector service(jsoup)
- [x] 通知栏提醒
- [x] 改成kotlin
- [x] 股票当日详情刷新
- [x] UI(直播仓位变化)
- [ ] UI(直播关注个股)
- [ ] UI(直播内容)
- [x] UI(自选实时行情)
- [ ] UI(历史仓位变化图表,对比3指数)
- [ ] UI(关注个股查询)
- [ ] 引导页面
- [ ] 管理自选股
- [ ] widget桌面小部件(stock)
- [ ] 更好，更多的ICONS
- [ ] 数据去中心化同步


----

### 项目


#### 开源技术
1. [Rxjava][2]
2. [RxAndroid][3]
3. [Retrofit][4]
4. [GLide][5]


### 截图

![][image-2]
![][image-3]

### 感谢
感谢开源，感谢See Weather，学习到了前辈们优秀的代码：
- [@张鸿洋][7]
- [@扔物线][8]
- [@drakeet][9]
- [@代码家][10]
- [@程序亦非猿][11]
- [@小鄧子][12]
- [@Jude95][13]
- [@泡在网上编代码][14]

感谢优秀的设计师提供素材：
- 多城市卡片 by [YujunZhu](http://yujunzhu.zcool.com.cn/)

### 关于作者

谢三弟

- [简书](http://www.jianshu.com/users/3372b4a3b9e5/latest_articles)
- [个人博客](http://imxie.itscoder.com)
- [知乎](https://www.zhihu.com/people/xcc3641.github.io)

### License

    Copyright 2017 HugoXie Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

__软件中图片素材均来源于网络，版权属于原作者。__






[1]: https://www.zhihu.com/question/26417244/answer/70193822
[2]: https://github.com/ReactiveX/RxJava
[3]: https://github.com/ReactiveX/RxAndroid
[4]: https://github.com/square/retrofit
[5]: https://github.com/bumptech/glide
[6]: https://github.com/yangfuhai/ASimpleCache
[7]: https://github.com/hongyangAndroid
[8]: https://github.com/rengwuxian
[9]: https://github.com/drakeet
[10]: https://github.com/daimajia
[11]: https://github.com/AlanCheen
[12]: https://github.com/SmartDengg
[13]: https://github.com/Jude95
[14]: http://weibo.com/u/2711441293?topnav=1&amp;wvr=6&amp;topsug=1&amp;is_all=1


[image-2]: /images/day.png
[image-3]: /images/night.png
[image-5]: http://xcc3641.qiniudn.com/app-%E6%94%AF%E4%BB%98%E5%AE%9D.jpg
