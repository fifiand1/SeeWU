package com.wzf.wucarryme.component

import android.os.Environment
import com.litesuits.orm.LiteOrm
import com.wzf.wucarryme.BuildConfig
import com.wzf.wucarryme.R
import com.wzf.wucarryme.base.BaseApplication
import com.wzf.wucarryme.common.C
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 统一数据库操作都使用OrmLite
 */
class OrmLite private constructor() {

    private var sLiteOrm: LiteOrm? = null

    init {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.appContext!!, C.ORM_NAME)
        }
        sLiteOrm!!.setDebugged(BuildConfig.DEBUG)
    }

    companion object {

        private const val DB_NAME = "wu.db" //数据库名字
        private const val PACKAGE_NAME = "com.wzf.wucarryme"
        private val DB_PATH = "/data" + Environment.getDataDirectory().absolutePath + "/" +
            PACKAGE_NAME + "/databases"  //在手机里存放数据库的位置(/data/data/com.wzf.wucarryme/databases/wu.db)
        private val DB_FILE = "$DB_PATH/$DB_NAME" //数据库名字
        private var instance: OrmLite? = null

        fun getInstance(): LiteOrm {
            if (instance == null) {
                instance = OrmLite()
            }
            return instance!!.sLiteOrm!!
        }

        /**
         * 检查数据库文件是否存在，若不存在则执行导入
         */
        fun checkDB() {
            val file = File(DB_FILE)
            if (!file.exists()) {
                val `is` = BaseApplication.appContext!!.resources.openRawResource(R.raw.wu) //欲导入的数据库
                var fos: FileOutputStream? = null
                try {
                    try {
                        File(DB_PATH).mkdirs()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    fos = FileOutputStream(DB_FILE)
                    val BUFFER_SIZE = 400000
                    val buffer = ByteArray(BUFFER_SIZE)
                    var count = 0
                    while ((`is`.read(buffer).apply { count = this }) > 0) {
                        fos.write(buffer, 0, count)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        if (fos != null) {
                            fos.close()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    try {
                        `is`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
