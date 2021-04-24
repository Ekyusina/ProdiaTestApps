package com.example.prodiatestapps.apiservice

import com.example.prodiatestapps.model.LoginResponse
import com.example.prodiatestapps.model.RegisterResponse
import com.example.prodiatestapps.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String?,
            @Field("password") password: String?
    ): Call<LoginResponse?>?

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("name") name: String?,
            @Field("email") email: String?,
            @Field("password") password: String?
    ): Call<RegisterResponse?>?

    @Multipart
    @POST("links/upload")
    fun upload(
            @Part file: MultipartBody.Part
    ): Call<UploadResponse?>?
}