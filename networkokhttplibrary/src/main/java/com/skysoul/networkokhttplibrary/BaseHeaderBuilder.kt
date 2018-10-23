package com.skysoul.networkokhttplibrary

import okhttp3.Request

abstract class BaseHeaderBuilder {

    abstract fun getBaseParams():HashMap<String,String>
    abstract fun buildBaseHeader(builder:Request.Builder)
}