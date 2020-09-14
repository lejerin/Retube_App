package lej.happy.retube.ui.play.replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_replies.*
import lej.happy.retube.R
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.models.comments.Replies
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.databinding.FragmentRepliesBinding
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.ui.play.PlayActivity
import lej.happy.retube.ui.play.comments.CommentsAdapter
import lej.happy.retube.util.Converter
import java.util.*
import kotlin.collections.ArrayList

class RepliesFragment(val comment: Comment.Item) : Fragment() , RecyclerViewClickListener {

    private lateinit var factory: RepliesViewModelFactory
    private lateinit var viewModel: RepliesViewModel

    private lateinit var binding: FragmentRepliesBinding

    private val repliesList: MutableList<Replies.Item> = ArrayList()
    private var isSetNum = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_replies, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        replies_rc.layoutManager = LinearLayoutManager(requireContext())
        replies_rc.setHasFixedSize(true)
        replies_rc.adapter = RepliesAdapter(repliesList, this)

        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory =
            RepliesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(RepliesViewModel::class.java)

        val title = comment.snippet.topLevelComment.snippet.authorDisplayName +  " \u00b7 " +
                Converter.formatTimeString(comment.snippet.topLevelComment.snippet.publishedAt)

        binding.commentdata = comment
        binding.title = title

        //댓글 불러오기
        viewModel.getRepliesDatas("snippet", comment.id, 30, getString(R.string.api_key))
        viewModel.repliseList.observe(viewLifecycleOwner, Observer { newComment ->

            (replies_rc.adapter as RepliesAdapter).setIsNext(viewModel.nextToken)
            val postionstart = repliesList.size +1
            repliesList.addAll(newComment)
            if(isSetNum){
                (replies_rc.adapter as RepliesAdapter).notifyDataSetChanged()
            }else{
                replies_rc.adapter!!.notifyItemRangeInserted(postionstart, repliesList.size)
            }

            isSetNum = false

        })


        btnOut.setOnClickListener {
            (activity as PlayActivity).onBackPressed()
        }
    }

    override fun onRecyclerViewItemClick(view: View, pos: Int) {
        if(pos == -9){
            System.out.println("바닥 요청")
            viewModel.getRepliesDatas("snippet", comment.id, 30, getString(R.string.api_key))
        }
    }

}