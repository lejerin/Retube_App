package lej.happy.retube.ui.play.replies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import lej.happy.retube.data.models.comments.Replies
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.util.Coroutines


class RepliesViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {


    private lateinit var job: Job


    private val _repliseList = MutableLiveData<List<Replies.Item>>()
    val repliseList : LiveData<List<Replies.Item>>
        get() = _repliseList

    var nextToken: String? = null
    private var firstCommentToken = true

    fun getRepliesDatas(part: String, videoId: String, maxResults: Int, key: String){

        job = Coroutines.ioThenMain(
            {
                if(nextToken != null){
                    repository.getMoreRepliesData(part, nextToken!!, videoId, maxResults, key)

                }else{
                    if(!firstCommentToken){
                        //더이상 불러올 댓글이 없음
                        job.cancel()
                    }
                    firstCommentToken = false
                    repository.getRepliesData(part, videoId, maxResults, key)

                }
            },
            {

                nextToken = it?.nextPageToken
                _repliseList.value = it?.items

            }
        )

    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}