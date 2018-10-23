package com.skysoul.networkokhttplibrary

import android.content.Context
import com.skysoul.networkokhttplibrary.listeners.BaseReqListener
import com.skysoul.utils.SSThreadManager
import com.skysoul.utils.Util
import com.skysoul.utils.encrypUtils.EncrypUtil
import com.skysoul.utils.logs.Print
import com.skysoul.utils.networks.NetWorkUtil
import okhttp3.*
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

abstract class BaseRequestClient {
    val TAG = BaseRequestClient::class.java.simpleName
    private lateinit var url: String
    private var reqListener: BaseReqListener<*>? = null
    private var isPost = true
    private var isSync = true
    private var isJava = true
    private var isEncrypt = false
    private var bytes: ByteArray? = null
    private var timeout = 30000L
    private var call: Call? = null
    private var baseHeaderBuilder: BaseHeaderBuilder? = null

    abstract fun initBaseHeaderBuilder(): BaseHeaderBuilder
    abstract var okHttpClient: OkHttpClient
    /**
     * 如果需要对参数进行加密验证，需要使用者自己与服务器定制加密逻辑，并实现此方法
     */
    abstract fun doEncryptParams(requestStr: String): ByteArray

    abstract fun doDeCrypResult(byteArray: ByteArray): ByteArray
    /**
     * 默认tag是""
     */
    fun startRequest(context: Context) {
        startRequest("", context)
    }

    fun startRequest(obj: Any, context: Context) {
        var isNetWorkOk = NetWorkUtil.isNetworkConnected(context)
        if (!isNetWorkOk) {
            Print.d(TAG, "startRequest", "isNetworkConnected = ERROR_NO_NETWORK");
            reqListener?.onFail(ServerErrorTypes.ERROR_NO_NETWORK)
            return
        }
        if (url == null) {
            Print.d(TAG, "startRequest", "url = null");
            reqListener?.onFail(ServerErrorTypes.ERROR_OTHER)
            return
        }

        if (isPost && bytes == null) {
            Print.d(TAG, "startRequest", "post but bytes = null");
            reqListener?.onFail(ServerErrorTypes.ERROR_OTHER)
            return
        }
        val clientBuilder = okHttpClient.newBuilder()
        clientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS)
        clientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS)

        if (isPost && isEncrypt) {
//            clientBuilder.addNetworkInterceptor{
//                val originalRequest:Request = it.request()
//
//                var clearBuilder = originalRequest.newBuilder()
//                        .removeHeader("Content-Type")
//                        .removeHeader("Accept-Encoding")
//                        .removeHeader("User-Agent")
//                        .build()
//                return@addNetworkInterceptor it.proceed(clearBuilder)
//            }
        }
//        val client = okHttpClient
        val client = clientBuilder.build()
        client.dispatcher().maxRequests = 32
        client.dispatcher().maxRequestsPerHost = 2

        val builder = Request.Builder().url(url)
        baseHeaderBuilder = initBaseHeaderBuilder()
        baseHeaderBuilder?.buildBaseHeader(builder)

        if (isPost) {
            if (isEncrypt) {
                builder.addHeader("EncryptType", "CARTOONME")
            } else {
                builder.addHeader("EncryptType", "NONE")
            }

            var mediaType: MediaType
            if (isJava) {
                mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
            } else {
                mediaType = MediaType.parse("application/octet-stream; charset=utf-8")
            }

            builder.post(RequestBody.create(mediaType, bytes))
        }

        builder.tag(obj)
        val request: Request = builder.build()
        try {
            if (isSync) {//同步
                call = client.newCall(request)
                try {
                    val response = call!!.execute()
                    processResponse(response)
                } catch (e: SocketTimeoutException) {
                    Print.d(TAG, "startRequest", "ERROR_TIMEOUT 1");
                    reqListener?.onFail(ServerErrorTypes.ERROR_TIMEOUT)
                }
            } else {
                call = client.newCall(request)
                call?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Print.d(TAG, TAG, "onFailure e " + e.toString())
                        if (SocketTimeoutException::class.java == e.cause) {
                            SSThreadManager.getMainHandler().post {
                                Print.d(TAG, "onFailure", "ERROR_TIMEOUT 2")
                                reqListener?.onFail(ServerErrorTypes.ERROR_TIMEOUT)
                            }
                        } else {
                            SSThreadManager.getMainHandler().post {
                                Print.d(TAG, "onFailure", "ERROR_OTHER 1")
                                reqListener?.onFail(ServerErrorTypes.ERROR_OTHER)
                            }
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {

                            processResponse(response)
                    }

                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Print.d(TAG, "startRequest", "ERROR_OTHER 2");
            reqListener?.onFail(ServerErrorTypes.ERROR_OTHER)
        }


    }

    /**
     * @date   2018/10/22 15:59
     * @Description 处理网络响应
     */
    private fun processResponse(response: Response) {
        if (response.isSuccessful) {
            try {
                val streamClass = InputStream::class.java
                val result:Any
                if (reqListener != null && reqListener!!.getResponseClass().equals(streamClass)) {
                    result = response.body().byteStream()
                    try {
                        Print.d(TAG, "processResponse", "request success");
                        SSThreadManager.getMainHandler().post {
                            reqListener!!.onSuccessDeal(result)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Print.d(TAG, "processResponse", "ERROR_DATA 1");
                        SSThreadManager.getMainHandler().post {
                            reqListener?.onFail(ServerErrorTypes.ERROR_FROM_LISTENER)
                        }
                    }
                } else {
                    var respBytes = response.body().bytes()
                    if (isEncrypt) {
                        respBytes = doDeCrypResult(respBytes)
                    }
                    if (respBytes == null) {
                        SSThreadManager.getMainHandler().post {
                            reqListener?.onFail(ServerErrorTypes.ERROR_OTHER)
                        }
                        return
                    }
                    val respStr = String(respBytes, Charsets.UTF_8)
                    Print.d(TAG, "processResponse", "${respStr}")
                    if (reqListener != null) {
                        result = Util.parseObject(respStr, reqListener!!.getResponseClass())!!
                        try {
                            Print.d(TAG, "processResponse", "request success");
                            SSThreadManager.getMainHandler().post {
                                reqListener!!.onSuccessDeal(result)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Print.d(TAG, "processResponse", "catch error when reqListener deal the response");
                            SSThreadManager.getMainHandler().post {
                                reqListener?.onFail(ServerErrorTypes.ERROR_FROM_LISTENER)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Print.d(TAG, "processResponse", "ERROR_DATA 3");
                SSThreadManager.getMainHandler().post {
                    reqListener?.onFail(ServerErrorTypes.ERROR_DATA)
                }
            }
        } else {
            val statusCode = response.code()
            Print.d(TAG, "processResponse", "response.code = ${statusCode}");
            SSThreadManager.getMainHandler().post {
                if (statusCode in 400 until 500) {
                    Print.d(TAG, "processResponse", "ERROR_4XX ${statusCode}");
                    reqListener?.onFail(ServerErrorTypes.ERROR_4XX)
                }
                if (statusCode >= 500) {
                    Print.d(TAG, "processResponse", "ERROR_5XX ${statusCode}");
                    reqListener?.onFail(ServerErrorTypes.ERROR_5XX)
                }
            }
        }
    }

    fun cancel() {
        if (call != null && call!!.isCanceled) {
            call!!.cancel()
        }
    }

    protected fun initParams(builder: Builder) {
        this.url = builder.url
        this.reqListener = builder.reqListener
        this.isEncrypt = builder.isEncrypt
        this.isPost = builder.isPost
        this.isSync = builder.isSync
        this.bytes = builder.bytes
        this.timeout = builder.timeout
    }


    abstract class Builder {
        internal lateinit var url: String
        internal var isSync = false
        internal var isPost = true
        internal var isEncrypt = false
        internal var isJava = true
        internal var timeout = 30000L
        internal var bytes: ByteArray? = null
        internal var needUrlEncode = true
        internal var reqListener: BaseReqListener<*>? = null
        internal var paraMap: HashMap<String, String> = hashMapOf()

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun isSync(isSync: Boolean): Builder {
            this.isSync = isSync
            return this
        }

        fun isPost(isPost: Boolean): Builder {
            this.isPost = isPost
            return this
        }

        fun addParasMap(postMapParams: Map<String, String>): Builder {
            this.paraMap.putAll(postMapParams)
            return this
        }

        fun addKeyValue(key: String, value: String): Builder {
            this.paraMap.put(key, value)
            return this
        }

        fun isEncrypt(isEncrypt: Boolean): Builder {
            this.isEncrypt = isEncrypt
            return this
        }

        fun needUrlEncode(needUrlEncode: Boolean): Builder {
            this.needUrlEncode = needUrlEncode
            return this
        }

        fun timeout(time: Long): Builder {
            if (time > 0) {
                this.timeout = time
            }
            return this
        }

        fun listener(reqListener: BaseReqListener<*>): Builder {
            this.reqListener = reqListener
            return this
        }

        fun build(): BaseRequestClient {
            var client = initClient()
            buildParams(client)
            return client
        }

        private fun buildParams(client: BaseRequestClient) {
            var requestParamsMap: HashMap<String, String>? = null
            client.baseHeaderBuilder = client.initBaseHeaderBuilder()
            if (client.baseHeaderBuilder != null) {
                requestParamsMap = client.baseHeaderBuilder!!.getBaseParams()
            }
            if (requestParamsMap == null)
                requestParamsMap = hashMapOf()

            if (paraMap != null) {
                for ((key, value) in paraMap) {
                    var newValue = value
                    if (needUrlEncode) {
                        try {
                            newValue = URLEncoder.encode(newValue, "utf-8")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    requestParamsMap.put(key, newValue)
                }
            }

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
            if (isPost) {
                if (isEncrypt) {
                    bytes = client.doEncryptParams(requestStr)
                } else {
                    try {
                        bytes = requestStr.toByteArray(Charsets.UTF_8)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            } else {
                if (!url.endsWith("?")) {
                    url += "?"
                }
                url += requestStr
            }

            client.initParams(this)


        }

        abstract fun initClient(): BaseRequestClient


    }
}