package com.example.prodiatestapps.model

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("access_token")
    var accessToken: String? = null
}