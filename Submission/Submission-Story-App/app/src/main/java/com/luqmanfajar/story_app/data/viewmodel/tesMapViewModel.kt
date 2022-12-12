package com.luqmanfajar.story_app.data.viewmodel

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository


class tesMapViewModel(private val repository: Repository) : ViewModel() {
    fun getLocation(auth:String) = repository.getLocation(auth)
}
