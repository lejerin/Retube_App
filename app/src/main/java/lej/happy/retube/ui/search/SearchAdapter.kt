package lej.happy.retube.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import lej.happy.retube.R
import lej.happy.retube.databinding.RowItemSearchBinding
import lej.happy.retube.data.models.search.Item
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter


class SearchAdapter (
    private val videoList : List<Item>,
    private val viewCountList : HashMap<Int, Int>,
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

    override fun getItemCount() = if (isNext != null) videoList.size+1 else videoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType)
        {
            VIEW_TYPE_DATA ->
            {//inflates row layout
                SearchViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.row_item_search,
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
        if(position < videoList.size) {
            return VIEW_TYPE_DATA
        }

        return VIEW_TYPE_PROGRESS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SearchViewHolder)
        {
            holder.recyclerviewMovieBinding.search = videoList[position]
            holder.recyclerviewMovieBinding.date =  Converter.formatTimeString(videoList[position].snippet.publishedAt)
//        + " \u00b7 " + Converter.getNumlength(videoList[position].statistics.viewCount) + "회"

            if(viewCountList.get(position) != null){
                holder.recyclerviewMovieBinding.date +=  " \u00b7 " + Converter.getNumlength(viewCountList[position].toString()) + "회"
            }

            holder.recyclerviewMovieBinding.searchView.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.searchView , position)
            }
        }

    }

    inner class SearchViewHolder(
        val recyclerviewMovieBinding: RowItemSearchBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

    inner class ProgressViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)
}