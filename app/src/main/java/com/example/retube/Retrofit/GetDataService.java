package com.example.retube.Retrofit;

import com.example.retube.models.Channel.ChannelList;
import com.example.retube.models.Home.First;
import com.example.retube.models.Search.Searchs;
import com.example.retube.models.VideoStats.VideoStats;
import com.example.retube.models.comments.Comment;
import com.example.retube.models.comments.Replies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {


    @GET("comments")
    Call<Replies> getRepliesData(
            @Query("part") String part,
            @Query("parentId") String videoId,
            @Query("maxResults") int maxResults,
            @Query("key") String key
    );


    @GET("commentThreads")
    Call<Comment.Model> getCommentsData(
            @Query("part") String part,
            @Query("videoId") String videoId,
            @Query("order") String order,
            @Query("maxResults") int maxResults,
            @Query("key") String key
    );

    @GET("commentThreads")
    Call<Comment.Model> getMoreCommentData(
            @Query("part") String part,
            @Query("videoId") String videoId,
            @Query("order") String order,
            @Query("pageToken") String pageToken,
            @Query("maxResults") int maxResults,
            @Query("key") String key
    );

    @GET("videos")
    Call<First> getVideoTitle(
            @Query("part") String part,
            @Query("key") String key,
            @Query("id") String id
    );

    @GET("videos")
    Call<VideoStats> getVideoDetail(
            @Query("part") String part,
            @Query("key") String key,
            @Query("id") String id
    );

    @GET("channels")
    Call<ChannelList> getChannels(
            @Query("part") String part,
            @Query("id") String id,
            @Query("key") String key,
            @Query("maxResults") int maxResults
    );

    @GET("videos")
    Call<First> getMostPopular(
            @Query("part") String part,
            @Query("chart") String chart,
            @Query("key") String key,
            @Query("regionCode") String regionCode,
            @Query("maxResults") int maxResults
    );

    @GET("search")
    Call<Searchs> getSerchVideo(
            @Query("part") String part,
            @Query("maxResults") int maxResults,
            @Query("order") String order,
            @Query("type") String type,
            @Query("q") String q,
            @Query("safeSearch") String safeSearch,
            @Query("key") String key
    );

    @GET("search")
    Call<Searchs> getMoreSerchVideo(
            @Query("part") String part,
            @Query("pageToken") String pageToken,
            @Query("maxResults") int maxResults,
            @Query("order") String order,
            @Query("type") String type,
            @Query("q") String q,
            @Query("safeSearch") String safeSearch,
            @Query("key") String key
    );

}
