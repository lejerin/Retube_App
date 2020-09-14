package lej.happy.retube.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lej.happy.retube.R
import lej.happy.retube.data.models.comments.Replies


@BindingAdapter("image")
fun loadImageview(view : ImageView, url: String){
    if(!url.isNullOrEmpty()){
        Glide.with(view)
            .load(url)
            .placeholder(R.drawable.gray)
            .fitCenter()
            .centerCrop()
            .into(view)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Replies.Item>?){
    if(data != null){
//        if(recyclerView.adapter == null){
//            recyclerView.adapter =
//                RepliesAdapter(
//                    data
//                )
//        }
//        val adapter = recyclerView.adapter as RepliesAdapter
//        adapter.submitList(data)
    }

}
