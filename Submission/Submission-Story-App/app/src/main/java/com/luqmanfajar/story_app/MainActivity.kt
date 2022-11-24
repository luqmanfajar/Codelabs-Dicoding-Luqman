package com.luqmanfajar.story_app


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.luqmanfajar.story_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnmove.setOnClickListener{
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }
    }



}