package com.example.prodiatestapps.ui.register

import com.example.prodiatestapps.apiservice.ApiClient
import com.example.prodiatestapps.apiservice.ApiInterface
import com.example.prodiatestapps.model.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterPresenter(initView: RegisterView.InitView) : RegisterView.Register {
    private val initView: RegisterView.InitView

    init {
        this.initView = initView
    }

    override fun register(name: String?, email: String?, pwd: String?) {
        initView.showLoading()
        val apiInterface: ApiInterface? = ApiClient().getRetrofitInstance()?.create(ApiInterface::class.java)
        val call: Call<RegisterResponse?>? = apiInterface?.register(name, email, pwd)
        call?.enqueue(object : Callback<RegisterResponse?> {

            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>) {
                initView.hideLoading()
                if (response.isSuccessful) {
                    initView.registerSuccess()
                } else {
                    val gson = Gson()
                    val adapter = gson.getAdapter(RegisterResponse::class.java)
                    try {
                        if (response.errorBody() != null){
                            var registerResponse = adapter.fromJson(
                                    response.errorBody()?.string())
                            initView.registerFailed(registerResponse?.message)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }


            }

            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                initView.registerFailed(t.toString())
                initView.hideLoading()
                t.printStackTrace()
            }
        })
    }
}