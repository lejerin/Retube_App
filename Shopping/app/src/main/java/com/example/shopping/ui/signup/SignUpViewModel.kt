package com.example.shopping.ui.signup

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.SignUpRequest
import com.example.shopping.data.model.SignUpResponse
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.util.Coroutines
import kotlinx.coroutines.Job


class SignUpViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private lateinit var job: Job

    var id: String? = null
    var email: String? = null
    var name: String? = null
    var password: String? = null
    var password_check: String? = null
    var age: String? = null
    var sex: String? = null

    private val _signData = MutableLiveData<SignUpResponse>()
    val signResponseData : LiveData<SignUpResponse>
        get() = _signData

    fun startSignUp(){
        if(!id.isNullOrEmpty() && !email.isNullOrEmpty() && !name.isNullOrEmpty() &&
            !password.isNullOrEmpty() && !password_check.isNullOrEmpty() && !age.isNullOrEmpty()
            && !sex.isNullOrEmpty()){
            job = Coroutines.ioThenMain(
                { repository.signUp(SignUpRequest(id!!, email!!, name!!, password!!, password_check!!, age!!.toInt(), sex!! )) },
                {
                    _signData.value = it
                }
            )
        }
    }

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            sex = when(position){
                0 -> "male"
                else -> "female"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}