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
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityJobGroupBinding
import com.example.cherry_pick_android.domain.model.JobGroup
import com.example.cherry_pick_android.presentation.adapter.JobGroupAdapter
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroups.jobgroups
import com.example.cherry_pick_android.presentation.ui.mypage.ProfileActivity

class JobGroupActivity : AppCompatActivity() {
    private lateinit var adapter: JobGroupAdapter
    private lateinit var gridView: GridView
    private lateinit var arrayList: ArrayList<String>
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

        GoToProfile()
        clickcompletebutton()

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

}