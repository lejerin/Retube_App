package lej.happy.retube.data.models.youtube

import com.google.gson.annotations.SerializedName

data class VideoStats (

    @SerializedName("items") val items : List<Items>
){
    data class Items (

        @SerializedName("statistics") val statistics : Video.Statistics
    )

    data class Statistics (

        @SerializedName("viewCount") val viewCount : Int,
        @SerializedName("likeCount") val likeCount : Int,
        @SerializedName("dislikeCount") val dislikeCount : Int,
        @SerializedName("commentCount") val commentCount : Int
    )
}