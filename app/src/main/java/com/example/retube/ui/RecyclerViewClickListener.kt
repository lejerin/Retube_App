package com.example.retube.ui

import android.view.View

interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view: View, pos: Int)
}