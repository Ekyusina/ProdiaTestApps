package com.example.prodiatestapps.ui.register

class RegisterView {
    interface InitView {
        fun showLoading()
        fun hideLoading()
        fun registerSuccess()
        fun registerFailed(errorMessage: String?)
    }

    interface Register {
        fun register(name:String?, email: String?, pwd: String?)
    }
}