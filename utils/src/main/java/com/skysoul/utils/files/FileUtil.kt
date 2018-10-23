package com.skysoul.utils.files

import java.io.File
import java.io.IOException

/**
 * @date   2018/10/22 11:53
 * @author sqc
 * @Description
 */
object FileUtil {
    /**
     * @param path 含文件名的全路径，负责检查并创建父文件夹及创建文件本身,并返回文件
     * */
    fun readyFile(path: String): File? {
        var file = File(path)
        var fileParent = file.parentFile
        if (!fileParent.exists()) {
            fileParent.mkdirs()
        }
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
        return file
    }
}