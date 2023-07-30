package com.example.cherry_pick_android.presentation.ui.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}