package lej.happy.retube.ui.play

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_comments.*
import lej.happy.retube.R
import lej.happy.retube.data.models.Video
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.databinding.FragmentCommentsBinding
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter
import lej.happy.retube.util.LinearLayoutManagerWrapper


class CommentsFragment(val videoid: String) : Fragment(),
    RecyclerViewClickListener , View.OnClickListener {

    private lateinit var factory: PlayViewModelFactory
    private lateinit var viewModel: PlayViewModel

    private val commentsList: MutableList<Comment.Item> = mutableListOf()
    private var order = "relevance"

    private var clickTitle = false
    private var allcount = 0
    private var isLoding = false
    private var isSetNum = false

    lateinit var layoutManager : LinearLayoutManagerWrapper
    private var lastVisibleItemPosition = 0

    private lateinit var binding: FragmentCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //검색 버튼 클릭 -> 데이터 불러오기
        //데이터 갖고온 뒤 조회수 불러오기
        //스크롤 시 다음 쿼리 요청

        layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = CommentsAdapter(commentsList, this)



        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory = PlayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlayViewModel::class.java)

        binding.playViewModel = viewModel


        //비디오 정보 가져오기
        viewModel.getDetailVideo("snippet,statistics",
            getString(R.string.api_key), "items(id,snippet(title,description,publishedAt,categoryId,channelId,channelTitle,tags),statistics)",videoid)
        viewModel.videoInfo.observe(viewLifecycleOwner, Observer { videoData ->

            //태그
            //db에 저장

            setData(videoData)


        })

        //댓글 불러오기
        isLoding = true
        viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
        viewModel.commentsList.observe(viewLifecycleOwner, Observer { newComment ->


            (recyclerView.adapter as CommentsAdapter).setIsNext(viewModel.nextToken)
            val postionstart = commentsList.size +1
            commentsList.addAll(newComment)

            if(isSetNum){
                recyclerView.adapter!!.notifyDataSetChanged()
            }else{
                recyclerView.adapter!!.notifyItemRangeInserted(postionstart, commentsList.size)
            }

            isSetNum = false
            isLoding = false

        })

        moreLayout.setOnClickListener(this)
        sortBtn.setOnClickListener(this)

        scrollView.viewTreeObserver.addOnScrollChangedListener(OnScrollChangedListener {
            val view = scrollView.getChildAt(scrollView.childCount - 1) as View
            val diff: Int = view.bottom - (scrollView.height + scrollView.scrollY)
            System.out.println("바닥" + diff + "is Loding" + isLoding)
            if (diff == 0 && !isLoding) {
                viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
            }
        })

        spinner2.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
              viewModel.jobCancle()
            }
            false
        })


        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                commentsList.clear()
                isSetNum = true
                viewModel.setSelectedLan(position)
            }

        }
    }

    private fun setData(video: Video){

        val data = video.items.get(0)

        title.text = data.snippet.title
        DescTextView.text = data.snippet.description
        allcount = (data.statistics.viewCount).toInt()
        viewCount.text = "조회수 " + Converter.getNumlength(data.statistics.viewCount) +"회"

        if(data.statistics.likeCount != null) likeCount.text = Converter.getNumlength(data.statistics.likeCount)
        else likeCount.text = ""

        if(data.statistics.dislikeCount != null) dislikeCount.text = Converter.getNumlength(data.statistics.dislikeCount)
        else dislikeCount.text = ""

        commentNum.text = "댓글 " + Converter.getNumlength(data.statistics.commentCount)

    }


    override fun onRecyclerViewItemClick(view: View, pos: Int) {

        (activity as PlayActivity).setRepliesFragment(commentsList[pos])

    }



    override fun onClick(v: View?) {
        when(v){

            moreLayout -> {
                clickMoreLayout()
            }
            sortBtn -> {
                setSortBtn()
            }
        }
    }

    private fun clickMoreLayout(){

        if(!clickTitle) {
            moreBtn.setImageDrawable(resources.getDrawable(R.drawable.triangle))
            title.maxLines = 4
            title.ellipsize = TextUtils.TruncateAt.END
            DescTextView.visibility = View.VISIBLE
            viewCount.text = "조회수 " + allcount +"회"

        }else {
            moreBtn.setImageDrawable(resources.getDrawable(R.drawable.retriangle))
            title.maxLines = 2
            title.ellipsize = TextUtils.TruncateAt.END
            DescTextView.visibility = View.GONE
            viewCount.text = "조회수 " + Converter.getNumlength(allcount.toString()) +"회"

        }

        clickTitle = !clickTitle;

    }

    private fun setSortBtn(){
        lastVisibleItemPosition = 0
        commentsList.clear()
        viewModel.resetCommentData()

        //creating a popup menu
        val popup = PopupMenu(context, sortBtn)
        //inflating menu from xml resource
        popup.inflate(R.menu.sort_item)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.relative -> {
                    //handle menu1 click
                    order = "relevance"
                    isLoding = true
                    viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
                    true
                }
                R.id.newest -> {
                    order = "time"
                    isLoding = true
                    viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }



}