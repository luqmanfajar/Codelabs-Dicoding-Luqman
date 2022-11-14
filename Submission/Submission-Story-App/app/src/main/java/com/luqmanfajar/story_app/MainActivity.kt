package com.luqmanfajar.story_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
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
                        }
                    } else {
                        Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                        binding.txtToken.setText(response.message())
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                }
            })

    }
}