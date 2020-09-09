package com.example.retube.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retube.R
import com.example.retube.databinding.RowItemSearchBinding
import com.example.retube.models.search.Item
import com.example.retube.util.Converter


class SearchAdapter (
    private val videoList : List<Item>,
    private val viewCountList : HashMap<Int, Int>
   // private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){

    override fun getItemCount() = videoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_item_search,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.recyclerviewMovieBinding.search = videoList[position]
        holder.recyclerviewMovieBinding.date =  Converter.formatTimeString(videoList[position].snippet.publishedAt)
//        + " \u00b7 " + Converter.getNumlength(videoList[position].statistics.viewCount) + "회"

        if(viewCountList.get(position) != null){
            holder.recyclerviewMovieBinding.date +=  " \u00b7 " + Converter.getNumlength(viewCountList[position].toString()) + "회"
        }

    }

    inner class SearchViewHolder(
        val recyclerviewMovieBinding: RowItemSearchBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

}