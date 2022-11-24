package com.luqmanfajar.story_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luqmanfajar.story_app.adapter.ListStoryAdapter
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.ApiService
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.api.StoriesResponse
import com.luqmanfajar.story_app.data.DataStory
import com.luqmanfajar.story_app.databinding.ActivityStoryBinding
import com.luqmanfajar.story_app.DetailStory.Companion.EXTRA_DETAIL
import retrofit2.Call
import retrofit2.Response

class Story : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var rvStory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStoryBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvStory = binding.rvStories
        rvStory.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager= layoutManager


        var tokenAuth = intent.getStringExtra(EXTRA_TOKEN)
        getAllStories(tokenAuth.toString())

        binding.fabAddStory.setOnClickListener{
            val i = Intent(this, AddStory::class.java)
            startActivity(i)
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
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val arrayStory: ArrayList<ListStoryItem> = responseBody.listStory as ArrayList<ListStoryItem>
                        showRecyclerList(arrayStory)


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
        rvStory.layoutManager = LinearLayoutManager(this)
        val listStoriesAdapter = ListStoryAdapter(storyItem)
        rvStory.adapter = listStoriesAdapter

        listStoriesAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                val moveWithObjectIntent = Intent(this@Story, DetailStory::class.java )
                moveWithObjectIntent.putExtra(EXTRA_DETAIL, DataStory(data.photoUrl,data.createdAt,data.name,data.description))
                startActivity(moveWithObjectIntent)
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





    companion object{
        const val EXTRA_TOKEN = "extra_token"
    }
}