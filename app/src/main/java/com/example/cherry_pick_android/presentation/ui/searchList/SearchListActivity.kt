package com.example.cherry_pick_android.presentation.ui.searchList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cherry_pick_android.databinding.ActivitySearchListBinding

class SearchListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}