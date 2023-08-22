package com.chwitkey.cherry_pick_android.presentation.ui.keyword.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.chwitkey.cherry_pick_android.databinding.DialogCheckBinding

// 커스텀 다이얼로그
class KeywordDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogCheckBinding.inflate(inflater, container, false)
        // 다이얼로그 배경색 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그 투명도 설정
        dialog?.window?.setDimAmount(0F)

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        // 다이얼로그가 화면에 보여진 후 0.5초 후에 사라지도록 설정
        view?.postDelayed({
            dismiss()
        }, 500)
    }
}