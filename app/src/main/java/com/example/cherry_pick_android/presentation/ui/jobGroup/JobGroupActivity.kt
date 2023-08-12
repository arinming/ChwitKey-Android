package com.example.cherry_pick_android.presentation.ui.jobGroup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityJobGroupBinding
import com.example.cherry_pick_android.domain.model.JobGroup
import com.example.cherry_pick_android.presentation.adapter.JobGroupAdapter
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroups.jobgroups
import com.example.cherry_pick_android.presentation.ui.mypage.ProfileActivity
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobGroupActivity : AppCompatActivity() {
    private lateinit var adapter: JobGroupAdapter
    private lateinit var gridView: GridView
    private lateinit var arrayList: ArrayList<String>
    private var flag = false
    private val loginViewModel: LoginViewModel by viewModels()
    private val binding: ActivityJobGroupBinding by lazy {
        ActivityJobGroupBinding.inflate(layoutInflater)
    }
    companion object{
        const val TAG = "JobGroupActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        gridView = binding.gvJobGroup
        arrayList = ArrayList()
        var selectedData: ArrayList<String>

        adapter = JobGroupAdapter(applicationContext, jobgroups, onCompleteButtonStateChanged)
        gridView?.adapter = adapter

        selectedData = adapter.getSelectedList()
        Log.d(TAG, "$selectedData")

        // 데이터 감지를 통해 최초 사용자 구분
        loginViewModel.getUserData().observe(this@JobGroupActivity, Observer {
            if(it.name != ""){
                flag = true
            }
        })

        onBackBtn()
        onCompleteBtn()

    }

    private fun GoToProfile() {
        val backbtn : ImageButton = binding.ibtnJobChangeBack
        backbtn.setOnClickListener{
            var selectedData = adapter.getSelectedList()
            Log.d(TAG,"$selectedData")

            finish()
        }
    }

    // 프로필페이지로 이동 = 완료버튼 클릭
    private fun clickcompletebutton() {
        val completeBtn : Button = binding.btnJobComplete
        completeBtn.setOnClickListener{
            var selectedData = adapter.getSelectedList()
            Log.d(TAG,"$selectedData")

            val intent = Intent(this, ProfileActivity::class.java).apply{
                putStringArrayListExtra("selectedJobList",selectedData)
            }
            setResult(Activity.RESULT_OK,intent)
            if(!isFinishing) finish()
        }
    }

    /* 주요 변경 사항 (밑에 2문단 코드) */
    private val onCompleteButtonStateChanged: () -> Unit = {
        updateCompleteButtonState() // 완료 버튼 상태 갱신
    }
    private fun updateCompleteButtonState() {
        val completeBtn: Button = binding.btnJobComplete
        val selectedJobList = adapter.getSelectedList()

        if (selectedJobList.size > 0) {
            completeBtn.setBackgroundResource(R.drawable.ic_job_complete_clicked)
            completeBtn.isEnabled = true
        } else {
            completeBtn.setBackgroundResource(R.drawable.ic_job_complete)
            completeBtn.isEnabled = false
        }
    }

    // 최초사용자의 여부에 따라 화면전환
    private fun onCompleteBtn(){
        binding.btnJobComplete.setOnClickListener {
            val selecetedJobList = adapter.getSelectedList()
            val resultIntent = Intent().apply {
                putStringArrayListExtra("selectedJobList", selecetedJobList)
            }
            setResult(Activity.RESULT_OK, resultIntent)

            Log.d(TAG, "completeFlag: ${flag}")
            if(!flag){
                val homeIntent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(homeIntent)
            }else{
                if(!isFinishing) finish()
            }
        }
    }

    private fun onBackBtn(){
        binding.ibtnJobChangeBack.setOnClickListener {
            finish()
        }
    }

}