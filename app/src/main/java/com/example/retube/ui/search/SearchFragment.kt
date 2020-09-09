package com.example.retube.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retube.R
import com.example.retube.activity.PlayActivity
import com.example.retube.data.models.search.Item
import com.example.retube.data.network.YoutubeApi
import com.example.retube.data.repositories.YoutubeRepository
import com.example.retube.databinding.FragmentSearchBinding
import com.example.retube.ui.RecyclerViewClickListener
import com.example.retube.util.LinearLayoutManagerWrapper
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(),
    RecyclerViewClickListener {

    private lateinit var factory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

    private val videoSearchList: MutableList<Item> = mutableListOf()
    private val viewCountList = HashMap<Int, Int>()


    lateinit var layoutManager : LinearLayoutManagerWrapper
    private var lastVisibleItemPosition = 0

    private lateinit var binding: FragmentSearchBinding

    private var startNum = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //검색 버튼 클릭 -> 데이터 불러오기
        //데이터 갖고온 뒤 조회수 불러오기
        //스크롤 시 다음 쿼리 요청

        layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)
        rc_search.layoutManager = layoutManager
        rc_search.setHasFixedSize(true)
        rc_search.adapter = SearchAdapter(videoSearchList,viewCountList, this)



        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory = SearchViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        binding.searchViewModel = viewModel



        binding.searchText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    videoSearchList.clear()
                   viewModel.find(v)
                }
                else -> return@OnEditorActionListener false
            }
            true
        })

        binding.checkBtn.setOnClickListener{
            videoSearchList.clear()
        }


        viewModel.searchdatas.observe(viewLifecycleOwner, Observer { searches ->
            (rc_search.adapter!! as SearchAdapter).setIsNext(viewModel.nextToken)
            videoSearchList.addAll(searches)
            rc_search.adapter!!.notifyItemRangeInserted(startNum,videoSearchList.size-1)
            startNum = videoSearchList.size
            getViewCount()

        })

        viewModel.viewCount.observe(viewLifecycleOwner, Observer { viewCount ->
            viewCountList.clear()
            viewCountList.put(viewCount.num, viewCount.count)
            System.out.println("갱신" + viewCount.num)
            rc_search.adapter!!.notifyItemChanged(viewCount.num)
        })

        addScrollerListener()

    }

    private fun getViewCount(){
        for (i in 0..videoSearchList.size-1){
            viewModel.getViewCountDatas("statistics", getString(R.string.api_key), videoSearchList[i].id.videoId ,i)
        }
    }

    override fun onRecyclerViewItemClick(view: View, pos: Int) {

        System.out.println("클릭")
        val intent = Intent(activity, PlayActivity::class.java)
        intent.putExtra("videoID", videoSearchList[pos].id.videoId)
        startActivity(intent)

        }



    private fun addScrollerListener()
    {
        rc_search.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition === itemTotalCount) {
                    //비디오 추가 갱신할 때
                    viewModel.findMore(binding.searchText)
                }
            }
        })
    }



}