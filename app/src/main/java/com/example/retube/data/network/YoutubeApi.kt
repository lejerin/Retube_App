package com.example.retube.data.network

import com.example.retube.models.Channel
import com.example.retube.models.HomeMostPopular
import com.example.retube.models.search.Searchs
import com.example.retube.models.VideoStats.VideoStats
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    //home
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

    //search
    @GET("search")
    suspend fun getSerchVideo(
        @Query("part") part: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("safeSearch") safeSearch: String,
        @Query("key") key: String
    ): Response<Searchs>

    @GET("search")
    suspend fun getMoreSerchVideo(
        @Query("part") part: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("safeSearch") safeSearch: String,
        @Query("key") key: String
    ): Response<Searchs>

    @GET("videos")
    suspend fun getVideoDetail(
        @Query("part") part: String,
        @Query("key") key: String,
        @Query("id") id: String
    ): Response<VideoStats>


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