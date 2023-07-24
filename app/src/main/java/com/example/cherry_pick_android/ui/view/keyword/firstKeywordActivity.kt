package com.example.cherry_pick_android.ui.view.keyword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.databinding.ActivityFirstkeywordBinding
import com.example.cherry_pick_android.ui.view.MainActivity
import java.util.LinkedList
import java.util.Queue

class firstKeywordActivity: AppCompatActivity() {
    lateinit var binding: ActivityFirstkeywordBinding
    private val maxSelectedKeywords = 3 // 최대 선택 가능한 키워드 버튼 개수

    companion object{
        const val TAG = "KeywordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firstkeyword)
        Log.d(TAG, "onCreate")

        val keyword = getKeyword() // 키워드 버튼 리스트 반환받기
        val selectedKeywords: Queue<Int> = LinkedList<Int>() // 선택된 키워드 버튼을 저장할 Queue

        /* 데이터 변경 감지 -> 백엔드 개발 완료되면 수정 예정
        keywordViewModel.userData.observe(this, Observer {
            binding.tvName.text = it.name + "님이 지원을 희망하는"
        })*/

        // 키워드 버튼 이벤트 처리 로직
        keyword.forEach { button ->
            button.setOnClickListener {
                val buttonId = button.id

                // 클릭된 버튼을 누르면 큐의 원소 삭제 및 ui 업데이트
                if(selectedKeywords.contains(buttonId)){
                    setButtonColor(buttonId, false)
                    selectedKeywords.remove(buttonId)
                }
                else{
                    // 최대 지정 키워드 개수가 넘어가는 경우 제일 첫번째 원소를 삭제 및 ui 업데이트
                    if(selectedKeywords.size >= maxSelectedKeywords){
                        val firstSelectedButtonId = selectedKeywords.poll()
                        setButtonColor(firstSelectedButtonId!!, false)
                    }
                    // 큐에 키워드 원소 추가
                    setButtonColor(buttonId, true)
                    selectedKeywords.add(buttonId)
                }
                // 선택된 키워드 개수에 따른 완료버튼 ui 이벤트 함수이용
                isCompleteCheck(selectedKeywords.size)
            }
            // 완료버튼을 누를 시 메인화면으로 이동
            binding.btnComplete.setOnClickListener {
                passSelectedKeywordData()
                finish()
            }
        }


    }

    // 키워드 버튼 리스트 생성
    private fun getKeyword(): List<Button>{
        val keyWordButton = listOf<Button>(
            binding.btnMarine, binding.btnBattery, binding.btnCar, binding.btnBuild,
            binding.btnFB, binding.btnClothes, binding.btnDisplay, binding.btnDistribution,
            binding.btnIT, binding.btnLeisure, binding.btnOil, binding.btnPetrochemistry,
            binding.btnPhone, binding.btnShipbuild, binding.btnSemiconductor, binding.btnSteel
        )
        return keyWordButton
    }

    // 선택된 키워드 데이터 넘겨주기 및 화면전환 (뉴스화면)
    private fun passSelectedKeywordData() {
        /* 데이터 넘겨주는 로직*/
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // 선택된 버튼 색상 변경 로직
    private fun setButtonColor(buttonId: Int, isSelected: Boolean){
        if(isSelected){
            findViewById<Button>(buttonId).setBackgroundResource(R.drawable.keyword_btn_select)
            findViewById<Button>(buttonId).setTextColor(getColor(R.color.white))
        }else{
            findViewById<Button>(buttonId).setBackgroundResource(R.drawable.keyword_btn_basic)
            findViewById<Button>(buttonId).setTextColor(getColor(R.color.black))
        }
    }

    // 선택된 키워드 개수를 통한 완료버튼 ui 이벤트
    private fun isCompleteCheck(size: Int){
        if(size > 0){
            findViewById<Button>(binding.btnComplete.id).setBackgroundResource(R.drawable.keyword_btn_complete_select)
            binding.btnComplete.isEnabled = true
        }
        else if(size == 0){
            findViewById<Button>(binding.btnComplete.id).setBackgroundResource(R.drawable.keyword_btn_complete_basic)
            binding.btnComplete.isEnabled = false
        }

    }

}