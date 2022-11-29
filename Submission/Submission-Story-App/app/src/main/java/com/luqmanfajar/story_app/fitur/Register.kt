package com.luqmanfajar.story_app.fitur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.RegisterResponse
import com.luqmanfajar.story_app.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Register Page"

        binding.txtMoveLogin.setOnClickListener{
            val i = Intent(this, Login::class.java)
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this@Register).toBundle())
        }
        binding.btnRegister.setOnClickListener{
            bikinUser()
        }
    }

    private fun bikinUser(){
        var name = binding.edRegisterName.text.toString()
        var email = binding.edRegisterEmail.text.toString()
        var password = binding.edRegisterPassword.text.toString()
        val i = Intent(this, Login::class.java)

        val service = ApiConfig().getApiService().createUser(name, email, password)
        service.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                       Toast.makeText(this@Register, "Register Sukses : "+response.message(), Toast.LENGTH_SHORT).show()

                        startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@Register).toBundle())
                    }
                } else {
                    Toast.makeText(this@Register, "Register Gagal : "+response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@Register, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }
        })
    }
}