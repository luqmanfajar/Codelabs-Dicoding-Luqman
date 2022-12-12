package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository

class tesLoginRepository(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)
}