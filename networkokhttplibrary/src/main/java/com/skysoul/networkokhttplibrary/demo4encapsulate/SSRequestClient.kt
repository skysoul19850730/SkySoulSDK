package com.skysoul.networkokhttplibrary.demo4encapsulate

import android.content.Context
import com.skysoul.networkokhttplibrary.BaseHeaderBuilder
import com.skysoul.networkokhttplibrary.BaseRequestClient
import com.skysoul.networkokhttplibrary.NITypes
import okhttp3.OkHttpClient

class SSRequestClient(override var okHttpClient: OkHttpClient, val context: Context) : BaseRequestClient() {


    override fun initBaseHeaderBuilder(): BaseHeaderBuilder {
        return SSBaseHeaderBuilder(context)
    }

    override fun doEncryptParams(requestStr: String): ByteArray {
        return requestStr.toByteArray(Charsets.UTF_8)
    }

    override fun doDeCrypResult(byteArray: ByteArray): ByteArray {
        return byteArray
    }

    object INSTENSE{
        fun Build( context: Context, okHttpClient: OkHttpClient):SSBuilder{
            return SSBuilder(context,okHttpClient)
        }
    }

     class SSBuilder(val context: Context,val okHttpClient: OkHttpClient) : Builder() {

        fun url(ssConstants: SSConstants): Builder {
            url = ssConstants.devUrl!!
            isJava = true
            if (ssConstants.niTypes == NITypes.JAVAGET) {
                isPost = false
            }
            isEncrypt = false
            return this
        }


        override fun initClient(): BaseRequestClient {
            return SSRequestClient(okHttpClient, context);
        }

    }


}