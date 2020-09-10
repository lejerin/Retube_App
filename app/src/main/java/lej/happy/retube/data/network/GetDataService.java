package lej.happy.retube.data.network;


import lej.happy.retube.data.models.Channel;
import lej.happy.retube.data.models.Video;
import lej.happy.retube.data.models.VideoStats.VideoStats;
import lej.happy.retube.data.models.comments.Comment;
import lej.happy.retube.data.models.comments.Replies;
import lej.happy.retube.data.models.search.Searchs;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {


    @GET("videos")
    Call<Video> getPlayVideo(
            @Query("part") String part,
            @Query("key") String key,
            @Query("fields") String fields,
            @Query("id") String id
    );


    @GET("comments")
    Call<Replies> getRepliesData(
            @Query("part") String part,
            @Query("parentId") String videoId,
            @Query("maxResults") int maxResults,
            @Query("key") String key
    );





    @GET("videos")
    Call<VideoStats> getVideoDetail(
            @Query("part") String part,
            @Query("key") String key,
            @Query("id") String id
    );

    @GET("channels")
    Call<Channel> getChannels(
            @Query("part") String part,
            @Query("id") String id,
            @Query("key") String key,
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





}
