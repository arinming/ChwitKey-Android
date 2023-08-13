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
import java.util.LinkedList
import java.util.Queue


class JobGroupAdapter(
    var context: Context,
    private val jobgroups: List<JobGroup>,
    private val onCompleteBtnCallback: ()->Unit
) : BaseAdapter() {
    private var selectedjobList = ArrayList<String>()
    companion object{
        const val TAG = "JobGroupAdapter"
    }
    override fun getCount(): Int = jobgroups.size

    override fun getItem(position: Int): Any {
        return jobgroups[position].jobgroup
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =  LayoutInflater.from(context).inflate(R.layout.item_job_group, null)
        val ivBtn: ImageView = view.findViewById(R.id.iv_job_group_btn)
        val tvBtn: TextView = view.findViewById(R.id.tv_job_group_btn)

        fun setItem(jobgroup: String) {
            tvBtn.text= jobgroup
        }
        setItem(jobgroups[position].jobgroup)

        fun resetItem(){
            // 이미 선택했던 직군 선택시 버튼 변경
            if (selectedjobList.contains(tvBtn.text)) {
                setBtn(ivBtn, tvBtn, false)
                selectedjobList.remove(mapperToJob(tvBtn.text.toString()))
                Log.d("JobGroupAdapter", "removed ${tvBtn.text} from selectedjobList")
            }
            // 최대 선택 가능한 직군 개수(=3) 제한
            else if(selectedjobList.size==3) {

            }
            // 새롭게 직군 선택시 버튼 변경
            else {
                setBtn(ivBtn, tvBtn, true)
                selectedjobList.add(mapperToJob(tvBtn.text.toString()))
                Log.d(TAG, selectedjobList.toString())
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

    // 클릭된 item = false, 클릭안된 item = true
    private fun setBtn(ivBtn: ImageView, tvBtn: TextView, flag: Boolean){
        if(flag){
            ivBtn.setBackgroundResource(R.drawable.ic_job_button_clicked)
            tvBtn.setTextColor(ContextCompat.getColor(context, R.color.sub_bage))
        }else{
            ivBtn.setBackgroundResource(R.drawable.ic_job_button)
            tvBtn.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

    }
    fun getSelectedList(): ArrayList<String>{

        return selectedjobList

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


}



