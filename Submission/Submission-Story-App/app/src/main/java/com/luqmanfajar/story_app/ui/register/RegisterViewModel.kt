package com.luqmanfajar.story_app.ui.register

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.getRegister(name, email, password)
}