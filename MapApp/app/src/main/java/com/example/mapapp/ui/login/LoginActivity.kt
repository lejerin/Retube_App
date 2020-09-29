package com.example.mapapp.ui.login

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mapapp.R
import com.example.mapapp.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() , View.OnClickListener, SignInDialog.SignInClickListener{

    private lateinit var viewModel: LoginViewModel
    lateinit var dialog : SignInDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        viewModel.loginResponseData.observe(this, Observer {
            Toast.makeText(this, it.ID.toString() , Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            goMainActivity()
        })

        btn_signup.setOnClickListener(this)
        btn_signin.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_signin -> {

                val fm = supportFragmentManager
                dialog = SignInDialog(this)
                dialog.show(fm, "sign in")
            }
            R.id.btn_signup -> {

            }
        }
    }

    override fun onSignIn(id: String, pw: String) {
        viewModel.startLogin(id, pw)
    }

    fun goMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}