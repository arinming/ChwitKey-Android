package com.example.cherry_pick_android.presentation.ui.mypage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityProfileBinding
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroupActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

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
        ChangeImage()
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
    private fun ChangeImage() {
        binding.ivProfilePic.setOnClickListener{

            // 앨범에서 사진 선택

            // 촬영하기->고유 이름으로 파일 생성->갤러리에 사진추가
            //dispatchTakePictureIntent()
            //createImageFile()
            //galleryAddPic()
            // 기본 이미지로 변경

            // 취소

        }
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