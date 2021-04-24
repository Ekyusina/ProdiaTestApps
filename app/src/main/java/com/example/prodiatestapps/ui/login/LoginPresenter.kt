package com.example.prodiatestapps.ui.login

import com.example.prodiatestapps.apiservice.ApiClient
import com.example.prodiatestapps.apiservice.ApiInterface
import com.example.prodiatestapps.model.LoginResponse
import com.example.prodiatestapps.model.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginPresenter(initView: LoginView.InitView) : LoginView.Login {
    private val initView: LoginView.InitView

    init {
        this.initView = initView
    }

    override fun login(email: String?, pwd: String?) {
        initView.showLoading()
        val apiInterface: ApiInterface? = ApiClient().getRetrofitInstance()?.create(ApiInterface::class.java)
        val call: Call<LoginResponse?>? = apiInterface?.login(email, pwd)
        call?.enqueue(object : Callback<LoginResponse?> {

            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                initView.hideLoading()

                if (response.isSuccessful) {
                    if (response?.body()?.data?.accessToken != null){
                        initView.loginSuccess()
                    }
                } else {
                    val gson = Gson()
                    val adapter = gson.getAdapter(RegisterResponse::class.java)
                    try {
                        if (response.errorBody() != null){
                            var registerResponse = adapter.fromJson(
                                    response.errorBody()!!.string())
                            initView.loginFailed(registerResponse?.message)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                initView.loginFailed(t.toString())
                initView.hideLoading()
                t.printStackTrace()
            }
        })
    }
}