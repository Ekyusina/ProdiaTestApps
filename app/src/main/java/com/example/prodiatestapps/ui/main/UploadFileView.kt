package com.example.prodiatestapps.ui.main

import okhttp3.MultipartBody

class UploadFileView{
    interface InitView{
        fun uploadSuccess(file: String?)
        fun uploadFailed(message: String?)
    }

    interface UploadFile{
        fun uploadFile(file: MultipartBody.Part?)
    }
}