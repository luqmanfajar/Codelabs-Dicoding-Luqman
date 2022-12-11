package com.luqmanfajar.story_app.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.di.Injection
import com.luqmanfajar.story_app.data.repository.StoriesRepository

class ViewModelFactory private constructor(
    private val repository: StoriesRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository(context),
            )
        }
    }

//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
//                AddStoryViewModel(repository) as T
//            }
////            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
////                MainViewModel(repository) as T
////            }
////            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) -> {
////                CreateStoryViewModel(repository) as T
////            }
////            modelClass.isAssignableFrom(LocationViewModel::class.java) -> {
////                LocationViewModel(repository) as T
////            }
//            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
}