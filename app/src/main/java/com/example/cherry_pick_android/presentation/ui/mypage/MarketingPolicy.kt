package com.example.cherry_pick_android.presentation.ui.mypage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cherry_pick_android.databinding.ActivityMarketingPolicyBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MarketingPolicy : AppCompatActivity() {
    private lateinit var binding: ActivityMarketingPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketingPolicyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvMarketingPolicy.text = getTextString(this,"마케팅정보수집동의")
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