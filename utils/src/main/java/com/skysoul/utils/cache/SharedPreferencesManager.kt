package com.skysoul.utils.cache

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager {

    public companion object {
        val SCREEN_WIDTH = "screen_width"
        val SCREEN_HEIGHT = "screen_height"
        val SCREEN_STATUS_BAR_HEIGHT = "screen_status_bar_height"
        val BOTTOM_VIEW_HEIGHT = "BOTTOM_VIEW_HEIGHT"
        val currentDisplayDensity = "currentDisplayDensity"
    }

    object INSTANCE {
        private val SHP_NAME = "shared_preferences_skysoul"
        private lateinit var spm: SharedPreferencesManager

        @Synchronized
        fun init(context: Context) {
            if (spm == null) {
                spm = SharedPreferencesManager()
                spm.sp = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE)
            }
        }

        @Throws(NullPointerException::class)
        fun getInstance(): SharedPreferencesManager {
            if (spm == null) {
                throw NullPointerException()
            }
            return spm
        }
    }

    private lateinit var sp: SharedPreferences

    fun getString(key:String):String?{
        return getString(key,null)
    }
    fun getString(key:String,defaultValue:String?):String?{
        return sp.getString(key,defaultValue)
    }
    fun getBoolean(key:String):Boolean?{
        return getBoolean(key,false)
    }
    fun getBoolean(key: String,defaultValue: Boolean):Boolean?{
        return sp.getBoolean(key,defaultValue)
    }
    fun getInt(key: String):Int{
        return getInt(key,0)
    }
    fun getInt(key: String,defaultValue: Int):Int{
        return sp.getInt(key,defaultValue)
    }
    fun getLong(key: String):Long{
        return getLong(key,0)
    }
    fun getLong(key: String,defaultValue: Long):Long{
        return sp.getLong(key,defaultValue)
    }
    fun getFloat(key: String):Float{
        return getFloat(key,0f)
    }
    fun getFloat(key: String,defaultValue: Float):Float{
        return sp.getFloat(key,defaultValue)
    }

    fun setString(key: String,value:String){
        var editor = sp.edit()
        editor.putString(key,value)
        editor.commit()
        editor.clear()
    }
    fun setFloat(key: String,value:Float){
        var editor = sp.edit()
        editor.putFloat(key,value)
        editor.commit()
        editor.clear()
    }
    fun setLong(key: String,value:Long){
        var editor = sp.edit()
        editor.putLong(key,value)
        editor.commit()
        editor.clear()
    }
    fun setInt(key: String,value:Int){
        var editor = sp.edit()
        editor.putInt(key,value)
        editor.commit()
        editor.clear()
    }
    fun setBoolean(key: String,value:Boolean){
        var editor = sp.edit()
        editor.putBoolean(key,value)
        editor.commit()
        editor.clear()
    }

    fun removeKey(key: String){
        var editor = sp.edit()
        editor.remove(key)
        editor.commit()
        editor.clear()
    }

}