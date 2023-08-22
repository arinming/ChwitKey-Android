package com.chwitkey.cherry_pick_android.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chwitkey.cherry_pick_android.R
import com.chwitkey.cherry_pick_android.data.remote.service.user.UserInfoService
import com.chwitkey.cherry_pick_android.domain.model.JobGroup
import javax.inject.Inject


class JobGroupAdapter(
    var context: Context,
    private val jobgroups: List<JobGroup>,
    private val onCompleteBtnCallback: ()->Unit

) : BaseAdapter() {
    @Inject
    lateinit var userInfoService: UserInfoService

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
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_job_group, null)
        val ivBtn: ImageView = view.findViewById(R.id.iv_job_group_btn)
        val tvBtn: TextView = view.findViewById(R.id.tv_job_group_btn)

        fun setItem(jobgroup: String) {
            tvBtn.text = jobgroup
        }
        setItem(jobgroups[position].jobgroup)

        tvBtn.setOnClickListener {
            resetButtonUI(ivBtn, tvBtn)
        }
        ivBtn.setOnClickListener {
            resetButtonUI(ivBtn, tvBtn)
        }

        return view
    }

    private fun resetButtonUI(ivBtn: ImageView, tvBtn: TextView) {
        if (selectedjobList.contains(tvBtn.text)) {
            setBtn(ivBtn, tvBtn, false)
            selectedjobList.remove(tvBtn.text.toString())
            Log.d("JobGroupAdapter", "removed ${tvBtn.text} from selectedjobList")
        }
        else if (selectedjobList.size == 3) {

        } else {
            setBtn(ivBtn, tvBtn, true)
            selectedjobList.add(tvBtn.text.toString())
            Log.d(TAG, selectedjobList.toString())
        }
        Log.d(TAG, "${selectedjobList}")
        onCompleteBtnCallback.invoke()
    }

    // 클릭된 item = false, 클릭안된 item = true
    private fun setBtn(ivBtn: ImageView, tvBtn: TextView, flag: Boolean){
        Log.d(TAG, "setBtn:${flag} ivBtn:${ivBtn} tvBtn:${tvBtn}")
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
}



