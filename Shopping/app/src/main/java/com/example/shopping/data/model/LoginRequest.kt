package com.example.shopping.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val Account_ID : String,
    val Account_password : String
    )