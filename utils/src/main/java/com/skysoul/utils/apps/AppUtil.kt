package com.skysoul.utils.apps

import android.content.Context
import android.content.pm.PackageManager

object AppUtil {
    private var versionName:String? = null
    fun getVersionName(context: Context): String? {
        if (versionName != null) {
            return versionName
        }
        val pm = context.getPackageManager()
        try {
            val pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES)
            versionName = pi.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return versionName
    }
}