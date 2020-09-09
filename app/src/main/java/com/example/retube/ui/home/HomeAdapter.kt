package com.example.retube.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retube.R
import com.example.retube.databinding.RowItemHomeBinding
import com.example.retube.data.models.Channel
import com.example.retube.data.models.HomeMostPopular
import com.example.retube.ui.RecyclerViewClickListener
import com.example.retube.util.Converter

class HomeAdapter (
    private val videoList : List<HomeMostPopular.Item>,
    private val channelList : HashMap<Int, Channel.Item>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    override fun getItemCount() = videoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HomeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_item_home,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.recyclerviewMovieBinding.home = videoList[position]
        holder.recyclerviewMovieBinding.subtitle = videoList[position].snippet.channelTitle +
                " \u00b7 " +
                Converter.getNumlength(videoList[position].statistics.viewCount) + "회" +
                 " \u00b7 " +
                Converter.formatTimeString(videoList[position].snippet.publishedAt)

        var url = ""
        if(channelList.get(position) != null){
            url =  channelList.get(position)!!.snippet.thumbnails.high.url
        }
        holder.recyclerviewMovieBinding.channelUri = url


        holder.recyclerviewMovieBinding.homeView.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.homeView , position)
        }

    }

    inner class HomeViewHolder(
        val recyclerviewMovieBinding: RowItemHomeBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)

}