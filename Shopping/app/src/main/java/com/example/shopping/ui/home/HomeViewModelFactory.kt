package com.example.shopping.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.data.network.ShopApi
import com.example.shopping.data.repository.ApiRepository

class HomeViewModelFactory(
    private val repository: ApiRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }

}