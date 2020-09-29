package com.example.shopping.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.network.ShopApi
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.databinding.ActivityLoginBinding
import com.example.shopping.ui.MainActivity
import com.example.shopping.ui.signup.SignUpActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var factory: LoginViewModelFactory
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,R.layout.activity_login)
        binding.lifecycleOwner = this

        val api = ShopApi()
        val repository = ApiRepository(api)
        factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel


        viewModel.loginResponseData.observe(this, Observer { data ->
            if(data.is_login){
                Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
            }
        })

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}