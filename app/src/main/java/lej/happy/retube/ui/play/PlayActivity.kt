package lej.happy.retube.ui.play

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import lej.happy.retube.R
import lej.happy.retube.data.models.youtube.Comments
import lej.happy.retube.ui.play.comments.CommentsFragment
import lej.happy.retube.ui.play.replies.RepliesFragment


class PlayActivity : AppCompatActivity() {

    lateinit var videoid : String
    lateinit var commentFragment : RepliesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)


        //영상 재생
        videoInit()

        //댓글 프래그먼트
        val commentFragment: Fragment =
            CommentsFragment(videoid)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.comment_layout, commentFragment).commit()


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
        }

        val newFragment: Fragment =
            YoutubePlayFragment(videoid)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.youtubePlay, newFragment).commit()
    }


    fun setRepliesFragment(comment: Comments.Item){
        commentFragment =
            RepliesFragment(comment)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.add(R.id.comment_layout, commentFragment)
        ft.addToBackStack(null)
        ft.commit()

    }

    override fun onBackPressed() {

        val manager = supportFragmentManager
        if(manager.backStackEntryCount > 0){
            manager.popBackStack()
        }else{
            super.onBackPressed()
        }

    }

}