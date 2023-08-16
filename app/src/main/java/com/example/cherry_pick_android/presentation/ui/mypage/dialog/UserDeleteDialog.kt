package com.example.cherry_pick_android.presentation.ui.mypage.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cherry_pick_android.databinding.DialogDeleteUserBinding

class UserDeleteDialog(
    userDeleteDialogInterface: UserDeleteDialogInterface
): DialogFragment() {
    private var userDeleteDialogInterface: UserDeleteDialogInterface ?= null
    private val binding: DialogDeleteUserBinding by lazy {
        DialogDeleteUserBinding.inflate(layoutInflater)
    }

    init {
        this.userDeleteDialogInterface = userDeleteDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dialog?.window?.setDimAmount(0.5F)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnOk.setOnClickListener {
            this.userDeleteDialogInterface?.onClickBtn(OK)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            this.userDeleteDialogInterface?.onClickBtn(NO)
            dismiss()
        }
        return binding.root
    }

    companion object{
        const val OK = "ok"
        const val NO = "no"
    }

}