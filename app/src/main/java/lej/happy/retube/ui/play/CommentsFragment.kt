package lej.happy.retube.ui.play

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.Converter


class CommentsFragment(val videoid: String) : Fragment() , RecyclerViewClickListener,
    View.OnClickListener {

    private lateinit var factory: PlayViewModelFactory
    private lateinit var viewModel: PlayViewModel

    private val commentsList: MutableList<Comment.Item> = ArrayList()
    private var clickTitle = false
    private var allcount = 0
    private var order = "relevance"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CommentsAdapter(commentsList,this)

        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory = PlayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlayViewModel::class.java)

        //비디오 정보 가져오기
        viewModel.getDetailVideo("snippet,statistics",
            getString(R.string.api_key), "items(id,snippet(title,description,publishedAt,categoryId,channelId,channelTitle,tags),statistics)",videoid)
        viewModel.videoInfo.observe(viewLifecycleOwner, Observer { videoData ->

            //태그
            //db에 저장
            setData(videoData)


        })

        //댓글 불러오기
        viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
        viewModel.commentsList.observe(viewLifecycleOwner, Observer { newComment ->

            commentsList.addAll(newComment)
            (recyclerView.adapter as CommentsAdapter).notifyDataSetChanged()

        })

        moreLayout.setOnClickListener(this)
        sortBtn.setOnClickListener(this)


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
                    viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
                    true
                }
                R.id.newest -> {
                    order = "time"
                    viewModel.getCommentDatas(videoid, order,getString(R.string.api_key))
                    true
                }
                else -> false
            }
        }
        popup.show()
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
}