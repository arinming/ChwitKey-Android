package com.example.cherry_pick_android.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityJobGroupBinding
import com.example.cherry_pick_android.domain.model.JobGroup
import dagger.hilt.android.AndroidEntryPoint


class JobGroupAdapter(
    var context: Context,
    private val jobgroups: List<JobGroup>,
    private val onCompleteBtnCallback: ()->Unit
) : BaseAdapter() {
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
        var ivBtn: ImageView = view.findViewById(R.id.iv_job_group_btn)
        var tvBtn: TextView = view.findViewById(R.id.tv_job_group_btn)

        fun setItem(jobgroup: String) {
            tvBtn.text= jobgroup
        }
        setItem(jobgroups[position].jobgroup)

        fun resetItem(){
            // 이미 선택했던 직군 선택시 버튼 변경
            if (selectedjobList.contains(tvBtn.text)) {
                ivBtn.setBackgroundResource(R.drawable.ic_job_button)
                tvBtn.setTextColor(ContextCompat.getColor(context, R.color.black))
                selectedjobList.remove(tvBtn.text)
                Log.d("JobGroupAdapter", "removed ${tvBtn.text} from selectedjobList")
            }
            // 최대 선택 가능한 직군 개수(=3) 제한
            else if(selectedjobList.size==3) {

            }
            // 새롭게 직군 선택시 버튼 변경
            else {
                ivBtn.setBackgroundResource(R.drawable.ic_job_button_clicked)
                tvBtn.setTextColor(ContextCompat.getColor(context, R.color.sub_bage))
                selectedjobList.add(tvBtn.text.toString())
                Log.d("JobGroupAdapter", "added ${tvBtn.text} from selectedjobList")
                Log.d("JobGroupAdapter", "selectedjobList크기 : ${selectedjobList.size} ")
            }
            /* 변경사항 */
            onCompleteBtnCallback.invoke()

        }

        tvBtn.setOnClickListener{
            resetItem()
        }
        ivBtn.setOnClickListener{
            resetItem()
        }

        return view
    }

    fun getSelectedList(): ArrayList<String>{

        return selectedjobList

    }


}



