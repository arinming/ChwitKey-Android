package com.example.cherry_pick_android.presentation.ui.mypage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cherry_pick_android.databinding.ActivityUserPolicyBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class UserPolicy : AppCompatActivity() {
    private lateinit var binding: ActivityUserPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPolicyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvUserPolicy.text = getTextString(this,"임시개인정보처리방침")
        goBack()
    }

    private fun goBack() {
        binding.ibtnBack.setOnClickListener{
            finish()
        }
    }

    private fun getTextString(context: Context, filename: String) : String{
        val fString = StringBuilder()
        val reader: BufferedReader

        try {
            reader = BufferedReader(
                InputStreamReader(context.resources.assets.open("$filename.txt"))
            )

            var str: String?
            while (reader.readLine().also {str = it} != null) {
                fString.append(str)
                fString.append('\n')
            }
            reader.close()
            return fString.toString()
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return "fail"
    }
}