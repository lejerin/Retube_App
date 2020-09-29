package com.example.mapapp.ui.login

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapapp.data.model.User
import com.example.mapapp.util.Coroutines
import kotlinx.coroutines.Job

class LoginViewModel(
) : ViewModel() {

    private lateinit var job: Job

    private val _loginData = MutableLiveData<User>()
    val loginResponseData : LiveData<User>
        get() = _loginData

    fun startLogin(id: String, password: String){
        if(id.isNotEmpty() && password.isNotEmpty()){
//            job = Coroutines.ioThenMain(
//                { repository.login(id!!, LoginRequest(id!!,password!!)) },
//                {
//                    _loginData.value = it
//                }
//            )

            _loginData.value = User("happy", "pw")
        }
    }


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}