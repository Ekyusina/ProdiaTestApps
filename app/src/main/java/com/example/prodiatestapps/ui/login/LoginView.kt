package com.example.prodiatestapps.ui.login

class LoginView {
    interface InitView {
        fun showLoading()
        fun hideLoading()
        fun loginSuccess()
        fun loginFailed(errorMessage: String?)
    }

    interface Login {
        fun login(email: String?, pwd: String?)
    }
}