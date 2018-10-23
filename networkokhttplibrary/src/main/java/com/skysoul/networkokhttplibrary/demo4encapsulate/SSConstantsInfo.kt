package com.skysoul.networkokhttplibrary.demo4encapsulate


class SSConstantsInfo {

    object ImageUpload {
        private val HOST_TEST = "http://121.41.80.48:8099/"
        private val HOST_OFFICAL = "http://imgupload.momentcam.net/"
        //        private static final String HOST_OFFICAL = "http://imgupload.api.mojipop.com/";
        val IMAGE_UPLOAD = HOST_TEST + "api/Image/Upload"
    }

    object Render {
        private val HOST_TEST = "http://121.43.190.213:8801/"
        private val HOST_OFFICAL = "http://render-mojipop.momentcam.net/"

        val GetCartoonFace = HOST_TEST + "api/FaceDetection/GetCartoonFace"
    }

    object Resource {
        private val HOST_TEST = "http://18.136.111.182:8001/"
        private val HOST_OFFICAL = "http://resource.api.mojipop.com/"
        val RandomCaricature = HOST_OFFICAL + "Api/Commend/RandomCaricature"
//        val RandomCaricature = "http://www.baidu.com"
        val RandomCaricature_Commend = HOST_TEST + "Api/Commend/vote"
    }


}
