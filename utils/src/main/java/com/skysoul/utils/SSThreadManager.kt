package com.skysoul.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by lvjiaxi on 2016/9/24.
 * 多线程工具
 */
object SSThreadManager {
    private val ASYNC_THREAD_NAME = "ASYNC_THREAD_NAME"
    private val GIF_PLAYER_THREAD_NAME = "GIF_PLAYER_THREAD_NAME"
    private var mainHandler: Handler? = null
    private val mainLock = Any()
    private var operatingHanlder: Handler? = null
    private var gifOperatingHanlder: Handler? = null
    private var gifOperatingThread: HandlerThread? = null
    private var operatingThread: HandlerThread? = null
    val NETWORK_EXECUTOR = excutor

    /**
     * 获取执行器
     * @return
     */
    private val excutor: Executor
        get() {
            var threadPoolExecutor: ThreadPoolExecutor? = null
            threadPoolExecutor = ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, LinkedBlockingQueue())

            threadPoolExecutor.corePoolSize = 3

            return threadPoolExecutor
        }

    /**
     * 获取异步线程
     */
    val asyncThreadHandler: Handler
        get() {
            if (operatingHanlder == null) {
                synchronized(SSThreadManager::class.java) {
                    operatingThread = HandlerThread(ASYNC_THREAD_NAME)
                    operatingThread!!.start()
                    operatingHanlder = Handler(operatingThread!!.looper)
                }
            }

            return operatingHanlder!!
        }

    /**
     * 获取异步线程
     */
    val gifPlayerThreadHandler: Handler
        get() {
            if (gifOperatingHanlder == null) {
                synchronized(SSThreadManager::class.java) {
                    gifOperatingThread = HandlerThread(GIF_PLAYER_THREAD_NAME)
                    gifOperatingThread!!.start()
                    gifOperatingHanlder = Handler(gifOperatingThread!!.looper)
                }
            }

            return gifOperatingHanlder!!
        }

    /**
     * 是否是主线程
     */
    val isMainThread: Boolean
        get() = Looper.getMainLooper() == Looper.myLooper()

    /**
     * 获取网络线程
     */
    fun executeOnNetWorkThread(runnable: Runnable) {
        try {
            NETWORK_EXECUTOR.execute(runnable)
        } catch (ignored: RejectedExecutionException) {
        }

    }

    /**
     * 获取主线程
     */
    fun getMainHandler(): Handler {
        if (mainHandler == null) {
            synchronized(mainLock) {
                if (mainHandler == null) {
                    mainHandler = Handler(Looper.getMainLooper())
                }
            }
        }

        return mainHandler!!
    }
}
