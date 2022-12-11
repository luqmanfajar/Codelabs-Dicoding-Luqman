package com.luqmanfajar.story_app.fitur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.customview.PasswordValidate
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.LoginResponse
import com.luqmanfajar.story_app.customview.CustomButton
import com.luqmanfajar.story_app.customview.EmailValidate
import com.luqmanfajar.story_app.data.paging.PagingModelFactory
import com.luqmanfajar.story_app.data.paging.StoryViewModel
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.viewmodel.LoginViewModel
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.data.viewmodel.tesLoginModel
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityLoginBinding
import com.luqmanfajar.story_app.fitur.AddStoryActivity.Companion.EXTRA_STORY
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

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
            Login()
        }
        binding.txtMoveRegister.setOnClickListener{
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
        }

    }

    private fun Login() {
        val loginViewModel = ViewModelProvider(this).get(tesLoginModel::class.java)

        var email = binding.edLoginEmail.text.toString()
        var password = binding.edLoginPassword.text.toString()

        loginViewModel.loginUser(email, password)
        loginViewModel.listReview.observe(this) { loginResult ->

            val moveWithObjectIntent = Intent(this@LoginActivity, StoryActivity::class.java)
            moveWithObjectIntent.putExtra(EXTRA_STORY, loginResult.token)
            startActivity(moveWithObjectIntent)
            saveSession(loginResult.token)

            Toast.makeText(this@LoginActivity, "Login Sukses", Toast.LENGTH_SHORT).show()
            finish()

        }
    }

    private fun saveSession(auth:String){
        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            LoginViewModel::class.java
        )
        loginViewModel.savePref(true, auth)
    }



//    private fun LoginUser(){
//
//        val pref= LoginPreferences.getInstance(dataStore)
//
//        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
//            LoginViewModel::class.java
//        )
//        var email = binding.edLoginEmail.text.toString()
//        var password = binding.edLoginPassword.text.toString()
//
//
//        val service = ApiConfig().getApiService().loginUser(email, password)
//        service.enqueue(object : retrofit2.Callback<LoginResponse>, LifecycleOwner {
//            override fun onResponse(
//                call: Call<LoginResponse>,
//                response: Response<LoginResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null && !responseBody.error) {
//
//                        val moveWithObjectIntent = Intent(this@LoginActivity, StoryActivity::class.java)
//                        moveWithObjectIntent.putExtra(EXTRA_TOKEN, responseBody.loginResult.token)
//
//                        startActivity(moveWithObjectIntent)
//                        loginViewModel.savePref(true,responseBody.loginResult.token)
//                        Toast.makeText(this@LoginActivity, "Login Sukses", Toast.LENGTH_SHORT).show()
//                        finish()
//
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "Login gagal : "+response.message(), Toast.LENGTH_SHORT).show()
//
//
//                }
//            }
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun getLifecycle(): Lifecycle {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }

    override fun onBackPressed() {
        Toast.makeText(this@LoginActivity, "Harus Login terlebih dahulu", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}