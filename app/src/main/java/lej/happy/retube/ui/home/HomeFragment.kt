package lej.happy.retube.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import lej.happy.retube.R
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.data.models.Channel
import lej.happy.retube.data.models.HomeMostPopular
import lej.happy.retube.ui.RecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_home.*
import lej.happy.retube.ui.play.PlayActivity


class HomeFragment : Fragment() ,
    RecyclerViewClickListener {

    private lateinit var factory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private val videoMostPopularList: MutableList<HomeMostPopular.Item> = mutableListOf()
    private var channelList: HashMap<Int, Channel.Item> = HashMap<Int, Channel.Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //리사이클러뷰 데이터 불러오기 -> 사진 썸네일 불러오기
        //클릭 리스너

        rc_home.layoutManager = LinearLayoutManager(requireContext())
        rc_home.setHasFixedSize(true)
        rc_home.adapter = HomeAdapter(videoMostPopularList,channelList,this)

        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        viewModel.getHomeDatas("snippet,statistics","items(id,snippet(title,thumbnails,publishedAt,channelId,channelTitle),statistics)",
            "mostPopular", getString(R.string.api_key),"KR",20)
        viewModel.homedatas.observe(viewLifecycleOwner, Observer { home ->

            videoMostPopularList.clear()
            videoMostPopularList.addAll(home.items)
            rc_home.adapter!!.notifyDataSetChanged()
            getAllChannelData()

        })

        viewModel.chennels.observe(viewLifecycleOwner, Observer { channel ->
            channelList.clear()
            channelList.putAll(channel)
            rc_home.adapter!!.notifyDataSetChanged()
        })


    }

    private fun getAllChannelData(){
        for (i in 0..videoMostPopularList.size-1){
            viewModel.getChannelDatas("snippet", videoMostPopularList.get(i).getSnippet().getChannelId(), getString(R.string.api_key),10 , i)
        }
    }

    override fun onRecyclerViewItemClick(view: View, pos: Int) {


                val intent = Intent(activity, PlayActivity::class.java)
                intent.putExtra("videoID", videoMostPopularList[pos].id)
                startActivity(intent)


    }

}