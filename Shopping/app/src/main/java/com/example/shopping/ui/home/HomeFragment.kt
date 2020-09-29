package com.example.shopping.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopping.R
import com.example.shopping.data.model.ProductResponse
import com.example.shopping.data.network.ShopApi
import com.example.shopping.data.repository.ApiRepository
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var factory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private val products: MutableList<ProductResponse> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        rc_home.layoutManager = LinearLayoutManager(requireContext())
        rc_home.setHasFixedSize(true)
        rc_home.adapter = HomeAdapter(products)

        val api = ShopApi()
        val repository = ApiRepository(api)
        factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        viewModel.getProductList()
        viewModel.products.observe(viewLifecycleOwner, Observer { list ->

            products.clear()
            products.addAll(list)
            rc_home.adapter!!.notifyDataSetChanged()

        })


        btn_add_product.setOnClickListener {

        }

    }

}