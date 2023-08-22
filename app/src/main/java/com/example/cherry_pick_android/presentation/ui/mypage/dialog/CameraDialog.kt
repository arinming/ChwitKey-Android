package com.example.cherry_pick_android.presentation.ui.mypage.dialog

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import coil.load
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.DialogCameraBinding
import com.example.cherry_pick_android.databinding.DialogGenderBinding
import com.example.cherry_pick_android.presentation.ui.infrom.dialog.GenderDialogInterface
import com.example.cherry_pick_android.presentation.ui.mypage.ProfileActivity
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import java.text.SimpleDateFormat

class CameraDialog (
    cameraDialogInterface: CameraDialogInterface
): DialogFragment() {
    companion object {
        const val TAG = "CameraDialog"
    }

    private var cameraDialogInterface: CameraDialogInterface? = null
    private val binding: DialogCameraBinding by lazy {
        DialogCameraBinding.inflate(layoutInflater)
    }

    init {
        this.cameraDialogInterface = cameraDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.5F)
        dialog?.window?.setGravity(Gravity.BOTTOM)

        dialog?.setCancelable(false)

        binding.tvImgFromAlbum.setOnClickListener {
            Log.d(TAG, "앨범으로부터 이미지변경")
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            this.cameraDialogInterface?.onAlbumClick(photoPickerIntent)

            dismiss()
        }
        binding.tvImgBasic.setOnClickListener {
            Log.d(TAG, "기본 이미지로 변경")
            activity?.let {
                var photoBasicIntent = Intent(it, ProfileActivity::class.java)
                this.cameraDialogInterface?.onBasicClick(photoBasicIntent)
            }

            dismiss()
        }
        binding.tvImgCancel.setOnClickListener {
            Log.d(TAG, "프로필이미지 변경 취소")
            activity?.let {
                var photoCancelIntent = Intent(it, ProfileActivity::class.java)
                this.cameraDialogInterface?.onCancelClick(photoCancelIntent)
            }
            dismiss()
        }
        return binding.root
    }






}


