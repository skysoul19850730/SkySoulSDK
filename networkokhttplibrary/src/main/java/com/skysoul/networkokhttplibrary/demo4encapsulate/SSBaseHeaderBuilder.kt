package com.skysoul.networkokhttplibrary.demo4encapsulate

import android.content.Context
import android.os.Build
import android.webkit.WebSettings

import com.skysoul.networkokhttplibrary.BaseHeaderBuilder
import com.skysoul.utils.apps.AppUtil
import java.util.HashMap
import okhttp3.Headers
import okhttp3.Request

/**
 * Created by lvjiaxi on 2016/12/12.
 *
 */
class SSBaseHeaderBuilder(private val context: Context) : BaseHeaderBuilder() {
    private var baseParamsMap: HashMap<String, String>? = null

    private val userAgent: String
        get() {
            var userAgent = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    userAgent = WebSettings.getDefaultUserAgent(context)
                } catch (e: Exception) {
                    userAgent = System.getProperty("http.agent")
                }

            } else {
                userAgent = System.getProperty("http.agent")
            }
            val sb = StringBuffer()
            var i = 0
            val length = userAgent.length
            while (i < length) {
                val c = userAgent[i]
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", c.toInt()))
                } else {
                    sb.append(c)
                }
                i++
            }
            return sb.toString()
        }

    override fun getBaseParams(): HashMap<String, String> {
        baseParamsMap = hashMapOf()
        return baseParamsMap!!
    }


    override fun buildBaseHeader(builder: Request.Builder) {

        val headerBuilder = Headers.Builder()
        headerBuilder.add("Lang", "zh")
        headerBuilder.add("Platform", "android:" + AppUtil.getVersionName(context) + ":mojipop")
        headerBuilder.add("User-Agent", userAgent)
        builder.headers(headerBuilder.build())
    }

}
