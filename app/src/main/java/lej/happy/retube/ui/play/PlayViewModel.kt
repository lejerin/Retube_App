package lej.happy.retube.ui.play

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import lej.happy.retube.data.models.Video
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.models.viewCount
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

    var nextToken: String? = null
    private var firstCommentToken = true

    private val list: MutableList<Comment.Item> = ArrayList()
    private var isAdd = false

    fun resetCommentData(){
        list.clear()
        detectPapago.removeData()
        isAdd = false
        nextToken = null
        firstCommentToken = true
    }

    fun getCommentDatas(videoId: String, order: String, key: String){

        if(!isAdd) list.clear()
        System.out.println("요청")

        job.cancel()

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
                //총 10개 이상 될 때 까지 반복
                if(list.size >= 10){
                    _commentsList.value = list
                    System.out.println("요청결과 " + list.size)
                    isAdd = false
                }else{
                    isAdd = true
                    getCommentDatas(videoId, order, key)
                }

            }
        )

    }

    fun jobCancle(){
        job.cancel()
        _commentsList.value = list
        isAdd = false
    }


    fun setSelectedLan(num: Int){
        _commentsList.value = detectPapago.getLanList(num);
    }



    private val _videoInfo = MutableLiveData<Video>()
    val videoInfo : LiveData<Video>
        get() = _videoInfo

    fun getDetailVideo(part: String, key: String, fields: String, id: String){
        job = Coroutines.ioThenMainWithNum(
            { repository.getDetailVideo(part, key, fields, id) },
            {
                if (it != null) {
                    _videoInfo.value = it
                }
            }
        )
    }



    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}