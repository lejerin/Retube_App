package lej.happy.retube.data.models.youtube

import com.google.gson.annotations.SerializedName

data class Channel (

    @SerializedName("kind") val kind : String,
    @SerializedName("etag") val etag : String,
    @SerializedName("pageInfo") val pageInfo : PageInfo,
    @SerializedName("items") val items : List<Items>

) {

    data class Items (

        @SerializedName("kind") val kind : String,
        @SerializedName("etag") val etag : String,
        @SerializedName("id") val id : String,
        @SerializedName("snippet") val snippet : Snippet
    )

    data class PageInfo (

        @SerializedName("resultsPerPage") val resultsPerPage : Int
    )

    data class Snippet (

        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String,
        @SerializedName("customUrl") val customUrl : String,
        @SerializedName("publishedAt") val publishedAt : String,
        @SerializedName("thumbnails") val thumbnails : Thumbnails,
        @SerializedName("localized") val localized : Localized,
        @SerializedName("country") val country : String
    )

    data class Localized (

        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String
    )

    data class Thumbnails (

        @SerializedName("default") val default : Default,
        @SerializedName("medium") val medium : Medium,
        @SerializedName("high") val high : High
    )

    data class High (

        @SerializedName("url") val url : String,
        @SerializedName("width") val width : Int,
        @SerializedName("height") val height : Int
    )

    data class Default (

        @SerializedName("url") val url : String,
        @SerializedName("width") val width : Int,
        @SerializedName("height") val height : Int
    )

    data class Medium (

        @SerializedName("url") val url : String,
        @SerializedName("width") val width : Int,
        @SerializedName("height") val height : Int
    )
}