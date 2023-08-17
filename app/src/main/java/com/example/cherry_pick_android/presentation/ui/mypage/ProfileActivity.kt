package com.example.cherry_pick_android.presentation.ui.mypage

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.request.user.UpLoadImageRequest
import com.example.cherry_pick_android.data.remote.request.user.updateNameRequest
import com.example.cherry_pick_android.data.remote.response.user.UpLoadImageResponse
import com.example.cherry_pick_android.data.remote.service.user.DeleteUserService
import com.example.cherry_pick_android.data.remote.service.user.UpLoadImageService
import com.example.cherry_pick_android.data.remote.service.user.UserInfoService
import com.example.cherry_pick_android.data.remote.service.user.UserNameUpdateService
import com.example.cherry_pick_android.databinding.ActivityProfileBinding
import com.example.cherry_pick_android.domain.repository.UserDataRepository
import com.example.cherry_pick_android.presentation.ui.jobGroup.JobGroupActivity
import com.example.cherry_pick_android.presentation.ui.login.LoginActivity
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialog
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.CameraDialogInterface
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.UserDeleteDialog
import com.example.cherry_pick_android.presentation.ui.mypage.dialog.UserDeleteDialogInterface
import com.example.cherry_pick_android.presentation.viewmodel.login.LoginViewModel
import com.example.cherry_pick_android.presentation.viewmodel.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(),CameraDialogInterface, UserDeleteDialogInterface {
    private lateinit var binding: ActivityProfileBinding

    companion object {
        const val TAG = "ProfileActivity"
    }
    // API 주입받기
    @Inject
    lateinit var deleteUserService: DeleteUserService
    @Inject
    lateinit var userNameUpdateService: UserNameUpdateService
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var userInfoService: UserInfoService
    @Inject
    lateinit var upLoadImageService: UpLoadImageService


    // 요청하고자 하는 권한들
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        // Manifest.permission.WRITE_EXTERNAL_STORAGE,
        // Manifest.permission.READ_EXTERNAL_STORAGE
    )
    // 권한 확인 응답
    private val getPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value == true }) {
        } else {
            Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    // 뷰 모델 주입받기
    private val viewModel: MyPageViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()


    // 촬영한 사진 불러오기
    // 이미지 로드
    private val readImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            //uri -> binding.ivProfilePic.load(uri)
    }

    // 카메라를 실행한 후 찍은 사진을 저장
    var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()) {
        if(it) { pictureUri.let {
            Glide.with(this).load(pictureUri).circleCrop().into(binding.ivProfilePic)
        }
        }
    }
    // 앨범으로부터 이미지 불러오기
    private val getResultImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            var imageUrl = it.data?.data
            Glide.with(this).load(imageUrl).circleCrop().into(binding.ivProfilePic)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 회원탈퇴 값 감지
        viewModel.isDelete.observe(this@ProfileActivity, Observer {
            Log.d(TAG, "옵저버 감지! : $it")
            deleteUserData(it)
        })

        // 유저 정보 갱신
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                binding.etProfileName.setText(userDataRepository.getUserData().name)
                binding.tvBirth.text = mapperToBirth(userDataRepository.getUserData().birthday)
                binding.tvGender.text = userDataRepository.getUserData().gender
            }
        }

        // 닉네임 변경 감지
        userDataRepository.getNameLiveData().observe(this@ProfileActivity, Observer {
            binding.etProfileName.setText(it)
        })

        // 직군 키워드 업데이트
        industryLoad()
        goBack()
        showCameraDialog()
        ChangeName()
        ChangeJobInterest()
        LeaveAccount()

    }

    override fun onResume() {
        industryLoad()
        super.onResume()
    }
    // 생년월일 매핑함수
    fun mapperToBirth(birthday: String): String{
        if(birthday.length == 8){
            val year = birthday.substring(0, 4)
            val month = birthday.substring(4, 6)
            val day = birthday.substring(6, 8)
            return "$year.$month.$day"
        }
        return birthday
    }

    // 마이페이지로 돌아가기 = 뒤로가기
    private fun goBack() {
        binding.ibtnProfileBack.setOnClickListener{
            finish()
        }
    }

    // 프로필 사진 변경
    fun showCameraDialog() {
        binding.ibtnProfileCamera.setOnClickListener{
            val dialog = CameraDialog(this)
            dialog.show(this.supportFragmentManager, "CameraDialog")
        }
    }
    // 앨범 이미지 불러오기
    override fun onAlbumClick(intent: Intent) {
        checkPermission(permissions)
        getResultImage.launch(intent)

    }
    // 카메라 촬영 후 이미지 불러오기
    override fun onCameraClick(intent:Intent) {
        lifecycleScope.launch {

            checkPermission(permissions)
            pictureUri = createImageFile()
            getTakePicture.launch(pictureUri)

            withContext(Dispatchers.Main) {
                val photofile = File(pictureUri.toString())
                val requestFile = photofile.asRequestBody("image/*".toMediaTypeOrNull())
                val bodyFile = MultipartBody.Part.createFormData("image", photofile.name, requestFile)
                val request = UpLoadImageRequest(pictureUri.toString())

                upLoadImageService.putUserImage(bodyFile, request).enqueue(object: Callback<UpLoadImageResponse>{
                    override fun onResponse(
                        call: retrofit2.Call<UpLoadImageResponse>,
                        response: retrofit2.Response<UpLoadImageResponse>
                    ) {
                        val status = response.body()?.status
                        if(status != 200){
                            Toast.makeText(this@ProfileActivity, "ERROR: $status", Toast.LENGTH_SHORT).show()
                        }
                        Log.d(TAG, "Success: $status")
                    }
                    override fun onFailure(
                        call: retrofit2.Call<UpLoadImageResponse>,
                        t: Throwable
                    ) {
                        Log.d(TAG, "ERROR: ${t.message} / ${call.toString()}")
                    }
                })


                /*lifecycleScope.launch {
                    val response = userUploadImageService.postUploadImage(bodyFile)
                }*/


            }
        }
    }
    // 기본 이미지로 변경
    override fun onBasicClick(intent: Intent) {
        binding.ivProfilePic.setImageDrawable(getDrawable(R.drawable.ic_my_page_user))
        //deleteImage
    }
    // 이미지 변경 취소
    override fun onCancelClick(intent: Intent) {    }

    // 고유 이름으로 이미지 파일 생성
    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(System.currentTimeMillis())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }



    // 직군정보 변경페이지로 이동
    private fun ChangeJobInterest() {
        binding.ibtnProfileJobChange.setOnClickListener {
            val intent = Intent(this, JobGroupActivity::class.java)
            startActivity(intent)
        }
    }

    // 이름 변경을 위해 edittext 활성화
    var cnt=0 // 연필버튼 또는 완료버튼
    private fun ChangeName() {
        binding.ibtnProfileNameChange.setOnClickListener{
            cnt+=1
            isNameChange(cnt)

            // 닉네임 수정
            if(cnt%2 == 0){
                val name = binding.etProfileName.text.toString()
                lifecycleScope.launch {
                    withContext(Dispatchers.Main){
                        val request = updateNameRequest(name)
                        val response = userNameUpdateService.putUserName(request)
                        val statusCode = response.body()?.statusCode
                        if(statusCode == 200){
                            loginViewModel.setUserData("name", binding.etProfileName.text.toString())
                        }else{
                            Toast.makeText(this@ProfileActivity, "통신오류: $statusCode", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    // 회원 탈퇴
    private fun LeaveAccount() {
        binding.tvLeaveAccount.setOnClickListener{
            val dialog = UserDeleteDialog(this)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "UserDeleteDialog")
        }
    }

    private fun checkPermission(permissions: Array<String>){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 권한 허용 여부 확인
            if (permissions.all {
                    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                }) {
                Toast.makeText(this, "카메라 권한이 허용되어 있습니다.", Toast.LENGTH_SHORT).show()
            }
            // 권한 요청
            else {
                getPermissionResult.launch(permissions)
            }
        }
    }

    override fun onClickBtn(value: String) {
        viewModel.setDelete(value)
    }

    private fun isNameChange(cnt: Int){
        if (cnt%2==0) {
            binding.etProfileName.isClickable= false
            binding.etProfileName.isFocusable= false
            binding.etProfileName.isFocusableInTouchMode= false
            binding.etProfileName.isEnabled= false
            binding.etProfileName.setTextColor(getColor(R.color.black))
            binding.ibtnProfileNameChange.setImageResource(R.drawable.ic_pencil)
        }
        else {
            binding.etProfileName.isClickable= true
            binding.etProfileName.isFocusable= true
            binding.etProfileName.isFocusableInTouchMode= true
            binding.etProfileName.isEnabled= true
            binding.etProfileName.setTextColor(getColor(R.color.main_pink))
            binding.ibtnProfileNameChange.setImageResource(R.drawable.ic_pencil_complete)
        }
    }

    // 영문->한글 매핑
    private fun mapperToJob(value: String): String{
        return when(value){
            "steel" -> "철강" "Petroleum/Chemical" -> "석유·화학" "oilrefining" -> "정유" "secondarybattery" -> "2차 전지"
            "Semiconductor" -> "반도체" "Display" -> "디스플레이" "Mobile" -> "휴대폰" "It" -> "IT"
            "car" -> "자동차" "Shipbuilding" -> "조선" "Shipping" -> "해운" "FnB" -> "F&B"
            "RetailDistribution" -> "소매유통" "Construction" -> "건설" "HotelTravel" -> "호텔·여행·항공" "FiberClothing" -> "섬유·의류"
            else -> ""
        }
    }

    // 회원탈퇴 기능
    private fun deleteUserData(ck: String){
        if(ck == "ok"){
            lifecycleScope.launch {
                val response = deleteUserService.deleteUser().body()?.statusCode
                // 200: 성공, 404: 존재하지 않는 유저
                withContext(Dispatchers.Main){
                    if(response == 200){
                        loginViewModel.setUserData("userId", "")
                        loginViewModel.setUserData("token", "")
                        loginViewModel.setUserData("platform", "")
                        loginViewModel.setIsOutView("out")

                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 백스택에 남아있는 액티비티 제거
                        }
                        startActivity(intent)
                    }else if(response == 404){
                        Log.d(TAG, "ERROR")
                    }else{
                        Toast.makeText(this@ProfileActivity, "통신 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // 직군 갱신
    private fun industryLoad(){
        lifecycleScope.launch {
            val response = userInfoService.getUserInfo().body()
            val statusCode = response?.statusCode
            val industryResponse =
                "${mapperToJob(response?.data?.industryKeyword1.toString())}, " +
                        "${mapperToJob(response?.data?.industryKeyword2.toString())}, " +
                        mapperToJob(response?.data?.industryKeyword3.toString())
            withContext(Dispatchers.Main){
                if(statusCode == 200){
                    binding.tvProfileJob.text = industryResponse
                }else{
                    Toast.makeText(this@ProfileActivity, "통신오류: $statusCode", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}