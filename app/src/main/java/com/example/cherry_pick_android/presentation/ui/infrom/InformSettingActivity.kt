package com.example.cherry_pick_android.presentation.ui.infrom

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityInformSettingBinding
import com.example.cherry_pick_android.presentation.ui.home.HomeActivity
import com.example.cherry_pick_android.presentation.ui.infrom.dialog.GenderDialog
import com.example.cherry_pick_android.presentation.ui.infrom.dialog.GenderDialogInterface
import com.google.android.material.textfield.TextInputLayout

class InformSettingActivity: AppCompatActivity(), GenderDialogInterface {
    private val binding: ActivityInformSettingBinding by lazy {
        ActivityInformSettingBinding.inflate(layoutInflater)
    }
    // 각 버튼의 Flag
    private var nickFlag = false
    private var genderFlag = false
    private var birthFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

        // 다른 곳을 클릭할때 EditText focus 해제
        binding.root.setOnClickListener{
            hideKeyboard()
        }

        nickTextWatcher() // 닉네임 변경 감지
        birthTextWatcher() // 생일 변경 감지

        with(binding.tvComplete){
            setOnClickListener {
                if(isEnabled){
                    val intent = Intent(this@InformSettingActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        // 성별 선택
        binding.tvGenderChoice.setOnClickListener {
            val dialog = GenderDialog(this)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "GenderDialog")
        }
    }

    // 키보드 숨기고 포커스 해제 함수
    private fun hideKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
    }

    // 닉네임 감지 함수
    private fun nickTextWatcher(){
        val tiNick = binding.tiNick
        binding.etNick.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                updateIcon(!p0.isNullOrEmpty(), tiNick)
                nickFlag = !p0.isNullOrEmpty()
                isComplete()
            }
        })
    }

    // 아이콘 업데이트 함수
    private fun updateIcon(isField: Boolean, textInputLayout: TextInputLayout){
        if(isField){
            textInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            textInputLayout.endIconDrawable =
                AppCompatResources.getDrawable(this@InformSettingActivity, R.drawable.ic_setting_check)
        }else{
            textInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }

    }

    // 성별 확인
    override fun onGenderClick(gender: String){
        binding.tvGenderChoice.text = gender
        binding.tvGenderChoice.setTextColor(getColor(R.color.black))
        genderFlag = true
        binding.ivGenderCk.isVisible = true
    }

    // 힌트 애니메이션 삭제
    private fun initView(){
        binding.tiNick.isHintEnabled = false
        binding.tiBirth.isHintEnabled = false
    }

    // 생년월일 감지함수
    private fun birthTextWatcher(){
        val tiBirth = binding.tiBirth
        binding.etBirth.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val value = p0.toString()
                // 생년월일의 조건에 맞는 로직 구현
                if(value.isEmpty()){
                    tiBirth.error = null
                    birthFlag = false
                    updateIcon(false, tiBirth)
                    tiBirth.boxStrokeColor = ContextCompat.getColor(this@InformSettingActivity, R.color.gray_4)
                }
                else if(value.length == 8){
                    val year = value.substring(0, 4).toInt()
                    val month = value.substring(4, 6).toInt()
                    val day = value.substring(6, 8).toInt()
                    Log.d("testBirht", "year:$year month:$month day:$day")

                    if(!isYear(year) || !isMonth(month) || !isDay(day)){
                        isError()
                    }else{
                        tiBirth.error = null
                        tiBirth.boxStrokeColor = ContextCompat.getColor(this@InformSettingActivity, R.color.gray_4)
                        updateIcon(true, tiBirth)
                        birthFlag = true
                    }
                }
                else{
                    isError()
                }
                isComplete()
            }

        })

    }

    // 에러 설정
    private fun isError(){
        birthFlag = false
        binding.tiBirth.apply {
            error = "잘못된 입력방식 입니다"
            boxStrokeColor = ContextCompat.getColor(context, R.color.main_pink)
            errorIconDrawable?.setTint(ContextCompat.getColor(context, R.color.main_pink))
        }
    }
    // 연도 범위 조건
    private fun isYear(year: Int): Boolean{
        return year in 1900..2023
    }
    // 월 범위 조건
    private fun isMonth(month: Int): Boolean{
        return month in 1..12
    }
    // 일 범위 조건
    private fun isDay(day: Int): Boolean{
        return day in 1..31
    }

    // 완료 버튼 함수
    private fun isComplete(){
        if(nickFlag && genderFlag && birthFlag){
            with(binding.tvComplete){
                setBackgroundResource(R.drawable.ic_keyword_complete)
                isEnabled = true
            }
        }else{
            with(binding.tvComplete){
                setBackgroundResource(R.drawable.ic_setting_no_complete)
                isEnabled = false
            }
        }
    }


}