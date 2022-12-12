package com.luqmanfajar.story_app.ui.login

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String) = repository.getLogin(email, password)
}