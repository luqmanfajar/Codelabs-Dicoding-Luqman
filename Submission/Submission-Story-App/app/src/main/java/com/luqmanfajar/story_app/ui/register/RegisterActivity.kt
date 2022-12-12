package com.luqmanfajar.story_app.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import com.luqmanfajar.story_app.data.viewmodel.*
import com.luqmanfajar.story_app.databinding.ActivityRegisterBinding
import com.luqmanfajar.story_app.ui.login.LoginActivity
import com.luqmanfajar.story_app.utils.Result
import com.luqmanfajar.story_app.utils.UiUtils


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Register Page"

        binding.txtMoveLogin.setOnClickListener{
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle())
        }
        binding.btnRegister.setOnClickListener{
            registerUser()
        }
    }


    private fun registerUser() {
        var name = binding.edRegisterName.text.toString()
        var email = binding.edRegisterEmail.text.toString()
        var password = binding.edRegisterPassword.text.toString()
        viewModel.register(name,email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    UiUtils.show(this)
                }
                is Result.Success -> {
                    UiUtils.close()
                    result.data.let {
                        if (!it.error) {
                            showMessage(it.message)
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            showMessage(it.message)
                        }
                    }
                }
                is Result.Error -> {
                    UiUtils.close()
                    showMessage(result.error)
                }
            }

        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}