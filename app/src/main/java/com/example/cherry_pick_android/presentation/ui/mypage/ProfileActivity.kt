package com.example.cherry_pick_android.presentation.ui.mypage

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityProfileBinding
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroupActivity
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialog
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialogInterface
import java.sql.Date
import java.text.SimpleDateFormat


class ProfileActivity : AppCompatActivity()/*,CameraDialogInterface*/ {
    private lateinit var binding: ActivityProfileBinding

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 1001
        const val ALBUM_OK = 2002
        const val CAMERA_OK = 2003
        const val BASIC_OK = 2004
        const val TAG = "ProfileActivity"
    }


    private fun checkPermission(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // 이미지 로드
    private val readImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            uri -> binding.ivProfilePic.load(uri)
    }

    // 카메라를 실행한 후 찍은 사진을 저장
    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            pictureUri.let { binding.ivProfilePic.setImageURI(pictureUri) }
        }
    }
    // 파일 불러오기
    private val getContentImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            uri ->
                uri.let { binding.ivProfilePic.setImageURI(uri)
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
    override fun onCameraClick(cameraMenu: String) {
        val camera_menu = cameraMenu

        //readImage.launch()
        // 권한 확인
        //checkPermission.launch(permissionList)
        //if (!checkPermission(permissions)) {
          //  requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        //}

        if (camera_menu=="@string/profile_img_fromAlbum"){
            getContentImage.launch("image/*")
        }
        else if (camera_menu=="@string/profile_img_fromCamera"){
            pictureUri = createImageFile()
            getTakePicture.launch(pictureUri)
        }
        else if (camera_menu=="@string/profile_img_basic"){
            binding.ivProfilePic.setImageResource(R.drawable.ic_my_page_user)
        }
        else if (camera_menu=="@string/profile_img_cancel"){
            // 변화없음
        }
    }
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
            //val intent = Intent(this,JobGroupActivity::class.java)
            //startActivity(intent)
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

}