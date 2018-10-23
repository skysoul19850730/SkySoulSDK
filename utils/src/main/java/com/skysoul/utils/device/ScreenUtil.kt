package com.skysoul.utils.device

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import com.skysoul.utils.cache.SharedPreferencesManager

object ScreenUtil {

    val bootomViewHeight = 75
    val LOW_DPI_STATUS_BAR_HEIGHT = 19
    val MEDIUM_DPI_STATUS_BAR_HEIGHT = 25
    val HIGH_DPI_STATUS_BAR_HEIGHT = 38
    val XHIGH_DPI_STATUS_BAR_HEIGHT = 50

    var currentDisplayDensity = DisplayMetrics.DENSITY_MEDIUM

    fun getScreenParameters(context: Context) {

        var displayMetrics = context.resources.displayMetrics
        var screenWith = displayMetrics.widthPixels
        var screenHight = displayMetrics.heightPixels
        var statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT
        currentDisplayDensity = displayMetrics.densityDpi

        var resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resId)
        } else {
            statusBarHeight =
                    when (displayMetrics.densityDpi) {
                        DisplayMetrics.DENSITY_LOW -> {
                            this.LOW_DPI_STATUS_BAR_HEIGHT
                        }
                        DisplayMetrics.DENSITY_MEDIUM -> MEDIUM_DPI_STATUS_BAR_HEIGHT
                        DisplayMetrics.DENSITY_HIGH -> HIGH_DPI_STATUS_BAR_HEIGHT
                        DisplayMetrics.DENSITY_XHIGH -> XHIGH_DPI_STATUS_BAR_HEIGHT
                        else -> {
                            XHIGH_DPI_STATUS_BAR_HEIGHT
                        }
                    }
        }

        var spm = SharedPreferencesManager.INSTANCE.getInstance()
        spm.setInt(SharedPreferencesManager.SCREEN_WIDTH, screenWith)
        spm.setInt(SharedPreferencesManager.SCREEN_HEIGHT, screenHight)
        spm.setInt(SharedPreferencesManager.SCREEN_STATUS_BAR_HEIGHT, statusBarHeight)
        spm.setInt(SharedPreferencesManager.currentDisplayDensity, currentDisplayDensity)
    }

    fun getScreenWidth(): Int {
        return SharedPreferencesManager.INSTANCE.getInstance().getInt(SharedPreferencesManager.SCREEN_WIDTH)
    }

    fun getScreenHeight(): Int {
        return SharedPreferencesManager.INSTANCE.getInstance().getInt(SharedPreferencesManager.SCREEN_HEIGHT)
    }

    fun getScreenStatusBarHeight(): Int {
        return SharedPreferencesManager.INSTANCE.getInstance().getInt(SharedPreferencesManager.SCREEN_STATUS_BAR_HEIGHT)
    }

    /**
     * 获取导航栏高
     * @param context
     * @return
     */
//    fun getSmartBarHeight(context: Context): Int {
//        val smartBarHeight = Util.dip2px(context, 42)
//        return smartBarHeight
//    }

    /**
     * 获取当前屏幕高
     * @param activity
     * @return
     */
    fun getCurrentScreenHeight(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        return metrics.heightPixels
    }

    /**
     * 设备是否有 smartBar
     * Flyme4.0 上SmartBar 高度是48dip，SmartBar 上的menu 限制最
     * 多5个，超出移至overflowbutton中，menu大小也有一定的限制，代码中
     * 计算，没有具体值；menu 上的图标建议参考SmarbarDemo 中的素材，
     * 104x104，并放到drawable-64dpi目录下，在不同dpi的Meizu设备上自
     * 动缩放。以
     * @return
     */
    fun hasSmartBar(): Boolean {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            val method = Class.forName("android.os.Build").getMethod("hasSmartBar")
            return (method.invoke(null) as Boolean)
        } catch (e: Exception) {
        }

        // 反射不到Build.hasSmartBar()，则用Build.DEVICE判断
        if (Build.DEVICE == "mx2") {
            return true
        } else if (Build.DEVICE == "mx" || Build.DEVICE == "m9") {
            return false
        }
        return false
    }

    /**
     * 获取是否存在NavigationBar：
     * @param context
     * @return
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return hasNavigationBar

    }

    /**
     * 获取NavigationBar的高度：
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        var navigationBarHeight = 0
        val rs = context.resources
        val id = rs.getIdentifier("navigation_bar_height", "dimen", "android")
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id)
        }
        return navigationBarHeight
    }
}