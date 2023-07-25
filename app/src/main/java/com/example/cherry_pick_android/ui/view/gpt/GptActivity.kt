package com.example.cherry_pick_android.ui.view.gpt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cherry_pick_android.databinding.ActivityGptBinding

class GptActivity: AppCompatActivity() {
    private val binding: ActivityGptBinding by lazy {
        ActivityGptBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}