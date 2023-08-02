package com.example.cherry_pick_android.presentation.ui.keyword.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cherry_pick_android.databinding.FragmentDotProgressBinding

class DotLoadingFragment: DialogFragment() {
    private val binding: FragmentDotProgressBinding by lazy {
        FragmentDotProgressBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 다이얼로그 배경색 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그 투명도 설정
        dialog?.window?.setDimAmount(0F)

        view?.postDelayed({ dismiss() }, 1000)
        return binding.root
    }
}