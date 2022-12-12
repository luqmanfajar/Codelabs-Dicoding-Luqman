package com.luqmanfajar.story_app.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.di.Injection
import com.luqmanfajar.story_app.data.repository.Repository

class ViewModelFactory private constructor(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository(context),
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(tesLoginRepository::class.java) -> {
                tesLoginRepository(repository) as T
            }
            modelClass.isAssignableFrom(tesStoryViewModel::class.java) -> {
                tesStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(tesRegisterRepoModel::class.java) -> {
                tesRegisterRepoModel(repository) as T
            }
            modelClass.isAssignableFrom(tesAddStoryRepoModel::class.java) -> {
                tesAddStoryRepoModel(repository) as T
            }
            modelClass.isAssignableFrom(tesMapViewModel::class.java) -> {
                tesMapViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}