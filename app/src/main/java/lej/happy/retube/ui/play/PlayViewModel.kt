package lej.happy.retube.ui.play

import android.util.SparseBooleanArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.models.comments.Replies
import lej.happy.retube.data.models.repliesData
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.helper.DetectPapago
import lej.happy.retube.util.Coroutines
import java.util.*


class PlayViewModel(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val detectPapago = DetectPapago()
    private lateinit var job: Job

    private val _commentsList = MutableLiveData<List<Comment.Item>>()
    val commentsList : LiveData<List<Comment.Item>>
        get() = _commentsList

    private var nextToken: String? = null
    private var firstCommentToken = true

    private val list: MutableList<Comment.Item> = ArrayList()
    private var isAdd = false

    fun getCommentDatas(videoId: String, order: String, key: String){

        if(!isAdd) list.clear()

        job = Coroutines.ioThenMain(
            {
                if(nextToken != null){
                    repository.getMoreCommentData("snippet", videoId, order, nextToken!!,  10, key)

                }else{
                    if(!firstCommentToken){
                        //더이상 불러올 댓글이 없음
                       job.cancel()
                    }
                    firstCommentToken = false
                    repository.getCommentData("snippet", videoId, order, 10, key)

                }

            },
            {

                nextToken = it?.getnextPageToken()

                //언어 체크 한 뒤 배열에 넣기
                list.addAll(detectPapago.analyzeList(it?.items))
                System.out.println("사이즈" + list.size)
                //총 10개 이상 될 때 까지 반복
                if(list.size >= 10){
                    _commentsList.value = list
                    isAdd = false
                }else{
                    isAdd = true
                    getCommentDatas(videoId, order, key)
                }


            }
        )

    }


//    private val _repliseList = MutableLiveData<repliesData>()
//    val repliseList : LiveData<repliesData>
//        get() = _repliseList
//
//    //선택했을 때 보이도록
//    private val mSelectedReplies = SparseBooleanArray(0)
//    fun getSelectReplies(count: Int): Boolean{
//        return mSelectedReplies.get(count,false)
//    }
//
//
//    private val repliesHashMap: HashMap<Int, List<Replies.Item>> = HashMap<Int, List<Replies.Item>>()
//    fun getReplies(count: Int) : List<Replies.Item>?{
//
//        System.out.println("채ㅕㅜㅅ" +count)
//        return repliesHashMap.get(count)
//    }
//
//    fun getRepliesDatas(part: String, videoId: String, maxResults: Int, key: String, num: Int){
//
//        mSelectedReplies.put(num, !mSelectedReplies.get(num, false))
//
//        if(!repliesHashMap.containsKey(num)){
//            job = Coroutines.ioThenMain(
//                { repository.getRepliesData(part, videoId, maxResults, key) },
//                {
//                    repliesHashMap.put(num, it!!.items)
//                    _repliseList.value = repliesData(num, it!!.items)
//
//                }
//            )
//        }else{
//            _repliseList.value = repliesData(num, null)
//        }
//
//    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}