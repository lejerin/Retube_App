package com.example.shopping.data.model

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

    @SerializedName("Account_ID")
    val Account_ID : String,

    @SerializedName("Account_email")
    val Account_email : String,

    @SerializedName("Account_name")
    val Account_name : String,

    @SerializedName("Account_password")
    val Account_password : String,

    @SerializedName("confirm_password")
    val confirm_password : String,

    @SerializedName("Account_age")
    val Account_age : Int?,

    @SerializedName("Account_sex")
    val Account_sex : String

)