package lej.happy.retube.data.network

import lej.happy.retube.data.models.youtube.*
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
    ): Response<Searches>

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
    ): Response<Searches>

    @GET("videos")
    suspend fun getVideoDetail(
        @Query("part") part: String,
        @Query("key") key: String,
        @Query("id") id: String
    ): Response<VideoStats>

    //댓글
    @GET("commentThreads")
    suspend fun getCommentsData(
        @Query("part") part: String,
        @Query("videoId") videoId: String,
        @Query("order") order: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Comments.Model>


    @GET("commentThreads")
    suspend fun getMoreCommentData(
        @Query("part") part: String,
        @Query("videoId") videoId: String,
        @Query("order") order: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Comments.Model>

    //대댓글

    @GET("comments")
    suspend fun getRepliesData(
        @Query("part") part: String,
        @Query("parentId") videoId: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Replies>

    @GET("comments")
    suspend fun getMoreRepliesData(
        @Query("part") part: String,
        @Query("pageToken") pageToken: String,
        @Query("parentId") videoId: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Replies>

    //플레이 비디오 자세한 정보
    @GET("videos")
    suspend fun getPlayVideo(
        @Query("part") part: String,
        @Query("key") key: String,
        @Query("fields") fields: String,
        @Query("id") id: String
    ): Response<Video>


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