package com.skysoul.networkokhttplibrary.demo4encapsulate


import com.skysoul.networkokhttplibrary.NITypes

enum class SSConstants {
    ImageUpload(SSConstantsInfo.ImageUpload.IMAGE_UPLOAD, NITypes.JAVA),
    GetCartoonFace(SSConstantsInfo.Render.GetCartoonFace, NITypes.JAVA),
    RandomCaricature(SSConstantsInfo.Resource.RandomCaricature, NITypes.JAVA),
    RandomCaricature_Commend(SSConstantsInfo.Resource.RandomCaricature_Commend, NITypes.JAVA),
    RandomCaricature_Commend_Test(SSConstantsInfo.Resource.RandomCaricature_Commend);

    var confKey: String? = null
        private set
    var devUrl: String? = null
        private set
    var niTypes: NITypes? = null
        private set

    private constructor(devUrl: String) {
        this.devUrl = devUrl
        this.confKey = name
        this.niTypes = NITypes.JAVAGET
    }

    private constructor(devUrl: String, niTypes: NITypes) {
        this.devUrl = devUrl
        this.confKey = name
        this.niTypes = niTypes
    }

}
