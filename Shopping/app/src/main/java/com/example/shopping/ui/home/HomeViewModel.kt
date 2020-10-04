package com.example.shopping.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.ProductResponse
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.util.Coroutines
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    private val _isUploadSuccess = MutableLiveData<Boolean>()
    val isUploadSuccess : LiveData<Boolean>
        get() = _isUploadSuccess

    fun uploadProduct(
        image: MultipartBody.Part,
        newProduct: RequestBody,
        productDetail: RequestBody,
        productPrice: RequestBody,
        productStock: RequestBody,
        productMajorCategory: RequestBody,
        productMinorCategory: RequestBody,
        productMerchandiser: RequestBody
    ){
        job = Coroutines.ioThenMain(
            { repository.uploadProduct(image, newProduct, productDetail, productPrice, productStock, productMajorCategory,
            productMinorCategory, productMerchandiser) },
            {
                System.out.println("결과값" + it?.status)
                _isUploadSuccess.value = true
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}