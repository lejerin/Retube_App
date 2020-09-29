package com.example.shopping.data.network

import com.example.shopping.data.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ShopApi {

    //회원가입
    @POST("account/signup")
    suspend fun signUpUser(
        @Body param : SignUpRequest
    ): Response<SignUpResponse>

    //로그인
    @POST("account/signin/{Account_id}/")
    suspend fun signInUser(
        @Path("Account_id") Account_id: String,
        @Body param : LoginRequest
    ): Response<LoginResponse>

    //상품 목록 불러오기
    @GET("product/")
    suspend fun getProduct(
    ): Response<List<ProductResponse>>

    //상품 목록 불러오기
    @POST("product/")
    suspend fun uploadProduct(
    ): Response<String>



    companion object{
        operator fun invoke() : ShopApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://172.30.1.12:8000/")
                .build()
                .create(ShopApi::class.java)
        }
    }

}