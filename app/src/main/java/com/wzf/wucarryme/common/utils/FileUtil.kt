package com.wzf.wucarryme.common.utils

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

import android.content.Context

object FileUtil {

    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        }

        if (file.isDirectory) {
            val childFiles = file.listFiles()
            if (childFiles == null || childFiles.size == 0) {
                return file.delete()
            }

            for (childFile in childFiles) {
                delete(childFile)
            }
            return file.delete()
        }
        return false
    }

    /**
     * 复制asset文件到指定目录
     * @param oldPath  asset下的路径
     * @param newPath  SD卡下保存路径
     */
    fun CopyAssets(context: Context, oldPath: String, newPath: String) {
        try {
            val fileNames = context.assets.list(oldPath)// 获取assets目录下的所有文件及目录名
            if (fileNames.size > 0) {// 如果是目录
                val file = File(newPath)
                file.mkdirs()// 如果文件夹不存在，则递归
                for (fileName in fileNames) {
                    CopyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName)
                }
            } else {// 如果是文件
                val `is` = context.assets.open(oldPath)
                val fos = FileOutputStream(File(newPath))
                val buffer = ByteArray(1024)
                var byteCount = 0
                while ((`is`.read(buffer).apply { byteCount = this }) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount)// 将读取的输入流写入到输出流
                }
                fos.flush()// 刷新缓冲区
                `is`.close()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
