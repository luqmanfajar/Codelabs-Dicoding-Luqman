package com.luqmanfajar.story_app.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luqmanfajar.story_app.R
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.viewmodel.AuthViewModel
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityStoryBinding
import androidx.activity.viewModels
import com.luqmanfajar.story_app.adapter.LoadingStateAdapter
import com.luqmanfajar.story_app.adapter.PagingAdapter
import com.luqmanfajar.story_app.data.viewmodel.ViewModelFactory
import com.luqmanfajar.story_app.ui.map.MapsActivity
import com.luqmanfajar.story_app.ui.add_story.AddStoryActivity
import com.luqmanfajar.story_app.ui.login.LoginActivity


class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var rvStory: RecyclerView

    private val pagingViewModel: StoriesViewModel by viewModels {

        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStoryBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvStory = binding.rvStories
        rvStory.setHasFixedSize(true)
        binding.rvStories.layoutManager= LinearLayoutManager(this)

        getData()

        binding.fabAddStory.setOnClickListener{
            val i = Intent(this, AddStoryActivity::class.java)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@StoryActivity).toBundle())
        }

        binding.fabLocation.setOnClickListener{
            val i = Intent(this, MapsActivity::class.java)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@StoryActivity).toBundle())
        }

    }

    private fun getData(){
        val adapter = PagingAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        pagingViewModel.getStory.observe(this) { list ->
            adapter.submitData(lifecycle,list)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            AuthViewModel::class.java
        )
        loginViewModel.deleteSession()
        finish()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@StoryActivity).toBundle())
        return true

    }

    override fun onResume() {
        super.onResume()
        getData()

    }

}


