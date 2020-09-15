package lej.happy.retube.data.models.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Searches (

    @SerializedName("kind")
    @Expose
    val kind : String,
    @SerializedName("etag")
    @Expose
    val etag : String,
    @SerializedName("nextPageToken")
    @Expose
    val nextPageToken : String,
    @SerializedName("regionCode")
    @Expose
    val regionCode : String,
    @SerializedName("pageInfo")
    @Expose
    val pageInfo : PageInfo,
    @SerializedName("items")
    @Expose
    val items : List<Items>

){

    data class PageInfo (

        @SerializedName("totalResults")
        @Expose
        val totalResults : Int,
        @SerializedName("resultsPerPage")
        @Expose
        val resultsPerPage : Int
    )

    data class Items (

        @SerializedName("kind")@Expose val kind : String,
        @SerializedName("etag")@Expose val etag : String,
        @SerializedName("id")@Expose val id : Id,
        @SerializedName("snippet")@Expose val snippet : Snippet
    )

    data class Snippet (

        @SerializedName("publishedAt")@Expose val publishedAt : String,
        @SerializedName("channelId")@Expose val channelId : String,
        @SerializedName("title")@Expose val title : String,
        @SerializedName("description")@Expose val description : String,
        @SerializedName("thumbnails")@Expose val thumbnails : Thumbnails,
        @SerializedName("channelTitle")@Expose val channelTitle : String,
        @SerializedName("liveBroadcastContent")@Expose val liveBroadcastContent : String,
        @SerializedName("publishTime")@Expose val publishTime : String
    )

    data class Id (

        @SerializedName("kind")@Expose val kind : String,
        @SerializedName("videoId")@Expose val videoId : String
    )

    data class Thumbnails (

        @SerializedName("default")@Expose val default : Default,
        @SerializedName("medium")@Expose val medium : Medium,
        @SerializedName("high")@Expose val high : High
    )

    data class High (

        @SerializedName("url")@Expose val url : String,
        @SerializedName("width")@Expose val width : Int,
        @SerializedName("height")@Expose val height : Int
    )

    data class Default (

        @SerializedName("url")@Expose val url : String,
        @SerializedName("width")@Expose val width : Int,
        @SerializedName("height")@Expose val height : Int
    )

    data class Medium (

        @SerializedName("url")@Expose val url : String,
        @SerializedName("width")@Expose val width : Int,
        @SerializedName("height")@Expose val height : Int
    )
}