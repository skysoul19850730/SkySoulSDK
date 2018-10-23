package com.skysoul.networkokhttplibrary.listeners

import com.skysoul.networkokhttplibrary.ServerErrorTypes
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class  BaseReqListener<T> {
    abstract fun onFail(error:ServerErrorTypes)
    abstract fun onSuccess(objects: T)

    fun onSuccessDeal(o:Any){
        o as T
        onSuccess(o)
    }

    fun getResponseClass():Class<T>{
        val entityClass : Class<T> = ((javaClass.genericSuperclass) as ParameterizedType).actualTypeArguments[0] as Class<T>
        return entityClass
    }
}