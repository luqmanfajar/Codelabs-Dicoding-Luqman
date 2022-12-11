package com.luqmanfajar.story_app


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.viewmodel.LoginViewModel
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.databinding.ActivityMainBinding
import com.luqmanfajar.story_app.fitur.LoginActivity
import com.luqmanfajar.story_app.fitur.StoryActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val pref = LoginPreferences.getInstance(dataStore)

        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            LoginViewModel::class.java
        )

        loginViewModel.getLoginStatus().observe(this
        ){ isLogin: Boolean ->
            if (isLogin){
                val i = Intent(this, StoryActivity::class.java)
                startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
                finish()
            } else {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
                finish()
            }

        }

    }


}