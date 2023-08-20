package com.example.cherry_pick_android.presentation.ui.mypage

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cherry_pick_android.R
import com.example.cherry_pick_android.data.remote.request.user.UpLoadImageRequest
import com.example.cherry_pick_android.data.remote.request.user.updateNameRequest
import com.example.cherry_pick_android.data.remote.response.user.UpLoadImageResponse
import com.example.cherry_pick_android.data.remote.service.user.DeleteUserService
import com.example.cherry_pick_android.data.remote.service.user.UpLoadImageService
import com.example.cherry_pick_android.data.remote.service.user.UserDeleteImageService
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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
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
    @Inject
    lateinit var userDeleteImageService: UserDeleteImageService


    // 요청하고자 하는 권한들
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        //Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //Manifest.permission.READ_EXTERNAL_STORAGE
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

    //private lateinit var photofile : File
    private lateinit var cameraFileUri: Uri
    private lateinit var albumFileUri: Uri

    private lateinit var contentUri: Uri

    private var extraOutputFile: File? = null

    private fun requestCapture() : Uri {
        val dateTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        extraOutputFile = File(cacheDir, "${dateTime}.jpg")
        Log.d(TAG,"extraOutputFile: ${extraOutputFile}")
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", extraOutputFile!!)
        Log.d(TAG,"uri: ${uri}")
        return uri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.Capture.ordinal -> {
                    extraOutputFile?.let { originalCapturedImageFile ->
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Images.Media.TITLE, originalCapturedImageFile.name)
                            put(MediaStore.Images.Media.SIZE, originalCapturedImageFile.length())
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
                            contentUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
                            if (contentUri != null) {
                                contentResolver.openFileDescriptor(contentUri, "w")?.use { pfd ->
                                    val outputStream = FileOutputStream(pfd.fileDescriptor)
                                    copyFromTo(FileInputStream(extraOutputFile), outputStream)
                                    contentValues.clear()
                                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                                    contentResolver.update(contentUri, contentValues, null, null)
                                }
                                //loginViewModel.setUserData("memberImgUrl",contentUri.toString())
                            }
                        } else {
                            val externalFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), originalCapturedImageFile.name)
                            copyFromTo(FileInputStream(extraOutputFile), FileOutputStream(externalFile))
                            contentValues.put(MediaStore.Images.Media.DATA, externalFile.absolutePath)
                            contentUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
                            if (contentUri != null) {
                                //loginViewModel.setUserData("memberImgUrl",contentUri.toString())
                            } else{
                                //loginViewModel.setUserData("memberImgUrl",null)
                            }
                        }
                        if (originalCapturedImageFile.delete())
                            Toast.makeText(this, "Removed the garbage file. ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun copyFromTo(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(8192)
        while (true) {
            val data = inputStream.read(buffer)
            if (data == -1) {
                break
            }
            outputStream.write(buffer)
        }
        inputStream.close()
        outputStream.close()
    }

    // 앨범으로부터 이미지 불러오기
    private val getResultImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            albumFileUri = it.data?.data!!
            Glide.with(this).load(albumFileUri).circleCrop().into(binding.ivProfilePic)

            val pathTemp = albumFileUri.toString()
            //photofile = File(pathTemp)
            val photofile = File(absolutelyPath(albumFileUri, this))

            val requestFile = photofile.asRequestBody("image/*".toMediaTypeOrNull())
            val bodyFile =
                MultipartBody.Part.createFormData("image", photofile.name, requestFile)
            val request = UpLoadImageRequest(pathTemp)

            upLoadImageService.putUserImage(bodyFile, request)
                .enqueue(object : Callback<UpLoadImageResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<UpLoadImageResponse>,
                        response: retrofit2.Response<UpLoadImageResponse>
                    ) {
                        val statusCode = response.body()?.statusCode
                        if (statusCode != 200) {
                            Toast.makeText(
                                this@ProfileActivity,
                                "ERROR: $statusCode",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.d(TAG, "Success: $statusCode")
                    }
                    override fun onFailure(
                        call: retrofit2.Call<UpLoadImageResponse>,
                        t: Throwable
                    ) {
                        Log.d(TAG, "ERROR: ${t.message} / ${call.toString()}")
                    }
                })
        }
    }

    // 카메라를 실행한 후 찍은 사진을 저장
    private val getTakePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()) {
        if(it) {
            cameraFileUri.let {
                Glide.with(this).load(cameraFileUri).circleCrop().into(binding.ivProfilePic)

                val pathTemp = cameraFileUri.toString()
                //photofile = File(pathTemp)
                val photofile = File(absolutelyPath(cameraFileUri, this))

                val requestFile = photofile.asRequestBody("image/*".toMediaTypeOrNull())
                val bodyFile =
                    MultipartBody.Part.createFormData("image", photofile.name, requestFile)
                val request = UpLoadImageRequest(pathTemp)

                upLoadImageService.putUserImage(bodyFile, request)
                    .enqueue(object : Callback<UpLoadImageResponse> {
                        override fun onResponse(
                            call: retrofit2.Call<UpLoadImageResponse>,
                            response: retrofit2.Response<UpLoadImageResponse>
                        ) {
                            val statusCode = response.body()?.statusCode
                            if (statusCode != 200) {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    "ERROR: $statusCode",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Log.d(TAG, "Success: $statusCode")
                        }
                        override fun onFailure(
                            call: retrofit2.Call<UpLoadImageResponse>,
                            t: Throwable
                        ) {
                            Log.d(TAG, "ERROR: ${t.message} / ${call.toString()}")
                        }
                    })
            }
        }
    }

    //
    private val getPickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {uri: Uri? ->
        uri?.let {
            // 이미지를 사용하는 코드 작성
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
            }
        }

        // 닉네임 변경 감지
        userDataRepository.getNameLiveData().observe(this@ProfileActivity, Observer {
            binding.etProfileName.setText(it)
        })

        // 직군 키워드 업데이트
        userInfoLoad()
        goBack()
        showCameraDialog()
        ChangeName()
        ChangeJobInterest()
        LeaveAccount()

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

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

                getResultImage.launch(intent)
                //getPickImage.launch(outputFileUri)

            }
        }

    }


    // 카메라 촬영 후 이미지 불러오기
    override fun onCameraClick(intent:Intent) {
        checkPermission(permissions)
        //val uriName = requestCapture()
        //getTakePicture.launch(uriName)

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

                val path = getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES )
                path.mkdirs() // 임시 파일이 위치할 폴더 생성

                val file = File.createTempFile(
                    "my_img",
                    ".jpg",
                    path
                ) // 임시 파일 생성
                Log.d(TAG, "File name : ${file}")

                cameraFileUri = FileProvider.getUriForFile(
                    applicationContext,
                    "com.example.cherry_pick_android.provider",
                    file
                ) // uri 생성
                Log.d(TAG, "Uri Name : ${cameraFileUri}")

                getTakePicture.launch(cameraFileUri)

            }
        }
    }


    // 기본 이미지로 변경
    override fun onBasicClick(intent: Intent) {
        binding.ivProfilePic.setImageDrawable(getDrawable(R.drawable.ic_my_page_user))

        lifecycleScope.launch {
            val response = userDeleteImageService.deleteImage().body()?.statusCode
            // 200: 성공
            withContext(Dispatchers.Main){
                if(response == 200){
                    Log.d(TAG, "Success: $response")
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "통신오류: $response",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    // 이미지 변경 취소
    override fun onCancelClick(intent: Intent) {    }

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
            binding.etProfileName.requestFocus()

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


    // uri -> 절대경로 변환
    private fun createCopyAndReturnRealPath(uri: Uri) :String? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.getAbsolutePath()
        return mCurrentPhotoPath

    }

    override fun onResume() {
        userInfoLoad()
        super.onResume()
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

    private fun mapperToJob(value: String): String{
        return when(value){
            "steel" -> "철강" "Petroleum/Chemical" -> "석유·화학" "oilrefining" -> "정유" "secondarybattery" -> "2차 전지"
            "Semiconductor" -> "반도체" "Display" -> "디스플레이" "Mobile" -> "휴대폰" "It" -> "IT"
            "car" -> "자동차" "Shipbuilding" -> "조선" "Shipping" -> "해운" "FnB" -> "F&B"
            "RetailDistribution" -> "소매유통" "Construction" -> "건설" "HotelTravel" -> "호텔·여행·항공" "FiberClothing" -> "섬유·의류"
            else -> ""
        }
    }

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

    private fun userInfoLoad(){
        lifecycleScope.launch {
            val response = userInfoService.getUserInfo().body()
            val statusCode = response?.statusCode
            val industryResponse =
                "${mapperToJob(response?.data?.industryKeyword1.toString())}, " +
                        "${mapperToJob(response?.data?.industryKeyword2.toString())}, " +
                        mapperToJob(response?.data?.industryKeyword3.toString())
            withContext(Dispatchers.Main){
                if(statusCode == 200){
                    val imageResponse = response?.data?.memberImgUrl
                    binding.tvProfileJob.text = industryResponse
                    binding.etProfileName.setText(response.data?.name)
                    binding.tvBirth.text = mapperToBirth(response.data?.birthdate!!)
                    binding.tvGender.text = response.data.gender

                    // 이미지 로드
                    if(imageResponse!=null) {
                        Glide.with(applicationContext).load(imageResponse).circleCrop()
                            .into(binding.ivProfilePic)
                    }else{
                        binding.ivProfilePic.setImageDrawable(getDrawable(R.drawable.ic_my_page_user))
                    }
                }else{
                    Toast.makeText(this@ProfileActivity, "통신오류: $statusCode", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }
}

private fun mapperToBirth(birth: String): String{
    val year = birth.substring(0, 4)
    val month = birth.substring(4, 6)
    val day = birth.substring(6, 8)

    return "$year.$month.$day"
}

enum class RequestCode {
    Capture, CapturePermissions
}