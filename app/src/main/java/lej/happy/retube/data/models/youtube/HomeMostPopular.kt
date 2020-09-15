package lej.happy.retube.data.models.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class HomeMostPopular (

	@SerializedName("items")
	@Expose
	val items : List<Items>
){

	data class Items (

		@SerializedName("id")
		@Expose
		val id : String,

		@SerializedName("snippet")
		@Expose
		val snippet : Snippet,

		@SerializedName("statistics")
		@Expose
		val statistics : Statistics
	)

	data class Snippet (

		@SerializedName("publishedAt")
		@Expose
		val publishedAt : String,

		@SerializedName("channelId")
		@Expose
		val channelId : String,
		@SerializedName("title")
		@Expose
		val title : String,
		@SerializedName("thumbnails")
		@Expose
		val thumbnails : Thumbnails,
		@SerializedName("channelTitle")
		@Expose
		val channelTitle : String
	)

	data class Statistics (

		@SerializedName("viewCount")
		@Expose
		val viewCount : Int,
		@SerializedName("likeCount")
		@Expose
		val likeCount : Int,
		@SerializedName("dislikeCount")
		@Expose
		val dislikeCount : Int,
		@SerializedName("favoriteCount")
		@Expose
		val favoriteCount : Int,
		@SerializedName("commentCount")
		@Expose
		val commentCount : Int
	)


	data class Thumbnails (

		@SerializedName("default")
		@Expose
		val default : Default,
		@SerializedName("medium")
		@Expose
		val medium : Medium,
		@SerializedName("high")
		@Expose
		val high : High,
		@SerializedName("standard")
		@Expose
		val standard : Standard,
		@SerializedName("maxres")
		@Expose
		val maxres : Maxres
	)

	data class High (

		@SerializedName("url")
		@Expose
		val url : String,
		@SerializedName("width")
		@Expose
		val width : Int,
		@SerializedName("height")
		@Expose
		val height : Int
	)

	data class Default (

		@SerializedName("url")
		@Expose
		val url : String,
		@SerializedName("width")
		@Expose
		val width : Int,
		@SerializedName("height")
		@Expose
		val height : Int
	)

	data class Medium (

		@SerializedName("url")
		@Expose
		val url : String,
		@SerializedName("width")
		@Expose
		val width : Int,
		@SerializedName("height")
		@Expose
		val height : Int
	)

	data class Standard (

		@SerializedName("url")
		@Expose
		val url : String,
		@SerializedName("width")
		@Expose
		val width : Int,
		@SerializedName("height")
		@Expose
		val height : Int
	)

	data class Maxres (

		@SerializedName("url")
		@Expose
		val url : String,
		@SerializedName("width")
		@Expose
		val width : Int,
		@SerializedName("height")
		@Expose
		val height : Int
	)

}
