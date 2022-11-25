package com.luqmanfajar.story_app


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.LoginPreferences
import com.luqmanfajar.story_app.data.preference.LoginViewModel
import com.luqmanfajar.story_app.data.preference.ViewModelFactory
import com.luqmanfajar.story_app.databinding.ActivityMainBinding
import com.luqmanfajar.story_app.fitur.AddStory
import com.luqmanfajar.story_app.fitur.Login
import com.luqmanfajar.story_app.fitur.Register
import com.luqmanfajar.story_app.fitur.Story

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val pref = LoginPreferences.getInstance(dataStore)

        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )

        loginViewModel.getLoginStatus().observe(this
        ){ isLogin: Boolean ->
            if (isLogin){
                val i = Intent(this, Story::class.java)
                startActivity(i)
            } else {
                val i = Intent(this, Register::class.java)
                startActivity(i)
            }

        }


    }



}