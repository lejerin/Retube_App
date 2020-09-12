package lej.happy.retube.ui.play

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lej.happy.retube.R
import lej.happy.retube.data.models.SaveTransData
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.databinding.RowItemCommentBinding
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter


class CommentsAdapter (

    private val commentList : List<Comment.Item>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object
    {
        private const val VIEW_TYPE_HEADER = 2;
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    private var sourceTarget = "ko"

    private var isNext : String? = null
    public fun setIsNext(str: String?){
        isNext = str
    }

    //번역 해시맵
    private val transHashMap: HashMap<Int, SaveTransData> = HashMap<Int, SaveTransData>()

    override fun getItemCount() = if (isNext != null) commentList.size+2 else commentList.size+1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType)
    {
        VIEW_TYPE_DATA ->
        {//inflates row layout
            CommentViewHolder(
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress,parent,false)
            ProgressViewHolder(view)
        }
        VIEW_TYPE_HEADER ->
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_header,parent,false)
            HeaderViewHolder(view)
        }
        else -> throw IllegalArgumentException("Different View type")
    }

    override fun getItemViewType(position: Int): Int
    {
        if(position == 0){
            return VIEW_TYPE_HEADER
        }
        if(position < commentList.size + 1) {
            return VIEW_TYPE_DATA
        }

        return VIEW_TYPE_PROGRESS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CommentViewHolder)
        {


            val binding = holder.recyclerviewMovieBinding

            val data = commentList[position-1]
            val title = data.snippet.topLevelComment.snippet.authorDisplayName +  " \u00b7 " +
                    Converter.formatTimeString(data.snippet.topLevelComment.snippet.publishedAt)

            binding.commentdata = data
            binding.title = title


            //한글이면 번역보기 없애기
            if(sourceTarget.equals("ko")){
                binding.transferBtn.visibility = View.GONE
            }else{
                binding.transferBtn.visibility = View.VISIBLE
            }

            //번역보기 눌렀을 때
            if(transHashMap.containsKey(position-1)){
                val getTrans = transHashMap.get(position-1)!!
                if(getTrans.isSelectTrans && getTrans.isSaveTrans){
                    binding.transferText.text = getTrans.transText
                    binding.transferText.visibility = View.VISIBLE
                }else{
                    binding.transferText.visibility = View.GONE
                }
            }


            holder.recyclerviewMovieBinding.recommentBtn.setOnClickListener {

                listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.recommentBtn, position-1)

            }


        }

    }


    inner class CommentViewHolder(
        val recyclerviewMovieBinding: RowItemCommentBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

    inner class HeaderViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView)

    inner class ProgressViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)
}