package com.luqmanfajar.story_app.fitur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.customview.CustomEditText
import com.luqmanfajar.story_app.fitur.Story.Companion.EXTRA_TOKEN
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.data.LoginModel
import com.luqmanfajar.story_app.data.LoginPreferences
import com.luqmanfajar.story_app.data.preference.LoginViewModel
import com.luqmanfajar.story_app.data.preference.ViewModelFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var customEditText: CustomEditText
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginModel: LoginModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Login Page"
        customEditText = binding.edLoginPassword


        binding.btnLogin.setOnClickListener{
            LoginUser()

        }



    }



    private fun LoginUser(){

        val pref= LoginPreferences.getInstance(dataStore)

        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )
        var email = binding.edLoginEmail.text.toString()
        var password = binding.edLoginPassword.text.toString()


        val service = ApiConfig().getApiService().loginUser(email, password)
        service.enqueue(object : retrofit2.Callback<LoginResponse>, LifecycleOwner {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())

                        binding.txtToken.setText(responseBody.loginResult.token)
                        val moveWithObjectIntent = Intent(this@Login, Story::class.java )
                        moveWithObjectIntent.putExtra(EXTRA_TOKEN, responseBody.loginResult.token)

                        startActivity(moveWithObjectIntent)
                        loginViewModel.savePref(true,responseBody.loginResult.token)


                    }
                } else {
                    Toast.makeText(this@Login, response.message(), Toast.LENGTH_SHORT).show()
                    binding.txtToken.setText(response.message())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

            override fun getLifecycle(): Lifecycle {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    companion object{
        const val EXTRA_LOGIN = "extra_login"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_DELETE = 2


    }


}