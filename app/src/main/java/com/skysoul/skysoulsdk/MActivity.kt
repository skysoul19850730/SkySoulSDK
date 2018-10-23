package com.skysoul.skysoulsdk

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.skysoul.networkokhttplibrary.ServerErrorTypes
import com.skysoul.networkokhttplibrary.Test
import com.skysoul.networkokhttplibrary.demo4encapsulate.SSConstants
import com.skysoul.networkokhttplibrary.demo4encapsulate.SSConstantsInfo
import com.skysoul.networkokhttplibrary.demo4encapsulate.SSRequestClient
import com.skysoul.networkokhttplibrary.listeners.BaseReqListener
import com.skysoul.utils.logs.Print
import kotlinx.android.synthetic.main.mact.*
import okhttp3.*
import java.io.IOException


class MActivity:Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mact)
        Print.Init(true,null);
        btnrequest.setOnClickListener {
            doRequest()
        }
        doRequest()

    }

    fun doRequest(){


//        var client = OkHttpClient()
//        var mediaType =   MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
//        var requestParamsMap = hashMapOf<String,String>()
//        requestParamsMap.put("count",12.toString())
//        requestParamsMap.put("gender",1.toString())
//        var strRequestUrlParam = StringBuffer()
//        for ((key, value) in requestParamsMap) {
//            strRequestUrlParam.append("&")
//                    .append(key)
//                    .append("=")
//                    .append(value)
//        }
//        if (strRequestUrlParam.length > 0) {
//            strRequestUrlParam.deleteCharAt(0)
//        }
//
//        var requestStr = strRequestUrlParam.toString()
//        var bytes =  requestStr.toByteArray(Charsets.UTF_8)
//
//        var request = Request.Builder().url(SSConstantsInfo.Resource.RandomCaricature)
//                .post(RequestBody.create(mediaType,bytes))
////                .header("Platform","android:3.0:mojipop")
////                .addHeader("Lang","zh")
//                .build()
//        var call = client.newCall(request)
//        call.enqueue(object:Callback{
//            override fun onFailure(call: Call?, e: IOException?) {
//                Log.d("sqc","onfail")
//            }
//
//            override fun onResponse(call: Call?, response: Response?) {
//                var bbbb = response?.body()?.bytes()
//                var str = String(bbbb!!,Charsets.UTF_8)
//                Log.d("sqc","onResponse${response?.code()}${str}")
//            }
//
//        })




        var params:HashMap<String,String> = hashMapOf()
        params.put("count",12.toString())
        params.put("gender",1.toString())
        val listener = object : BaseReqListener<String>() {
            override fun onFail(error: ServerErrorTypes) {
                Log.d("sqc","error")
            }

            override fun onSuccess(objects: String) {
                Log.d("sqc",objects)
            }
        }
        val ssRequestClient= SSRequestClient.INSTENSE.Build(this,OkHttpClient())
                .url(SSConstants.RandomCaricature)
                .addParasMap(params)
                .listener(listener).build()
        ssRequestClient.startRequest(this)

        Test.testNull()
    }
}