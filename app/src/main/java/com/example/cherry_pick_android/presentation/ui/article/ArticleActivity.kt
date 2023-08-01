package com.example.cherry_pick_android.presentation.ui.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.databinding.ActivityArticleBinding

class ArticleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}