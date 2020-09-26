package lej.happy.retube.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.data.models.youtube.Channel
import lej.happy.retube.util.Coroutines
import kotlinx.coroutines.Job
import lej.happy.retube.data.models.youtube.HomeMostPopular

class HomeViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {

    private lateinit var job: Job

    private val _homedatas = MutableLiveData<HomeMostPopular>()
    val homedatas : LiveData<HomeMostPopular>
        get() = _homedatas

    fun getHomeDatas(part: String, fields: String, chart: String, key: String, regionCode: String, maxResults: Int){
        job = Coroutines.ioThenMain(
            { repository.getMostPopularData(part, fields, chart, key, regionCode, maxResults) },
            {
                _homedatas.value = it
            }
        )

    }

    private val _list = HashMap<Int, Channel.Items>()
    private val _channels = MutableLiveData<HashMap<Int, Channel.Items>>()
    val chennels : LiveData<HashMap<Int, Channel.Items>>
        get() = _channels

    fun getChannelDatas(part: String, id: String, key: String, maxResults: Int, num: Int){
        job = Coroutines.ioThenMainWithNum(
            { repository.getChannelData(part, id, key, maxResults) },
            {
                if (it != null) {
                    _list.put(num,it.items.get(0))
                    _channels.value = _list
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}