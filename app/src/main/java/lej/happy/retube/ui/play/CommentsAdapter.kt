package lej.happy.retube.ui.play

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lej.happy.retube.R
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
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    private var isNext : String? = null
    public fun setIsNext(str: String?){
        isNext = str
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress,parent,false)
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


            holder.recyclerviewMovieBinding.recommentBtn.setOnClickListener {

                listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.recommentBtn, position)

            }
        }

    }

    inner class CommentsViewHolder(
        val recyclerviewMovieBinding: RowItemCommentBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

    inner class ProgressViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)
}