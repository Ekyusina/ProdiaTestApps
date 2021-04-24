package com.example.prodiatestapps.ui.main

import com.example.prodiatestapps.apiservice.ApiClient
import com.example.prodiatestapps.apiservice.ApiInterface
import com.example.prodiatestapps.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadFilePresenter(initView: UploadFileView.InitView): UploadFileView.UploadFile {
    private val initView : UploadFileView.InitView

    init {
        this.initView = initView
    }

    override fun uploadFile(file: MultipartBody.Part?) {
        val apiInterface: ApiInterface? = ApiClient().getRetrofitInstance()?.create(ApiInterface::class.java)
        val call: Call<UploadResponse?>? = file?.let { it -> apiInterface?.upload(it) }
        call?.enqueue(object : Callback<UploadResponse?> {

            override fun onResponse(call: Call<UploadResponse?>, response: Response<UploadResponse?>) {
                if (response.isSuccessful){
                    initView.uploadSuccess(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<UploadResponse?>, t: Throwable) {
                initView.uploadFailed(t.toString())
                t.printStackTrace()
            }
        })
    }
}