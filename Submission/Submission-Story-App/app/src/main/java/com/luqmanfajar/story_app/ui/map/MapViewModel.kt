package com.luqmanfajar.story_app.ui.map

import androidx.lifecycle.ViewModel
import com.luqmanfajar.story_app.data.repository.Repository


class MapViewModel(private val repository: Repository) : ViewModel() {
    fun getLocation(auth:String) = repository.getLocation(auth)
}
