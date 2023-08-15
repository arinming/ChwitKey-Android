package com.example.cherry_pick_android.presentation.ui.mypage

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.bumptech.glide.Glide
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityProfileBinding
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroupActivity
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialog
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialogInterface
import java.sql.Date
import java.text.SimpleDateFormat


class ProfileActivity : AppCompatActivity(),CameraDialogInterface {
    private lateinit var binding: ActivityProfileBinding

    companion object {
        const val TAG = "ProfileActivity"
    }
    // 요청하고자 하는 권한들
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        // Manifest.permission.WRITE_EXTERNAL_STORAGE,
        // Manifest.permission.READ_EXTERNAL_STORAGE
    )
    // 권한 확인 응답
    private val getPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value == true }) {
        } else {
            Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 촬영한 사진 불러오기
    // 이미지 로드
    private val readImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            //uri -> binding.ivProfilePic.load(uri)
    }

    // 카메라를 실행한 후 찍은 사진을 저장
    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()) {
        if(it) { pictureUri.let {
                Glide.with(this).load(pictureUri).circleCrop().into(binding.ivProfilePic)
            }
        }
    }
    // 앨범으로부터 이미지 불러오기
    private val getResultImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            var imageUrl = it.data?.data
            Glide.with(this).load(imageUrl).circleCrop().into(binding.ivProfilePic)
        }
    }

    // 직군선택 결과 받아오기
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result-> if (result.resultCode == RESULT_OK){
                val returnString = result.data?.getExtras()?.getStringArrayList("selectedJobList")
                Log.e("ProfileActivity","${returnString}")
                binding.tvProfileJob.text = returnString?.joinToString(", ")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        goBack()
        showCameraDialog()
        ChangeName()
        ChangeJobInterest()
        LeaveAccount()

    }

    // 마이페이지로 돌아가기 = 뒤로가기
    private fun goBack() {
        binding.ibtnProfileBack.setOnClickListener{
            finish()
        }
    }

    // 프로필 사진 변경
    fun showCameraDialog() {
        binding.ibtnProfileCamera.setOnClickListener{
            val dialog = CameraDialog(this)
            dialog.show(this.supportFragmentManager, "CameraDialog")
        }
    }
    // 앨범 이미지 불러오기
    override fun onAlbumClick(intent: Intent) {
        checkPermission(permissions)
        getResultImage.launch(intent)
    }
    // 카메라 촬영 후 이미지 불러오기
    override fun onCameraClick(intent:Intent) {
        checkPermission(permissions)
        pictureUri = createImageFile()
        getTakePicture.launch(pictureUri)
    }
    // 기본 이미지로 변경
    override fun onBasicClick(intent: Intent) {
        binding.ivProfilePic.setImageDrawable(getDrawable(R.drawable.ic_my_page_user))
    }
    // 이미지 변경 취소
    override fun onCancelClick(intent: Intent) {    }

    // 고유 이름으로 이미지 파일 생성
    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(System.currentTimeMillis())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }


    // 직군정보 변경페이지로 이동
    private fun ChangeJobInterest() {
        binding.ibtnProfileJobChange.setOnClickListener {
            val intent = Intent(this, JobGroupActivity::class.java)
            getResultText.launch(intent)
        }
    }

    // 이름 변경을 위해 edittext 활성화
    var cnt=0 // 연필버튼 또는 완료버튼
    private fun ChangeName() {
        binding.ibtnProfileNameChange.setOnClickListener{
            cnt+=1

            if (cnt%2==0) {
                binding.etProfileName.isClickable= false
                binding.etProfileName.isFocusable= false
                binding.etProfileName.isFocusableInTouchMode= false
                binding.etProfileName.isEnabled= false
                binding.etProfileName.setTextColor(getColor(R.color.black))
                binding.ibtnProfileNameChange.setImageResource(R.drawable.ic_pencil)
            }
            else {
                binding.etProfileName.isClickable= true
                binding.etProfileName.isFocusable= true
                binding.etProfileName.isFocusableInTouchMode= true
                binding.etProfileName.isEnabled= true
                binding.etProfileName.setTextColor(getColor(R.color.main_pink))
                binding.ibtnProfileNameChange.setImageResource(R.drawable.ic_pencil_complete)
            }
        }
    }

    // 회원 탈퇴
    private fun LeaveAccount() {
        binding.tvLeaveAccount.setOnClickListener{

        }
    }

    private fun checkPermission(permissions: Array<String>){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 권한 허용 여부 확인
            if (permissions.all {
                    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                }) {
                Toast.makeText(this, "카메라 권한이 허용되어 있습니다.", Toast.LENGTH_SHORT).show()
            }
            // 권한 요청
            else {
                getPermissionResult.launch(permissions)
            }
        }
    }
}