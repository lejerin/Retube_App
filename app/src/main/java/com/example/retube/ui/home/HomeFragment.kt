package com.example.retube.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retube.R
import com.example.retube.data.network.YoutubeApi
import com.example.retube.data.repositories.YoutubeRepository
import com.example.retube.models.Channel
import com.example.retube.models.HomeMostPopular
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(){

    private lateinit var factory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private var videoMostPopularList: List<HomeMostPopular.Item> = ArrayList()
    private var channelList: HashMap<Int, Channel.Item> = HashMap<Int, Channel.Item>()

    private var homeAdapter = HomeAdapter(context, videoMostPopularList,channelList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //리사이클러뷰 데이터 불러오기 -> 사진 썸네일 불러오기
        //클릭 리스너

        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory =
            HomeViewModelFactory(
                repository
            )
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        viewModel.getHomeDatas("snippet,statistics","items(id,snippet(title,thumbnails,publishedAt,channelId,channelTitle),statistics)",
            "mostPopular", getString(R.string.api_key),"KR",3)
        viewModel.homedatas.observe(viewLifecycleOwner, Observer { home ->
            rc_home.also {
                videoMostPopularList = home.items
                getAllChannelData()
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = HomeAdapter(context, videoMostPopularList,channelList)
            }
        })

        viewModel.chennels.observe(viewLifecycleOwner, Observer { channel ->
            channelList = channel
            System.out.println("갱신" + channelList)
            rc_home.adapter = HomeAdapter(context, videoMostPopularList,channelList)
        })


    }

    fun getAllChannelData(){
        for (i in 0..videoMostPopularList.size-1){
            viewModel.getChannelDatas("snippet", videoMostPopularList.get(i).getSnippet().getChannelId(), getString(R.string.api_key),10 , i)
        }
    }

}