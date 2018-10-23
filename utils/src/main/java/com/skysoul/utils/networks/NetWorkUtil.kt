package com.skysoul.utils.networks

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

object NetWorkUtil {

    fun isNetworkConnected(context: Context?):Boolean{
        var manage:ConnectivityManager  = context?.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info = manage?.activeNetworkInfo
        if(info!=null && info.isConnected){
            return true
        }
       return  false
    }
}