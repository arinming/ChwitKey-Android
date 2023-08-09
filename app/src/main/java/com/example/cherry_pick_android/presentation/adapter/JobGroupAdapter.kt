package com.example.cherry_pick_android.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.domain.model.JobGroup


class JobGroupAdapter(var context: Context, private val jobgroups: List<JobGroup>) : BaseAdapter() {
    private var selectedjobList =arrayListOf<String>()

    override fun getCount(): Int = jobgroups.size

    override fun getItem(position: Int): Any {
        return jobgroups[position].jobgroup
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View =  LayoutInflater.from(context).inflate(R.layout.item_job_group, null)
        var btn: Button = view.findViewById(R.id.btn_job_group)
        //var surroundView: View =  LayoutInflater.from(context).inflate(R.layout.activity_job_group, null)
        //var completeBtn: Button = surroundView.findViewById(R.id.btn_complete)

        fun setItem(jobgroup: String) {
            btn.text= jobgroup
        }
        setItem(jobgroups[position].jobgroup)

        // 1개 이상 직군 선택시 완료 버튼 활성화
        /*fun activateCompleteBtn() {
            if (selectedjobList.size > 0) {
                completeBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.main_pink))
                completeBtn.setBackgroundResource(R.drawable.ic_job_complete_clicked)
                completeBtn.isEnabled = true
                Log.d("JobGroupActivity", "selectedData크기 : ${selectedjobList.size}이므로 버튼 활성화 ")
            } else {
                completeBtn.setBackgroundResource(R.drawable.ic_job_complete)
                completeBtn.isEnabled = false
            }
        }*/

        btn.setOnClickListener{
// 이미 선택했던 직군 선택시 버튼 변경
            if (selectedjobList.contains(btn.text)) {
                btn.setBackgroundResource(R.drawable.ic_job_button)
                btn.setTextColor(ContextCompat.getColor(context, R.color.black))
                selectedjobList.remove(btn.text)
                Log.d("JobGroupAdapter", "removed ${btn.text} from selectedjobList")
            }
            // 최대 선택 가능한 직군 개수(=3) 제한
            else if(selectedjobList.size==3) {
                // 아무일도 일어나지 않음^___^
            }
            // 새롭게 직군 선택시 버튼 변경
            else {
                btn.setBackgroundResource(R.drawable.ic_job_button_clicked)
                btn.setTextColor(ContextCompat.getColor(context, R.color.sub_bage))
                selectedjobList.add(btn.text.toString())
                Log.d("JobGroupAdapter", "added ${btn.text} from selectedjobList")
                Log.d("JobGroupAdapter", "selectedjobList크기 : ${selectedjobList.size} ")
            }

        }

        return view
    }

    fun getSelectedList(): ArrayList<String>{

        return selectedjobList

    }


}



