package lej.happy.retube.ui.play

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.fragment_search.*
import lej.happy.retube.R
import lej.happy.retube.data.models.comments.Comment
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.databinding.ActivityPlayBinding
import lej.happy.retube.ui.RecyclerViewClickListener
import lej.happy.retube.util.LinearLayoutManagerWrapper


class PlayActivity : AppCompatActivity() , RecyclerViewClickListener {


    private lateinit var factory: PlayViewModelFactory
    private lateinit var viewModel: PlayViewModel
    lateinit var videoid : String

    private val commentsList: MutableList<Comment.Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPlayBinding>(this,R.layout.activity_play)

        //liveData를 사용하기 위해 if not 관찰해도 refresh가 되지않음
        binding.lifecycleOwner = this

        val api = YoutubeApi()
        val repository = YoutubeRepository(api)
        factory = PlayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlayViewModel::class.java)

        binding.playViewModel = viewModel

        val layoutManager = LinearLayoutManagerWrapper(this@PlayActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CommentsAdapter(commentsList, this)

        //영상 재생
        videoInit()
        //영상 상세정보 불러오기
        viewModel.getCommentDatas(videoid, "relevance",getString(R.string.api_key))
        viewModel.commentsList.observe(this, Observer { newComment ->

            commentsList.addAll(newComment)
            (recyclerView.adapter as CommentsAdapter).notifyDataSetChanged()
            loadingLayout.setVisibility(View.INVISIBLE)

        })

//        viewModel.repliseList.observe(this, Observer { change ->
//            recyclerView.adapter!!.notifyItemChanged(change.num + 1)
//        })

    }

    private fun videoInit(){
        val intent = intent /*데이터 수신*/
        videoid = intent.extras!!.getString("videoID").toString() /*String형*/

        val action = intent.action
        val type = intent.type

        // 인텐트 정보가 있는 경우 실행
        if (Intent.ACTION_SEND == action && type.equals("text/plain")) {

            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            videoid = sharedText.substring(sharedText.lastIndexOf("/") + 1)
            println("공유링크 :$videoid")

        }

        val newFragment: Fragment =
            YoutubePlayFragment(videoid)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.youtubePlay, newFragment).commit()
    }

    override fun onRecyclerViewItemClick(view: View, pos: Int) {
        //프래그먼트 만들기
        commentsList[pos].id
    }
}