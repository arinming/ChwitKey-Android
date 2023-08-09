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
import com.example.cherry_pick_android.domain.model.JobGroup
import com.example.cherry_pick_android.presentation.adapter.JobGroupAdapter
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroups.jobgroups
import com.example.cherry_pick_android.presentation.ui.mypage.ProfileActivity

class JobGroupActivity : AppCompatActivity() {
    private lateinit var adapter: JobGroupAdapter
    private lateinit var gridView: GridView
    private lateinit var arrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_group)

        gridView = findViewById(R.id.gv_job_group)
        arrayList = ArrayList()
        adapter = JobGroupAdapter(applicationContext, jobgroups!!)
        gridView?.adapter = adapter

        var selectedData = adapter.getSelectedList()
        Log.d("JobGroupActivity", "$selectedData")

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.e("JobGroupActivity", "onItemCli ckListener is working")
                var jobgroupItem: JobGroup = jobgroups.get(position)
                arrayList.add(jobgroupItem.toString())
                Log.d("JobGroupActivity", "arrayList : $arrayList ")

                var selectedData = adapter.getSelectedList()
                Log.d("JobGroupActivity", "$selectedData")

            }

        GoToProfile()
        clickcompletebutton()
        //activateCompleteBtn()
    }

    private fun GoToProfile() {
        val backbtn : ImageButton = findViewById(R.id.ibtn_job_change_back)
        backbtn.setOnClickListener{
            var selectedData = adapter.getSelectedList()
            Log.d("JobGroupActivity","$selectedData")

            finish()
        }
    }

    // 프로필페이지로 이동 = 완료버튼 클릭
    private fun clickcompletebutton() {
        val completeBtn : Button = findViewById(R.id.btn_complete)
        completeBtn.setOnClickListener{
            var selectedData = adapter.getSelectedList()
            Log.d("JobGroupActivity","$selectedData")

            val intent = Intent(this, ProfileActivity::class.java).apply{
                putStringArrayListExtra("selectedJobList",selectedData)
            }
            setResult(Activity.RESULT_OK,intent)
            if(!isFinishing) finish()
        }
    }

    private fun activateCompleteBtn() {
        val completeBtn : Button = findViewById(R.id.btn_complete)

// 직군 1개이상 선택시 완료 버튼 활성화
            if (arrayList.size > 0) {
                completeBtn.setBackgroundResource(R.drawable.ic_job_complete_clicked)
                completeBtn.isEnabled= true
                Log.d("JobGroupActivity", "arrayList크기 : ${arrayList.size}이므로 버튼 활성화 ")

            }
            else{
                completeBtn.setBackgroundResource(R.drawable.ic_job_complete)
                completeBtn.isEnabled= false
            }

    }


}