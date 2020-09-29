package com.example.shopping.data.model

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("is_join")
    val is_join : Boolean,

    @SerializedName("ID")
    val ID : String,

    @SerializedName("name")
    val name : String
    )