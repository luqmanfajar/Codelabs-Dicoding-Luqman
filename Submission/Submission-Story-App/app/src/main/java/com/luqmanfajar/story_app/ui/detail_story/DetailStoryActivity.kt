package com.luqmanfajar.story_app.ui.detail_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        setDetailStory()
    }

    private fun setDetailStory(){
        val imageView: ImageView = binding.ivDetailPhoto
        val txtName: TextView = binding.tvDetailName
        val txtDetailDeskripsi: TextView =binding.tvDetailDescription
        val txtDetailDate: TextView=binding.tvDetailTanggal

        val dataStories =  intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL)

        Glide.with(imageView)
            .load(dataStories?.photoUrl)
            .into(imageView)
        txtName.text = "Nama : "+ dataStories?.name
        txtDetailDeskripsi.text =  "Deskripsi : "+ dataStories?.description
        txtDetailDate.text= "Tanggal Dibuat : "+ dataStories?.createdAt
    }


    companion object{

        const val EXTRA_DETAIL = "extra_detail"

    }
}