package lej.happy.retube.data.network;


import lej.happy.retube.data.models.youtube.Channel;
import lej.happy.retube.data.models.youtube.Searches;
import lej.happy.retube.data.models.youtube.VideoStats;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {



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
    Call<Searches> getSerchVideo(
            @Query("part") String part,
            @Query("maxResults") int maxResults,
            @Query("order") String order,
            @Query("type") String type,
            @Query("q") String q,
            @Query("safeSearch") String safeSearch,
            @Query("key") String key
    );





}
