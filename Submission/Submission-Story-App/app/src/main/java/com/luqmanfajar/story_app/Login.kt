package com.luqmanfajar.story_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.luqmanfajar.story_app.Story.Companion.EXTRA_TOKEN
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.databinding.ActivityLoginBinding
import com.luqmanfajar.story_app.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnDaftar.setOnClickListener{
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }
        binding.btnLogin.setOnClickListener{
            LoginUser()
        }
    }

    private fun LoginUser(){


        var email = binding.edtLoginEmail.text.toString()
        var password = binding.edtLoginPassword.text.toString()

        val service = ApiConfig().getApiService().loginUser(email, password)
        service.enqueue(object : retrofit2.Callback<LoginResponse> {
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

                        binding.txtToken.setText(responseBody.loginResult.token.toString())
                        val moveWithObjectIntent = Intent(this@Login, Story::class.java )
                        moveWithObjectIntent.putExtra(EXTRA_TOKEN, responseBody.loginResult.token.toString())
                        startActivity(moveWithObjectIntent)

                    }
                } else {
                    Toast.makeText(this@Login, response.message(), Toast.LENGTH_SHORT).show()
                    binding.txtToken.setText(response.message())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }
        })

    }


}