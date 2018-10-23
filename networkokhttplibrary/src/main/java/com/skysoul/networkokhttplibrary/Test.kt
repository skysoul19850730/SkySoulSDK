package com.skysoul.networkokhttplibrary

import android.util.Log
import com.skysoul.networkokhttplibrary.demo4encapsulate.SSConstantsInfo
import okhttp3.*
import java.io.IOException

object Test {
    fun testNull(){
        var b:String?=null
        var d = StringBuilder()
        d.append(b)
        Log.d("sqc",d.toString())
    }

    fun test(){
        var client = OkHttpClient()
        var mediaType =   MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
        var requestParamsMap = hashMapOf<String,String>()
        requestParamsMap.put("count",12.toString())
        requestParamsMap.put("gender",1.toString())
        var strRequestUrlParam = StringBuffer()
        for ((key, value) in requestParamsMap) {
            strRequestUrlParam.append("&")
                    .append(key)
                    .append("=")
                    .append(value)
        }
        if (strRequestUrlParam.length > 0) {
            strRequestUrlParam.deleteCharAt(0)
        }

        var requestStr = strRequestUrlParam.toString()
        var bytes =  requestStr.toByteArray(Charsets.UTF_8)

        var request = Request.Builder().url(SSConstantsInfo.Resource.RandomCaricature)
                .post(RequestBody.create(mediaType,bytes))
//                .header("Platform","android:3.0:mojipop")
//                .addHeader("Lang","zh")
                .build()
        var call = client.newCall(request)
        call.enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("sqc","onfail")
            }

            override fun onResponse(call: Call?, response: Response?) {
                var bbbb = response?.body()?.bytes()
                var str = String(bbbb!!,Charsets.UTF_8)
                Log.d("sqc","onResponse${response?.code()}${str}")
            }

        })
    }
}