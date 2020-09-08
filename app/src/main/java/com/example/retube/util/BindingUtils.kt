package com.example.retube.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.retube.R
import de.hdodenhof.circleimageview.CircleImageView

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
