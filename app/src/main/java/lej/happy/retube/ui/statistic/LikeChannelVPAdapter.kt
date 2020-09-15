package lej.happy.retube.ui.statistic

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_item_stat_channel_vp.view.*
import lej.happy.retube.R
import lej.happy.retube.data.models.ChannelStat

class LikeChannelVPAdapter(private val list: List<ChannelStat>): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        var view = inflater.inflate(R.layout.row_item_stat_channel_vp, container, false)


        view.channelNameText.text = list[position].name
        view.channelViewCountText.text = list[position].viewCount.toString()

            if (list[position].url != null) {
                Picasso.get()
                    .load(list[position].url)
                    .placeholder(R.drawable.gray)
                    .fit()
                    .centerCrop()
                    .into(view.channelImg, object : Callback {
                        override fun onSuccess() {
                            Log.d(ContentValues.TAG, "Thumbnail success")
                        }

                        override fun onError(e: Exception) {
                            Log.d(ContentValues.TAG, "Thumbnail error")
                        }
                    })
            } else {
                view.channelImg.setImageResource(R.drawable.gray)
            }


        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageWidth(position: Int): Float {
        return 0.95f
    }
}