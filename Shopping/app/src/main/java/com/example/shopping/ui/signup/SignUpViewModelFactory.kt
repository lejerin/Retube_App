package com.example.shopping.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.data.repository.ApiRepository

class SignUpViewModelFactory(
    private val repository: ApiRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository) as T
    }

}