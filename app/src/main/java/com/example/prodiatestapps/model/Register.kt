package com.example.prodiatestapps.model

import com.google.gson.annotations.SerializedName

class Register {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("email")
    var email: String? = null
}