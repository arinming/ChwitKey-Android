package com.example.cherry_pick_android.presentation.ui.infrom.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cherry_pick_android.databinding.DialogGenderBinding

class GenderDialog(
    genderDialogInterface: GenderDialogInterface
): DialogFragment() {
    private var genderDialogInterface: GenderDialogInterface? = null
    private val binding: DialogGenderBinding by lazy {
        DialogGenderBinding.inflate(layoutInflater)
    }

    init {
        this.genderDialogInterface = genderDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.5F)

        binding.tvWoman.setOnClickListener {
            this.genderDialogInterface?.onGenderClick(binding.tvWoman.text.toString())
            dismiss()
        }

        binding.tvMan.setOnClickListener {
            this.genderDialogInterface?.onGenderClick(binding.tvMan.text.toString())
            dismiss()
        }
        return binding.root
    }


}