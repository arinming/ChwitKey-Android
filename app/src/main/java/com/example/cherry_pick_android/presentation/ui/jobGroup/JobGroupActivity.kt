package com.example.cherry_pick_android.presentation.ui.jobGroup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.request.login.SaveUserRequest
import com.example.cherry_pick_android.data.remote.service.login.SaveUserService
import com.example.cherry_pick_android.databinding.ActivityJobGroupBinding
import com.example.cherry_pick_android.presentation.adapter.JobGroupAdapter
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroups.jobgroups
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class JobGroupActivity: AppCompatActivity() {
    private lateinit var adapter: JobGroupAdapter
    private lateinit var gridView: GridView
    private lateinit var arrayList: ArrayList<String>
    private var userId = ""
    private var name = ""
    private var gender = ""
    private var birth = ""
    private var flag = false
    private val loginViewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var saveUserService: SaveUserService
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

        adapter = JobGroupAdapter(applicationContext, jobgroups, onCompleteButtonStateChanged)
        gridView.adapter = adapter

        val selectedData: ArrayList<String> = adapter.getSelectedList()
        Log.d(TAG, "$selectedData")

        // 데이터 감지를 통해 최초 사용자 구분
        loginViewModel.isInit.observe(this@JobGroupActivity, Observer {
            flag = it != "NO"
        })
        loginViewModel.getUserData().observe(this@JobGroupActivity, Observer {
            userId = it.userId
            name = it.name
            gender = it.gender
            birth = it.birthday
        })

        onBackBtn()
        onCompleteBtn()

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

            // Request body 설정
            if(!flag){
                Log.d(TAG, selecetedJobList.toString())
                val request = SaveUserRequest(
                    birthdate = birth,
                    gender = gender,
                    memberNumber = userId,
                    name = name,
                    industryKeyword1 = selecetedJobList.getOrElse(0){ "" },
                    industryKeyword2 = selecetedJobList.getOrElse(1){ "" },
                    industryKeyword3 = selecetedJobList.getOrElse(2){" "}
                )
                lifecycleScope.launch {
                    val saveUserResponse = saveUserService.saveUserInform(request)
                    val status = saveUserResponse.body()?.statusCode.toString()
                    withContext(Dispatchers.Main){
                        if(status == "200"){
                            val homeIntent = Intent(this@JobGroupActivity, HomeActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 백스택에 남아있는 액티비티 제거
                            }
                            startActivity(homeIntent)
                        }else{
                            Log.d(TAG, "오류: ${saveUserResponse.body()?.statusCode}")
                        }
                    }
                }

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