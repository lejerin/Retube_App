package com.example.mapapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID")
    val ID : String,

    @SerializedName("pw")
    val pw : String
)