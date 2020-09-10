package lej.happy.retube.data.repositories

import lej.happy.retube.data.network.YoutubeApi

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

    //search
    suspend fun getSearchData(part: String, maxResults: Int, order: String, type: String, q: String,
                              safeSearch: String, key: String)
            = apiRequest {
        api.getSerchVideo(part, maxResults, order, type, q, safeSearch, key)

    }

    suspend fun getMoreSearchData(part: String, pageToken: String, maxResults: Int, order: String, type: String, q: String,
                              safeSearch: String, key: String)
            = apiRequest {
        api.getMoreSerchVideo(part, pageToken, maxResults, order, type, q, safeSearch, key)

    }

    //조회수
    suspend fun getViewDetailData(part: String, key: String, id: String)
            = apiRequest {
        api.getVideoDetail(part, key, id)

    }

    //댓글
    suspend fun getCommentData(part: String,  videoId: String, order: String, maxResults: Int, key: String)
            = apiRequest {
        api.getCommentsData(part, videoId, order, maxResults, key)

    }

    suspend fun getMoreCommentData(part: String,  videoId: String, order: String, pageToken: String,
                                   maxResults: Int, key: String)
            = apiRequest {
        api.getMoreCommentData(part, videoId, order, pageToken, maxResults, key)

    }

}