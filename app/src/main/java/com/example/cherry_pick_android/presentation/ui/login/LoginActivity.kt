package com.example.cherry_pick_android.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityLoginBinding
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.newsSearch.NewsSearchActivity

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        goToHome()
    }

    private fun goToHome() {
        binding.ibtnKakao.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}