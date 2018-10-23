package com.skysoul.utils.logs

import android.util.Log
import com.skysoul.utils.Util
import com.skysoul.utils.files.FileUtil

import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

/**
 * 自定义信息打印
 */
object Print {
    private val LEVEL_V = 0
    private val LEVEL_I = 1
    private val LEVEL_D = 2
    private val LEVEL_E = 3

    /**
     * 用于关闭打印信息(false 关闭打印)-出包时设置为false
     */
    private var openPrint = false
    /**
     * 输出文件,含文件名
     */
    private var fileOutputPath: String? = null
    /**
     * Debug等级
     */
    private val debugLevel = LEVEL_V

    /**
     * 初始化
     * @param openPrint
     * @param fileOutputPath 输出文件,含文件名
     */
    fun Init(openPrint: Boolean, fileOutputPath: String?) {
        Print.openPrint = openPrint
        Print.fileOutputPath = fileOutputPath
    }

    fun e(classtag: String?, methodtag: String?, text: String?) {
        if (!openPrint) {
            return
        }
        if (classtag == null || methodtag == null || text == null) {
            return
        }
        if (debugLevel <= LEVEL_E) {
            Log.e("类->$classtag 方法->$methodtag", text)
        }

    }
    fun eToFile(classtag: String?, methodtag: String?, text: String?) {
        eToFileWithFilePath(classtag,methodtag,text,null)
    }
    fun  eToFileWithFilePath(classtag: String?, methodtag: String?, text: String?,filePath:String?) {
        e(classtag,methodtag,text)
        printToFile(classtag, methodtag, text,filePath)
    }

    fun i(tag: String?, text: String?) {
        if (!openPrint) {
            return
        }
        if (tag == null || text == null) {
            return
        }
        if (debugLevel <= LEVEL_I) {
            Log.i(tag, text)
        }
    }

    fun d(tag: String?, text: String?) {
        if (!openPrint) {
            return
        }
        if (tag == null || text == null) {
            return
        }

        if (debugLevel <= LEVEL_D) {
            Log.d(tag, text)
        }
    }

    fun d(classtag: String?,tag: String?, text: String?) {
        if (!openPrint) {
            return
        }
        if (classtag==null || tag == null || text == null) {
            return
        }

        if (debugLevel <= LEVEL_D) {
            Log.d("类->$classtag 方法->$tag", text)
        }
    }


    fun v(classtag: String?, tag: String?, text: String?) {
        if (!openPrint) {
            return
        }
        if (classtag == null || tag == null || text == null) {
            return
        }
        if (debugLevel <= LEVEL_V) {
            Log.v("类->$classtag 方法->$tag", text)
        }
    }

    fun printToFile(classtag: String?, tag: String?, text: String?,newOutputPath: String?) {
        if (!openPrint) {
            return
        }

        var toSavePath = fileOutputPath
        if(newOutputPath!=null){
            toSavePath = newOutputPath
        }

        if (toSavePath == null) {
            return
        }
        if (classtag == null || tag == null || text == null) {
            return
        }
        val toSaveFile = FileUtil.readyFile(toSavePath)
        try {
            val writer = PrintWriter(FileWriter(toSaveFile, true))
            writer.write(Util.getFormatedTime(null) + "\n")
            writer.write("$classtag..$tag..$text\n")
            writer.write("\n")
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
