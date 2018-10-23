package com.skysoul.networkokhttplibrary

enum class ServerErrorTypes {
    /**
     * 无网络
     */
    ERROR_NO_NETWORK,
    /**
     * 服务器错误4
     */
    ERROR_4XX,
    /**
     * 服务器错误5
     */
    ERROR_5XX,
    /**
     * 超时
     */
    ERROR_TIMEOUT,
    /**
     * 其他
     */
    ERROR_OTHER,
    /**
     * 数据错误
     */
    ERROR_DATA,
    /** 回调接口发生异常被捕获，会继续调用onfail  */
    ERROR_FROM_LISTENER,
}


