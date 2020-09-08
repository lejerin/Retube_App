package com.example.retube.data.network

import com.example.retube.models.Channel
import com.example.retube.models.HomeMostPopular
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("channels")
    suspend fun getChannels(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int
    ): Response<Channel>

    @GET("videos")
    suspend fun getMostPopular(
        @Query("part") part: String,
        @Query("fields") fields: String,
        @Query("chart") chart: String,
        @Query("key") key: String,
        @Query("regionCode") regionCode: String,
        @Query("maxResults") maxResults: Int
    ): Response<HomeMostPopular>

    companion object{
        operator fun invoke() : YoutubeApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .build()
                .create(YoutubeApi::class.java)
        }
    }


}