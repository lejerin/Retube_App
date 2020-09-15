package lej.happy.retube.data.models.youtube

import com.google.gson.annotations.SerializedName

data class Video (

    @SerializedName("items") val items : List<Items>
){
    data class Items (

        @SerializedName("id") val id : String,
        @SerializedName("snippet") val snippet : Snippet,
        @SerializedName("statistics") val statistics : Statistics
    )

    data class Snippet (

        @SerializedName("publishedAt") val publishedAt : String,
        @SerializedName("channelId") val channelId : String,
        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String,
        @SerializedName("channelTitle") val channelTitle : String,
        @SerializedName("tags") val tags : List<String>,
        @SerializedName("categoryId") val categoryId : Int
    )

    data class Statistics (

        @SerializedName("viewCount") val viewCount : Int,
        @SerializedName("likeCount") val likeCount : Int,
        @SerializedName("dislikeCount") val dislikeCount : Int,
        @SerializedName("favoriteCount") val favoriteCount : Int,
        @SerializedName("commentCount") val commentCount : Int
    )
}