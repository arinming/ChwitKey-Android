package com.example.cherry_pick_android.presentation.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.databinding.ActivityArticleBinding
import com.example.cherry_pick_android.presentation.ui.gpt.GptActivity

class ArticleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        goToBack()
        goToGPT()
    }

    fun goToBack() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    fun goToGPT() {
        binding.ibtnGpt.setOnClickListener {
            startActivity(Intent(this, GptActivity::class.java))
        }
    }
}