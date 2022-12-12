package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository

class tesRegisterRepoModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}