package com.luqmanfajar.story_app.fitur

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luqmanfajar.story_app.R
import com.luqmanfajar.story_app.adapter.ListStoryAdapter
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.api.StoriesResponse
import com.luqmanfajar.story_app.data.DataStory
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.preference.LoginViewModel
import com.luqmanfajar.story_app.data.preference.ViewModelFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityDetailStoryBinding
import com.luqmanfajar.story_app.databinding.ActivityStoryBinding
import com.luqmanfajar.story_app.fitur.DetailStory.Companion.EXTRA_DETAIL
import retrofit2.Call
import retrofit2.Response

class Story : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var rvStory: RecyclerView
    private lateinit var bindingDetail: ActivityDetailStoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStoryBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvStory = binding.rvStories
        rvStory.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager= layoutManager

        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )


        var tokenAuth = intent.getStringExtra(EXTRA_TOKEN)

        if (tokenAuth != null) {
            getAllStories(tokenAuth)
        } else {
            loginViewModel.getAuthKey().observe(this
            ){
                    authToken : String ->
                    tokenAuth = authToken
                    getAllStories(tokenAuth!!)
            }

        }

        binding.fabAddStory.setOnClickListener{
            val i = Intent(this, AddStory::class.java)
            i.putExtra(AddStory.EXTRA_STORY, tokenAuth)
            startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@Story).toBundle())
        }

    }

    private fun getAllStories(tokenAuth: String){
        showLoading(true)
        val service = ApiConfig().getApiService2(tokenAuth).getStories(1)
        service.enqueue(object : retrofit2.Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(true)
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val arrayStory: ArrayList<ListStoryItem> = responseBody.listStory as ArrayList<ListStoryItem>
                        showRecyclerList(arrayStory)
                        Toast.makeText(this@Story, "Data Loaded", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this@Story, response.message(), Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Toast.makeText(this@Story, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })

    }


    private fun showRecyclerList(storyItem: ArrayList<ListStoryItem>) {
        bindingDetail = ActivityDetailStoryBinding.inflate(layoutInflater)
        rvStory.layoutManager = LinearLayoutManager(this)
        val listStoriesAdapter = ListStoryAdapter(storyItem)
        rvStory.adapter = listStoriesAdapter


        listStoriesAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                val moveWithObjectIntent = Intent(this@Story, DetailStory::class.java )
                moveWithObjectIntent.putExtra(EXTRA_DETAIL, DataStory(data.photoUrl,data.createdAt,data.name,data.description))
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@Story as Activity,
                        Pair(bindingDetail.ivDetailPhoto, "transisiPhoto"),
                        Pair(bindingDetail.tvDetailName, "transisiNama"),
                        Pair(bindingDetail.tvDetailDescription, "transisiDeskripsi"),
                        Pair(bindingDetail.tvDetailTanggal,"transisiTanggal")
                    )
                startActivity(moveWithObjectIntent, optionsCompat.toBundle())
            }
        })


    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )
        loginViewModel.deleteSession()
        finish()
        val i = Intent(this, Login::class.java)
        startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@Story).toBundle())
        return true

    }

    override fun onResume() {
        super.onResume()
        val pref= LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )
        var tokenAuth = intent.getStringExtra(EXTRA_TOKEN)

        if (tokenAuth != null) {
            getAllStories(tokenAuth)
        } else {
            loginViewModel.getAuthKey().observe(this
            ){
                    authToken : String ->
                tokenAuth = authToken
                getAllStories(tokenAuth!!)
            }

        }

    }

    override fun onBackPressed() {
        Toast.makeText(this@Story, "Harus Logout terlebih dahulu", Toast.LENGTH_SHORT).show()
    }


    companion object{
        const val EXTRA_TOKEN = "extra_token"
    }
}


