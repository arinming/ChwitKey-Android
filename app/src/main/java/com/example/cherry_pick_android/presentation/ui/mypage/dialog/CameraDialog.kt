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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
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

    // 요청하고자 하는 권한들
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    //var hasCameraPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    //var hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    //var hasReadPerm = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.5F)
        dialog?.window?.setGravity(Gravity.BOTTOM)

        binding.tvImgFromAlbum.setOnClickListener {
            //binding.tvImgFromAlbum.setTextColor(requireContext().getColor(R.color.main_pink))
            //context?.let { it1 -> binding.tvImgFromAlbum.setTextColor(it1.getColor(R.color.main_pink)) }
            this.cameraDialogInterface?.onCameraClick(binding.tvImgFromAlbum.toString())
        }
        binding.tvImgFromCamera.setOnClickListener {
            Log.d(TAG, "카메라로부터 이미지변경")
            this.cameraDialogInterface?.onCameraClick(binding.tvImgFromCamera.toString())
        }
        binding.tvImgBasic.setOnClickListener {
            this.cameraDialogInterface?.onCameraClick(binding.tvImgBasic.toString())
        }
        binding.tvImgCancel.setOnClickListener {
            Log.d(TAG, "프로필이미지변경 취소")
            this.cameraDialogInterface?.onCameraClick(binding.tvImgCancel.toString())
            dismiss()
        }
        return binding.root
    }

}


