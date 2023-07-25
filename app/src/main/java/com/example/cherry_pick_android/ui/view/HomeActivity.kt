package com.example.cherry_pick_android.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityHomeBinding

class HomeActivity: AppCompatActivity() {
    // 뷰 바인딩
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}