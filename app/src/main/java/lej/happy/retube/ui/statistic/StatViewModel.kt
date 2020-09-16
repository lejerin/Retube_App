package lej.happy.retube.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.Job
import lej.happy.retube.data.models.ChannelStat
import lej.happy.retube.data.models.Keyword
import lej.happy.retube.data.models.Realm.ViewChannel
import lej.happy.retube.data.models.UserStat
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.util.Coroutines
import lej.happy.retube.util.RealmUtil


class StatViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {

    private lateinit var job: Job
    private val realm = Realm.getDefaultInstance()

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


        val viewChannels: RealmResults<ViewChannel> = realm.where(ViewChannel::class.java)
            .sort("channelCount", Sort.DESCENDING)
            .findAll()

        System.out.println("채널" + viewChannels.size)

        for(i in viewChannels.indices){
            getChannelDatas("snippet", viewChannels[i]?.channelId!!,
                key, 10, i, viewChannels[i]?.channelCount!!)
        }
    }

    //유저 시청 정보(count, time)
    private val _userData = MutableLiveData<UserStat>()
    val userData : LiveData<UserStat>
        get() = _userData


    fun getUserData(){
        _userData.value = RealmUtil.getUserData()
    }

    fun getPieChartData(): PieData = RealmUtil.getPieChartList()

    fun getKeyWordSearch(checkedId: Int): Keyword {
        val list = RealmUtil.getKeyWordSearch(checkedId)
        val keyword = Keyword()
        for(i in 0..8){
            if(i < list!!.size) {
                var text: String = list?.get(i)?.noun ?: ""
                if (text.length > 5) {
                    text = text.substring(0, 5) + ".."
                }
                keyword.list[i] = text
            }
        }

        return keyword
    }

    fun getKeyWordView(checkedId: Int): Keyword {
        val list = RealmUtil.getKeyWordView(checkedId)
        val keyword = Keyword()
        for(i in 0..8){
            if(i < list!!.size) {
                var text: String = list?.get(i)?.noun ?: ""
                if (text.length > 5) {
                    text = text.substring(0, 5) + ".."
                }
                keyword.list[i] = text
            }
        }

        return keyword
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}