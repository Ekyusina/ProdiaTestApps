package com.example.prodiatestapps.model

import com.google.gson.annotations.SerializedName

class RegisterResponse {
    @SerializedName("status")
    var status: Boolean? = false
    @SerializedName("status_code")
    var statusCode: Int? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("data")
    var data: Register? = null
}