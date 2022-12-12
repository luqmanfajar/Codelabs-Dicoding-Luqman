package com.luqmanfajar.story_app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.customview.PasswordValidate
import com.luqmanfajar.story_app.customview.CustomButton
import com.luqmanfajar.story_app.customview.EmailValidate
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.viewmodel.AuthViewModel
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.data.viewmodel.ViewModelFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityLoginBinding
import com.luqmanfajar.story_app.ui.register.RegisterActivity
import com.luqmanfajar.story_app.ui.story.StoryActivity
import com.luqmanfajar.story_app.utils.Result
import com.luqmanfajar.story_app.utils.UiUtils

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var customPassword: PasswordValidate
    private lateinit var customButton: CustomButton
    private lateinit var customEmail: EmailValidate

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Login Page"
        customPassword = binding.edLoginPassword
        customEmail = binding.edLoginEmail
        customButton = binding.btnLogin


        binding.btnLogin.setOnClickListener{
            loginUser()
        }
        binding.txtMoveRegister.setOnClickListener{
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
        }

    }

    private fun loginUser() {
        var email = binding.edLoginEmail.text.toString()
        var password = binding.edLoginPassword.text.toString()
        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnLogin.isEnabled = false
                    UiUtils.show(this)
                }
                is Result.Success -> {
                    UiUtils.close()
                    binding.btnLogin.isEnabled = true
                    result.data.let {
                        if (!it.error) {
                            saveSession(it.loginResult.token)
                            showMessage(it.message)
                            startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                            finish()
                        } else {
                            showMessage(it.message)
                        }
                    }
                }
                is Result.Error -> {
                    UiUtils.close()
                    binding.btnLogin.isEnabled = true
                    showMessage(result.error)
                }
            }

        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveSession(auth:String){
        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            AuthViewModel::class.java
        )
        loginViewModel.savePref(true, auth)
    }


    override fun onBackPressed() {
        Toast.makeText(this@LoginActivity, "Harus Login terlebih dahulu", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}