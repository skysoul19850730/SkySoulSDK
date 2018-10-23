package com.skysoul.utils.encrypUtils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncrypUtil {
    fun getMD5(sourceString: String): String {
        try {
            var toChapter = sourceString.toByteArray(Charsets.UTF_8)
            var md5 = MessageDigest.getInstance("MD5")
            md5.update(toChapter)
            var toChapterDigest = md5.digest()

            var md5str = StringBuffer()
            for (item in toChapterDigest) {
                var digital: Int = item.toInt() and 0xff
                var hexString = Integer.toHexString(digital)
                if (hexString.length < 2) {
                    hexString = "0" + hexString
                }
                md5str.append(hexString)
            }
            return md5str.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}