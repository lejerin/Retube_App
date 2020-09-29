package com.example.shopping.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.ProductResponse
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.util.Coroutines
import kotlinx.coroutines.Job

class HomeViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private lateinit var job: Job

    private val _products = MutableLiveData<List<ProductResponse>>()
    val products : LiveData<List<ProductResponse>>
        get() = _products

    fun getProductList(){
        job = Coroutines.ioThenMain(
            { repository.getProduct() },
            {
                _products.value = it
            }
        )

    }

    fun uploadProduct(){
        job = Coroutines.ioThenMain(
            { repository.uploadProduct(ProductResponse()) },
            {

            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}