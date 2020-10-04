package com.example.shopping.data.network

import com.example.shopping.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    //상품 목록 보내기
    @Multipart
    @POST("product/create")
    suspend fun uploadProduct(
        @Part file: MultipartBody.Part,
        @Part("product_name") product_name: RequestBody,
        @Part("product_detail") product_detail: RequestBody,
        @Part("product_price") product_price: RequestBody,
        @Part("product_stock") product_stock: RequestBody,
        @Part("product_major_category") product_major_category: RequestBody,
        @Part("product_minor_category") product_minor_category: RequestBody,
        @Part("product_merchandiser") product_merchandiser: RequestBody
    ): Response<UploadResponse>


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