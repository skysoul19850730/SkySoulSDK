package com.skysoul.networkokhttplibrary;

/**
 * Created by lvjiaxi on 2016/12/8.
 * 服务器接口种类，根据不同种类采取不同的加密/解密或POST、GET机制
 */
public enum NITypes {
    /**
     * DotNet请求
     */
    DotNET,
    /**
     * JAVA请求
     */
    JAVA,
    /**
     * JAVA GET请求
     */
    JAVAGET,
    ;

    NITypes() {
    }
}
