package com.example.shopping.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.network.ShopApi
import com.example.shopping.data.repository.ApiRepository
import com.example.shopping.databinding.ActivitySignUpBinding
import androidx.lifecycle.Observer
import com.example.shopping.ui.MainActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var factory: SignUpViewModelFactory
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this,R.layout.activity_sign_up)
        binding.lifecycleOwner = this

        val api = ShopApi()
        val repository = ApiRepository(api)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)

        binding.signUpViewModel = viewModel


        binding.btnReturn.setOnClickListener {
            finish()
        }


        viewModel.signResponseData.observe(this, Observer { data ->
            if(data.is_join){
                Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
            }

        })

    }


}