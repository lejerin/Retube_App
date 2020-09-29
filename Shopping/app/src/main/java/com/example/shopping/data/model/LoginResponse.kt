package com.example.shopping.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("is_login")
    val is_login : Boolean,

    @SerializedName("ID")
    val ID : String,

    @SerializedName("name")
    val name : String
    )