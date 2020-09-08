package com.example.retube.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImageview(view : ImageView, url: String){
    Glide.with(view)
        .load(url)
        .into(view)
}