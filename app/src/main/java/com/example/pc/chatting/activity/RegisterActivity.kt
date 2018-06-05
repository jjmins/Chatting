package com.example.pc.chatting.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pc.chatting.R
import com.example.pc.chatting.SignUp
import com.example.pc.chatting.util.RetrofitUtil
import kotlinx.android.synthetic.main.activity_resiger.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import android.view.View
import pub.devrel.easypermissions.EasyPermissions

class RegisterActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks{
    var id : String = ""
     var passwd : String = ""
     var name : String = ""
     var email : String = ""
     var phone : String = ""
     var emptycheck : Boolean = false
     var path : String? = null
     var uri: Uri? = null
     var file : File? = null

    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resiger)

        userPassword.hint = "Password"
        userName.hint =  "User Name"
        userEmail.hint = "User Email"
        userPhone.hint = "Phone Number"

        userProfile.setOnClickListener{
            var intent = Intent(this,CameraPopupActivity::class.java)
            startActivityForResult(intent,1)
        }

        backBtn.setOnClickListener{
            finish()
        }

        completeBtn.setOnClickListener {
            passwd = userPassword.text.toString()
            name = userName.text.toString()
            email = userEmail.text.toString()
            phone = userPhone.text.toString()

            var emails = ("^[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+$")//email 정규식
            var passwords = ("^(?=.*?[A-Za-z])(?=.*?[0-9]).{8,}$")//passwd 정규식
            var phones = ("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$") //phone number 정규식
            val emailm = Pattern.matches(emails,userEmail.text)
            val phonem = Pattern.matches(phones,userPhone.text)
            val passwordm = Pattern.matches(passwords,userPassword.text)
            //정규식 변환

            Log.e("i", emailm.toString())

            if(!passwordm){
                emptycheck = true
                userPassword.error = "8자 이상 입력하고 숫자와 영문자를 적어도 하나 이상 입력해주세요"
                userPassword.requestFocus()
            }

            if(!emailm){
                emptycheck = true
                userEmail.error = "이메일 형식에 맞게 입력하세요"
                userEmail.requestFocus()
            }

            if(!phonem){
                emptycheck = true
                userPhone.error = "휴대폰번호 형식에 맞게 입력하세요" +
                        "ex)01000000000"
                userPhone.requestFocus()
            }

            if(email.isEmpty()) {
                emptycheck = true
                userEmail.error = "이메일을 입력하십시오"
                userEmail.requestFocus()
            }
            if(passwd.isEmpty()){
                emptycheck = true
                userPassword.error = "비밀번호를 입력하십시오"
                userPassword.requestFocus()
            }
            if(name.isEmpty()){
                emptycheck = true
                userName.error = "이름을 입력하십시오"
                userName.requestFocus()
            }
            if(phone.isEmpty()){
                emptycheck = true
                userPhone.error = "전화번호를 입력하십시오"
                userPhone.requestFocus()
            }
            if(!emptycheck){
                signup()
            }
        }
    }


    fun camera(){
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.e("cameraUri", fileUri.toString())
        fileUri = getOutputMediaFileUri()
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(cameraIntent, 100)
    }

    fun gallery(){
           val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent,200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == 0){
            var img = data?.getStringExtra("img")
            if(img == "basic") {
                //setdata
                Profile.setImageResource(R.drawable.emptyimg)
                cameraImg.visibility = View.GONE
            }
        }
        if(resultCode == 1) {
            camera()
        }
        if(resultCode == 2){
            gallery()
        }

        if(requestCode == 200 && resultCode === Activity.RESULT_OK){
            uri = data!!.data
            if(EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Profile.setImageURI(uri)
                cameraImg.visibility = View.GONE
                file = File(getRealPathFromURIPath(uri!!,this))
                Log.e("uripath", file.toString())
            } else {
                EasyPermissions.requestPermissions(this,"파일을 읽으려면 권한이 필요합니다",300, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (requestCode == 100 && resultCode === Activity.RESULT_OK) {
            //RESULT_OK -> 카메라를 실제로 찍었는지, 취소로 나갔는지
            Log.e("camera", "camera")
            var resultBitmap : Bitmap? = null

            uri = fileUri
            Profile.setImageURI(uri)
            cameraImg.visibility = View.GONE
            var filepath = getRealPathFromURIPath(uri!!,this)
            file = File(filepath)
            Log.e("filepath", filepath)

//            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,fileUri)
//                var ei = ExifInterface(path)
//                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                        ExifInterface.ORIENTATION_UNDEFINED)
//
//                when (orientation) {
//                    ExifInterface.ORIENTATION_ROTATE_90 -> {
//                        resultBitmap = rotateImage(bitmap, 90F)
//                    }
//                    ExifInterface.ORIENTATION_ROTATE_180 -> {
//                        resultBitmap = rotateImage(bitmap, 180F)
//                    }
//                    ExifInterface.ORIENTATION_ROTATE_270 -> {
//                        resultBitmap = rotateImage(bitmap, 270F)
//                    }
//                    ExifInterface.ORIENTATION_NORMAL -> {
//                    }
//                    else -> {
//                        resultBitmap = bitmap
//                    }
//                }
//
//
//
//                Profile.setImageBitmap(resultBitmap)
//                cameraImg.visibility = View.GONE
//
//            } catch (e: IOException) {
//
//                // TODO Auto-generated catch block
//
//                e.printStackTrace()
//            }
        }
    }

    fun signup(){

        val res : Call<SignUp> = RetrofitUtil.postService.SignUp(
                email,
                passwd,
                name,
                Integer.parseInt(userPhone.text.toString()),
                RetrofitUtil.createMultipartBody(file!!,"img")
        )

        res.enqueue(object : Callback<SignUp>{
            override fun onResponse(call: Call<SignUp>?, response: Response<SignUp>?) {
                Log.e("register",response!!.code().toString())
                Log.e("register_Message",response!!.message())
                if(response!!.code() == 200){
                    response.body()?.let {
                        Toast.makeText(applicationContext, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }else if(response!!.code() == 409){
                    Toast.makeText(applicationContext,"이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show()
                }else if(response!!.code() == 400){
                    Toast.makeText(applicationContext,"Server Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUp>?, t: Throwable?) {
                Log.e("retrofit Error", t!!.message)
            }
        })
    }

    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String {
        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            return contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        if(uri != null) {
            gallery()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        Toast.makeText(this,"권한이 없습니다",Toast.LENGTH_SHORT).show()
    }

    private fun getOutputMediaFileUri(): Uri? {
        // check for external storage
        // Create an image file nameFile imagePath = new File(Context.getFilesDir(), "images");
        var imagePath : String = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imagePath, ".jpg", storageDir)
        path = image.absolutePath
        return FileProvider.getUriForFile(this, "com.mydomain.fileprovider", image)
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }
}
