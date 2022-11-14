package com.luqmanfajar.story_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.RegisterResponse
import com.luqmanfajar.story_app.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{
            bikinUser()
        }
    }

    private fun bikinUser(){
        var name = binding.edtTextName.text.toString()
        var email = binding.edtTextEmail.text.toString()
        var password = binding.edtTextPassword.text.toString()

        val service = ApiConfig().getApiService().createUser(name, email, password)
        service.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
//                        Toast.makeText(this@Register, responseBody.message, Toast.LENGTH_SHORT).show()
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())
                        binding.txtResponse.setText(response.message())
                    }
                } else {
                    Toast.makeText(this@Register, response.message(), Toast.LENGTH_SHORT).show()
                    binding.txtResponse.setText(response.message())
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@Register, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }
        })
    }

}