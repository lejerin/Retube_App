package com.example.retube.data.repositories

import com.example.retube.data.network.YoutubeApi

class YoutubeRepository (
    private val api: YoutubeApi
) : SafeApiRequest(){

    suspend fun getMostPopularData(part: String, fields: String, chart: String, key: String,
                                   regionCode: String, maxResults: Int)
        = apiRequest {
            api.getMostPopular(part,fields,chart,key,regionCode,maxResults)

    }

    suspend fun getChannelData(part: String, id: String, key: String, maxResults: Int)
    =    apiRequest {
            api.getChannels(part,id,key,maxResults)
        }

}