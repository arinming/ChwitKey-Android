package com.example.cherry_pick_android.presentation.ui.jobGroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.request.user.IndustryKeyword
import com.example.cherry_pick_android.data.remote.request.user.InitUserSaveRequest
import com.example.cherry_pick_android.data.remote.request.user.updateIndustryRequest
import com.example.cherry_pick_android.data.remote.service.user.InitUserSaveService
import com.example.cherry_pick_android.data.remote.service.user.UpdateIndustryService
import com.example.cherry_pick_android.databinding.ActivityJobGroupBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
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
    private var nickname = ""
    private var gender = ""
    private var birth = ""
    private val loginViewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var initUserSaveService: InitUserSaveService
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var updateIndustryService: UpdateIndustryService

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

        // 유저정보 갱신
        loginViewModel.getUserData().observe(this@JobGroupActivity, Observer {
            userId = it.userId
            nickname = it.name
            gender = it.gender
            birth = it.birthday
        })

        onBackBtn()
        onCompleteBtn()

    }

    private val onCompleteButtonStateChanged: () -> Unit = {
        updateCompleteButtonState() // 완료 버튼 상태 갱신
    }

    // 직군 선택 개수에 따른 완료버튼 이벤트
    private fun updateCompleteButtonState() {
        val completeBtn: Button = binding.btnJobComplete
        val selectedJobList = adapter.getSelectedList()

        if (selectedJobList.size > 0) {
            completeBtn.setBackgroundResource(R.drawable.bg_active_btn)
            completeBtn.isEnabled = true
        } else {
            completeBtn.setBackgroundResource(R.drawable.bg_disabled_btn)
            completeBtn.isEnabled = false
        }
    }

    // 직군을 영문으로 매핑 작업
    fun mapperToJob(value: String): String{
        return when(value){
            "철강" -> "steel" "석유·화학" -> "Petroleum/Chemical" "정유" -> "oilrefining" "2차 전지" -> "secondarybattery"
            "반도체" -> "Semiconductor" "디스플레이" -> "Display" "휴대폰" -> "Mobile" "IT" -> "it"
            "자동차" -> "car" "조선" -> "Shipbuilding" "해운" -> "Shipping" "F&B" -> "FnB"
            "소매유통" -> "RetailDistribution" "건설" -> "Construction" "호텔·여행·항공" -> "HotelTravel" "섬유·의류" -> "FiberClothing"
            else -> ""
        }
    }

    // 최초사용자의 여부에 따라 화면전환
    private fun onCompleteBtn(){
        binding.btnJobComplete.setOnClickListener {
            val selecetedJobList = adapter.getSelectedList()

            lifecycleScope.launch {
                val isInit = userDataRepository.getUserData().isInit
                Log.d(TAG, "list:${getIndustryList(selecetedJobList)}")
                // 최초 사용자일 경우 직군 업데이트
                if(isInit == "InitUser"){
                    val request = InitUserSaveRequest(
                        birth = birth,
                        gender = gender,
                        nickname = nickname,
                        industryKeywords = getIndustryList(selecetedJobList)
                    )
                    val response = initUserSaveService.putInitUserData(request)
                    val statusCode = response.body()?.statusCode

                    withContext(Dispatchers.Main){
                        if(statusCode == 200){
                            val homeIntent = Intent(this@JobGroupActivity, HomeActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 백스택에 남아있는 액티비티 제거
                            }
                            loginViewModel.setUserData("isInit", "exitUser")
                            startActivity(homeIntent)
                        }else{
                            Log.d(TAG, "STATUS ERROR")
                        }
                    }
                }
                // 기존 사용자일 경우 직군 업데이트
                else if(isInit == "exitUser"){
                    val industryList = getIndustryList(selecetedJobList)
                    withContext(Dispatchers.Main){
                        val request = updateIndustryRequest(
                            industryKeyword1 = industryList[0].industryKeyword,
                            industryKeyword2 = industryList[1].industryKeyword,
                            industryKeyword3 = industryList[2].industryKeyword
                            )
                        val response = updateIndustryService.putUpdateUserIndustry(request)
                        val statusCode = response.body()?.statusCode

                        if(statusCode == 200){
                            finish()
                        }else{
                            Toast.makeText(this@JobGroupActivity, "통신오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Log.d(TAG, "TYPE ERROR : userStatus[${isInit}]")
                }
            }
        }
    }

    private fun onBackBtn(){
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun getIndustryList(list: ArrayList<String>): List<IndustryKeyword> {
        val size = list.size
        val industryList = mutableListOf<IndustryKeyword>()

        for (i in 0 until 3) {
            if (i < size) {
                industryList.add(IndustryKeyword(mapperToJob(list[i])))
            } else {
                industryList.add(IndustryKeyword("")) // 원하는 기본 값으로 채워넣기
            }
        }
        return industryList
    }


}