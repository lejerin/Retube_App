package lej.happy.retube.data.network

import lej.happy.retube.data.models.Channel
import lej.happy.retube.data.models.HomeMostPopular
import lej.happy.retube.data.models.VideoStats.VideoStats
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.models.search.Searchs
import retrofit2.Call
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

    //댓글
    @GET("commentThreads")
    suspend fun getCommentsData(
        @Query("part") part: String,
        @Query("videoId") videoId: String,
        @Query("order") order: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Comment.Model>


    @GET("commentThreads")
    suspend fun getMoreCommentData(
        @Query("part") part: String,
        @Query("videoId") videoId: String,
        @Query("order") order: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): Response<Comment.Model>


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