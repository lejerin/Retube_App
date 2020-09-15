package lej.happy.retube.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.Job
import lej.happy.retube.data.models.ChannelStat
import lej.happy.retube.data.models.Realm.ViewChannel
import lej.happy.retube.data.models.youtube.Channel
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.util.Coroutines


class StatViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {

    private lateinit var job: Job


    private val _list = HashMap<Int, ChannelStat>()
    private val _channels = MutableLiveData<List<ChannelStat>>()
    val chennels : LiveData<List<ChannelStat>>
        get() = _channels

    private fun getChannelDatas(part: String, id: String, key: String, maxResults: Int, num: Int, count: Int){
        job = Coroutines.ioThenMainWithNum(
            { repository.getChannelData(part, id, key, maxResults) },
            {
                if (it != null) {
                    _list.put(num, ChannelStat(it.items[0].snippet.title, count, it.items[0].snippet.thumbnails.high.url))

                    if(_list.size == 3){
                        _channels.value = ArrayList(_list.values)
                    }
                }
            }
        )
    }

    fun getChannelData(key: String) {
        val realm = Realm.getDefaultInstance()

        val viewChannels: RealmResults<ViewChannel> = realm.where(ViewChannel::class.java)
            .sort("channelCount", Sort.DESCENDING)
            .findAll()

        System.out.println("채널" + viewChannels.size)

        for(i in viewChannels.indices){
            getChannelDatas("snippet", viewChannels[i]?.channelId!!,
                key, 10, i, viewChannels[i]?.channelCount!!)
        }
    }

    //키워드 갖고오기


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}