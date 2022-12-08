package com.luqmanfajar.story_app.fitur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.customview.PasswordValidate
import com.luqmanfajar.story_app.fitur.Story.Companion.EXTRA_TOKEN
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.customview.CustomButton
import com.luqmanfajar.story_app.customview.EmailValidate
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.preference.LoginViewModel
import com.luqmanfajar.story_app.data.preference.ViewModelFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.database.di.Injection
import com.luqmanfajar.story_app.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var customPassword: PasswordValidate
    private lateinit var customButton: CustomButton
    private lateinit var customEmail: EmailValidate

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Login Page"
        customPassword = binding.edLoginPassword
        customEmail = binding.edLoginEmail
        customButton = binding.btnLogin

        binding.btnLogin.setOnClickListener{
            LoginUser()
        }
        binding.txtMoveRegister.setOnClickListener{
            val i = Intent(this, Register::class.java)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@Login).toBundle())
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

                        val moveWithObjectIntent = Intent(this@Login, Story::class.java)
                        moveWithObjectIntent.putExtra(EXTRA_TOKEN, responseBody.loginResult.token)

                        startActivity(moveWithObjectIntent)
                        loginViewModel.savePref(true,responseBody.loginResult.token)
                        Toast.makeText(this@Login, "Login Sukses", Toast.LENGTH_SHORT).show()
                        finish()

                    }
                } else {
                    Toast.makeText(this@Login, "Login gagal : "+response.message(), Toast.LENGTH_SHORT).show()


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

    override fun onBackPressed() {
        Toast.makeText(this@Login, "Harus Login terlebih dahulu", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}