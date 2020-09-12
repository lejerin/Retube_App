package lej.happy.retube.ui.play.replies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
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

    fun getRepliesDatas(part: String, videoId: String, maxResults: Int, key: String){

            job = Coroutines.ioThenMain(
                { repository.getRepliesData(part, videoId, maxResults, key) },
                {
                    _repliseList.value = it?.items

                }
            )

    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}