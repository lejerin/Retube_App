package com.example.shopping.ui.login

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.LoginResponse
import com.example.shopping.data.model.LoginRequest
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.util.Coroutines
import kotlinx.coroutines.Job

class LoginViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private lateinit var job: Job

    var id: String? = null
    var password: String? = null

    private val _loginData = MutableLiveData<LoginResponse>()
    val loginResponseData : LiveData<LoginResponse>
        get() = _loginData

    fun startLogin(){
        if(!id.isNullOrEmpty() && !password.isNullOrEmpty()){
            job = Coroutines.ioThenMain(
                { repository.login(id!!, LoginRequest(id!!,password!!)) },
                {
                    _loginData.value = it
                }
            )
        }
    }


    val clicksListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    startLogin()
                }
                else -> return false
            }
            return true
        }

    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}