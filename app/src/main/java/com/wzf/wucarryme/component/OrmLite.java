package com.wzf.wucarryme.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.litesuits.orm.LiteOrm;
import com.wzf.wucarryme.BuildConfig;
import com.wzf.wucarryme.R;
import com.wzf.wucarryme.base.BaseApplication;
import com.wzf.wucarryme.common.C;

import android.os.Environment;

/**
 * 统一数据库操作都使用OrmLite
 */
public class OrmLite {

    public static final String DB_NAME = "wu.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.wzf.wucarryme";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
        PACKAGE_NAME + "/databases";  //在手机里存放数据库的位置(/data/data/com.wzf.wucarryme/databases/wu.db)
    public static final String DB_FILE = DB_PATH + "/" + DB_NAME; //数据库名字
    private LiteOrm sLiteOrm;
    private static OrmLite instance;

    public static LiteOrm getInstance() {
        if (instance == null) {
            instance = new OrmLite();
        }
        return instance.sLiteOrm;
    }

    private OrmLite() {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.getAppContext(), C.ORM_NAME);
        }
        sLiteOrm.setDebugged(BuildConfig.DEBUG);
    }

    /**
     * 检查数据库文件是否存在，若不存在则执行导入
     */
    public static void checkDB() {
        File file = new File(DB_FILE);
        if (!(file.exists())) {
            InputStream is = BaseApplication.getAppContext().getResources().openRawResource(R.raw.wu); //欲导入的数据库
            FileOutputStream fos = null;
            try {
                try {
                    new File(DB_PATH).mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fos = new FileOutputStream(DB_FILE);
                int BUFFER_SIZE = 400000;
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
