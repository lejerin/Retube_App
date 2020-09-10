package lej.happy.retube.ui.search

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lej.happy.retube.helper.WiseNLUExample
import lej.happy.retube.data.Realm.RealmSearch
import lej.happy.retube.data.models.search.Item
import lej.happy.retube.data.models.viewCount
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.util.Coroutines
import io.realm.Realm
import kotlinx.coroutines.Job
import java.util.*
import kotlin.collections.ArrayList


class SearchViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {

    private lateinit var job: Job

    var newSearchText = ""
    private val wiseNLUExample = WiseNLUExample()

    fun find(view: View){
        view.hideKeyboard()
        saveNoun((wiseNLUExample.getNoun(newSearchText)))
        getSearchDatas("snippet", 10, "relevance", "video",
            newSearchText,"none","AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0")
    }
    fun findMore(view: View){
        if(nextToken != null){
            getMoreSearchDatas("snippet", nextToken!! , 10, "relevance", "video",
                newSearchText,"none","AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0")
        }
    }


    private val _searchdatas = MutableLiveData<MutableList<Item>>()
    val searchdatas : LiveData<MutableList<Item>>
        get() = _searchdatas

    var nextToken : String? = null

    init {
        _searchdatas.value = ArrayList()
    }

    fun getSearchDatas(part: String, maxResults: Int, order: String, type: String, q: String, safeSearch: String, key: String){
        job = Coroutines.ioThenMain(
            { repository.getSearchData(part, maxResults, order, type, q, safeSearch, key) },
            {
                nextToken = it?.nextPageToken
                _searchdatas.value = it!!.items
            }
        )
    }


    fun getMoreSearchDatas(part: String, pageToken: String, maxResults: Int, order: String, type: String, q: String, safeSearch: String, key: String){
        job = Coroutines.ioThenMain(
            { repository.getMoreSearchData(part, pageToken, maxResults, order, type, q, safeSearch, key) },
            {

                nextToken = it?.nextPageToken
//                val arr = _searchdatas.value
//                arr?.addAll(it!!.items)
//                _searchdatas.value = arr
                _searchdatas.value = it!!.items
                System.out.println("loading more" + _searchdatas.value!!.size)
            }
        )
    }

    //조회수 불러오기

    private val _viewCount = MutableLiveData<viewCount>()
    val viewCount : LiveData<viewCount>
        get() = _viewCount

    fun getViewCountDatas(part: String,  key: String, id: String, num: Int){
        job = Coroutines.ioThenMainWithNum(
            { repository.getViewDetailData(part, key, id) },
            {
                if (it != null) {
                    _viewCount.value = viewCount(num, it.items[0].statistics.viewCount.toInt())
                }
            }
        )
    }

    fun saveNoun(list: List<String>){
        val realm: Realm = Realm.getDefaultInstance()

        for (i in 0..list.size-1){
            val isRealmSearch: RealmSearch? =
                realm.where(RealmSearch::class.java).equalTo("noun", list[i]).findFirst()
            if (isRealmSearch != null){
                realm.executeTransaction {
                    isRealmSearch.setCount(isRealmSearch.getCount() + 1)
                    isRealmSearch.setDate(Date())
                }
            }else{
                realm.executeTransaction { realm ->
                    val search: RealmSearch = realm.createObject(RealmSearch::class.java)
                    search.setNoun(list[i])
                    search.setCount(1)
                    search.setDate(Date())
                }
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}