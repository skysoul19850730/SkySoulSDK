package com.skysoul.utils

import com.alibaba.fastjson.JSON
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun getFormatedTimeWithFormData(format: String?, date: Date?): String {
        var mFormat: String? = format
        if (mFormat == null) {
            mFormat = "yyyyMMdd_HHmmss"
        }
        var mDate: Date? = date
        if (mDate == null) {
            mDate = Date()
        }
        return SimpleDateFormat(mFormat).format(date)
    }

    /**
     * 格式示例：20130314_115745 （yyyyMMdd_HHmmss）获取当前时间 date 传空
     */
    fun getFormatedTime(date: Date?): String {
        return getFormatedTimeWithFormData(null, date)
    }

    /**
     * 格式示例：2013-03-14 11:57:45 （yyyyMMddHHmmss）
     */
    fun getFormatedTime1(date: Date?): String {
        return getFormatedTimeWithFormData("yyyyMMddHHmmss", date)
    }

    /**
     * 格式示例：2013-03-14 11:57:45 （yyyy-MM-dd HH:mm:ss）
     */
    fun getFormatedTime2(date: Date?): String {
        return getFormatedTimeWithFormData("yyyy-MM-dd HH:mm:ss", date)
    }


    /**
     * Json串转对象
     * @param jsonString
     * @param tClass
     * @param <T>
     * @return
    </T> */
    fun <T> parseObject(jsonString: String, tClass: Class<T>): T {
        return JSON.parseObject(jsonString, tClass)
    }

    /**
     * 流转对象
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
    </T> */
    fun <T> parseObject(inputStream: InputStream, tClass: Class<T>): T {
        val jsonString = Stream2JsonStr(inputStream)
        return JSON.parseObject(jsonString, tClass)
    }

    /**
     * 流转Json串
     * @param inputStream
     * @return
     */
    fun Stream2JsonStr(inputStream: InputStream): String {
        var jsonStr = ""
        // ByteArrayOutputStream相当于内存输出流
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        // 将输入流转移到内存输出流中
        try {
            inputStream.use { input ->
                //lamuda表达式+() 即闭包的方式来代表一个变量进行比较
//                if({len}()!=-1){
//
//                }
                out.use {
                    while ({ len = input.read(buffer, 0, buffer.size);len }() != -1) {
                        it.write(buffer, 0, len)
                    }
                    // 将内存流转换为字符串
                    jsonStr = String(out.toByteArray())
                }
                //使用also扩展，input.read(buffer,0,buffer.size).also { len = it }，返回的并不是len，返回的应该是it，只不过内部将it赋值给了len，所以后面可以直接用len。also返回的是使用者本身
//                out.use { output ->
//                    while (input.read(buffer,0,buffer.size).also { len = it } !=-1){
//                        output.write(buffer,0,len)
//                    }
//                    // 将内存流转换为字符串
//                    jsonStr = String(out.toByteArray())
//                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return jsonStr
    }
}