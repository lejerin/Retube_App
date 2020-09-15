package lej.happy.retube.ui.play.replies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lej.happy.retube.R
import lej.happy.retube.data.models.youtube.Replies
import lej.happy.retube.databinding.RowItemRepliesCommentBinding
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter


class RepliesAdapter (
    private val repliesList : List<Replies.Item>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object
    {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    private var isNext : String? = null
    fun setIsNext(str: String?){
        isNext = str
    }

    override fun getItemCount() = if (isNext != null) repliesList.size+1 else repliesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType)
    {
        VIEW_TYPE_DATA ->
        {//inflates row layout
            CommentsViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_item_replies_comment,
                    parent,
                    false
                )
            )
        }
        VIEW_TYPE_PROGRESS ->
        {//inflates progressbar layout
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_replies_footer,parent,false)
            ProgressViewHolder(view)
        }
        else -> throw IllegalArgumentException("Different View type")
    }

    override fun getItemViewType(position: Int): Int
    {
        if(position < repliesList.size) {
            return VIEW_TYPE_DATA
        }

        return VIEW_TYPE_PROGRESS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CommentsViewHolder)
        {
            holder.recyclerviewMovieBinding.repliesdata = repliesList[position]
            holder.recyclerviewMovieBinding.title =  repliesList[position].snippet.authorDisplayName +  " \u00b7 " +
                    Converter.formatTimeString(repliesList[position].snippet.publishedAt)

        }

        if( holder is ProgressViewHolder){

            holder.moreCommentBtn.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.moreCommentBtn, -9)
            }

        }

    }

    inner class CommentsViewHolder(
        val recyclerviewMovieBinding: RowItemRepliesCommentBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

    inner class ProgressViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val moreCommentBtn: TextView = view.findViewById(R.id.moreCommentBtn)

    }



}