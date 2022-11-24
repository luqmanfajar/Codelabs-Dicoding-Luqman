package com.luqmanfajar.story_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.luqmanfajar.story_app.data.DataStory
import com.luqmanfajar.story_app.databinding.ActivityDetailStoryBinding

class DetailStory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        val imageView: ImageView = binding.RIdTvDetailPhoto
        val txtName: TextView = binding.RIdIvDetailName
        val txtDetailDeskripsi: TextView =binding.RIdTvDetailDescription
        val txtDetailDate: TextView=binding.RIdDetailTanggal

        val dataStories =  intent.getParcelableExtra<DataStory>(EXTRA_DETAIL) as DataStory

        Glide.with(imageView)
            .load(dataStories.photoUrl)
            .into(imageView)
        txtName.text = "Nama : "+ dataStories.name
        txtDetailDeskripsi.text =  "Deskripsi : "+ dataStories.description
        txtDetailDate.text= "Tanggal Dibuat : "+ dataStories.createdAt
    }

    companion object{

        const val EXTRA_DETAIL = "extra_detail"

    }
}