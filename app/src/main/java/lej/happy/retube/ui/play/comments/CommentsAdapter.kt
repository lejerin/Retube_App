package lej.happy.retube.ui.play.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import lej.happy.retube.R
import lej.happy.retube.data.models.youtube.SaveTransData
import lej.happy.retube.data.models.youtube.Comments
import lej.happy.retube.databinding.RowItemCommentBinding
import lej.happy.retube.helper.TransferPapago
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter


class CommentsAdapter (
    private val commentList : List<Comments.Item>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    private val transferPapago = TransferPapago()
    //번역 리스트
    private val transHashMap: HashMap<Int, SaveTransData> = HashMap<Int, SaveTransData>()

    private var isNext : String? = null
    fun setIsNext(str: String?){
        isNext = str
    }

    private var sourceTarget = 0
    fun setSourceTarget(num: Int){
        sourceTarget = num
    }

    override fun getItemCount() = if (isNext != null) commentList.size+1 else commentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType)
    {
        VIEW_TYPE_DATA ->
        {//inflates row layout
            CommentsViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_item_comment,
                    parent,
                    false
                )
            )
        }
        VIEW_TYPE_PROGRESS ->
        {//inflates progressbar layout
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_footer,parent,false)
            ProgressViewHolder(view)
        }
        else -> throw IllegalArgumentException("Different View type")
    }

    override fun getItemViewType(position: Int): Int
    {
        if(position < commentList.size) {
            return VIEW_TYPE_DATA
        }

        return VIEW_TYPE_PROGRESS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CommentsViewHolder)
        {
            holder.recyclerviewMovieBinding.commentdata = commentList[position]
            holder.recyclerviewMovieBinding.title =  commentList[position].snippet.topLevelComment.snippet.authorDisplayName +  " \u00b7 " +
                    Converter.formatTimeString(commentList[position].snippet.topLevelComment.snippet.publishedAt)


            //한글이면 번역보기 없애기
            if(sourceTarget == 0){
                holder.recyclerviewMovieBinding.transferBtn.visibility = View.GONE
            }else{
                holder.recyclerviewMovieBinding.transferBtn.visibility = View.VISIBLE
            }

            //번역보기 눌렀을 때
            if(transHashMap.containsKey(position)){
                val getTrans = transHashMap.get(position)!!
                if(getTrans.isSelectTrans){
                    holder.recyclerviewMovieBinding.transferText.text = getTrans.transText
                    holder.recyclerviewMovieBinding.transferText.visibility = View.VISIBLE
                }else{
                    holder.recyclerviewMovieBinding.transferText.visibility = View.GONE
                }
            }

            holder.recyclerviewMovieBinding.transferBtn.setOnClickListener {

                if(transHashMap.containsKey(position)){
                    val getTrans = transHashMap.get(position)!!
                    if(getTrans.isSelectTrans){
                        getTrans.isSelectTrans = false
                        transHashMap.put(position, getTrans)
                        holder.recyclerviewMovieBinding.transferBtn.text = "번역보기"
                        holder.recyclerviewMovieBinding.transferText.visibility = View.GONE

                    }else{
                        getTrans.isSelectTrans = true
                        transHashMap.put(position, getTrans)
                        holder.recyclerviewMovieBinding.transferBtn.text = "번역숨기기"
                        holder.recyclerviewMovieBinding.transferText.text = getTrans.transText
                        holder.recyclerviewMovieBinding.transferText.visibility = View.VISIBLE
                    }
                }else{
                    CoroutineScope(Job() + Dispatchers.Main).launch(Dispatchers.Default) {
                        val result = async {
                            transferPapago.startTransfer(commentList[position].snippet.topLevelComment.snippet.textDisplay, sourceTarget, "ko")
                        }.await()
                        withContext(Dispatchers.Main) {
                            transHashMap.put(position,
                                SaveTransData(
                                    true,
                                    result
                                )
                            )
                            holder.recyclerviewMovieBinding.transferText.text = result
                            holder.recyclerviewMovieBinding.transferText.visibility = View.VISIBLE
                        }
                    }
                }

            }


            holder.recyclerviewMovieBinding.recommentBtn.setOnClickListener {

                listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.recommentBtn, position)

            }
        }

        if( holder is ProgressViewHolder){

            holder.moreCommentBtn.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.moreCommentBtn, -9)
            }

        }

    }

    inner class CommentsViewHolder(
        val recyclerviewMovieBinding: RowItemCommentBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

    inner class ProgressViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val moreCommentBtn: TextView = view.findViewById(R.id.moreCommentBtn)

    }



}