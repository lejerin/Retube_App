package com.example.shopping.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.model.ProductResponse
import com.example.shopping.databinding.RcRowItemHomeBinding

class HomeAdapter (
    private val productList : List<ProductResponse>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    override fun getItemCount() = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HomeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rc_row_item_home,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        System.out.println("ddd" + productList[position])

        holder.rcBinding.product = productList[position]


    }

    inner class HomeViewHolder(
        val rcBinding: RcRowItemHomeBinding
    ) : RecyclerView.ViewHolder(rcBinding.root)

}