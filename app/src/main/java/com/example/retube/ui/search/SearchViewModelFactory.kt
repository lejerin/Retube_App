package com.example.retube.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retube.data.repositories.YoutubeRepository
import com.example.retube.ui.home.HomeViewModel

class SearchViewModelFactory(
    private val repository: YoutubeRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }

}