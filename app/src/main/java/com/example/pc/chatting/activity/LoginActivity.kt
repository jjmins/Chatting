package com.example.pc.chatting.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pc.chatting.DB
import com.example.pc.chatting.R
import com.example.pc.chatting.Signin
import com.example.pc.chatting.Token
import com.example.pc.chatting.util.RetrofitServer
import com.example.pc.chatting.util.RetrofitUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_resiger.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    var id : String = ""
    var passwd : String = ""
    var token : Token? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginId.hint = "Email"
        loginPassword.hint = "Password"
        loginBtn.setOnClickListener {
            id = loginId.text.toString()
            passwd = loginPassword.text.toString()
            if (id.isEmpty()) {
                loginId.error = "아이디를 입력해주세요"
                //Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                loginId.requestFocus()
            }else if(passwd.isEmpty()){
                loginPassword.error = "비밀번호를 입력해주세요"
                //Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                loginPassword.requestFocus()
            } else {
                Log.e("post", "post")
                signIn()
            }
        }

        signUpBtn.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val content = SpannableString("Sing Up")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        signUpBtn.text = content
        //글꼴 변경
    }

//    fun DB(){
//        val postService = RetrofitUtil.retrofit!!.create(RetrofitServer::class.java)
//        val res : Call<DB> = postService.DB()
//        res.enqueue(object : retrofit2.Callback<DB> {
//            override fun onResponse(call: Call<DB>?, response: Response<DB>?) {
//                Log.d("Retrofit", response!!.code().toString())
//
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        Log.e("respones", response.message())
//                    }
//                }else{
//                    Log.e("respones", response.message())
//                }}
//
//            override fun onFailure(call: Call<DB>?, t: Throwable?) {
//            Log.e("retrofit Error", t!!.message)
//            }
//        })
//    }
    fun tokenservice(){
        val postService = RetrofitUtil.retrofit!!.create(RetrofitServer::class.java)
        val res : Call<List<Token>> = postService.Token()
        res.enqueue(object  : retrofit2.Callback<List<Token>> {
            override fun onResponse(call: Call<List<Token>>?, response: Response<List<Token>>?) {
                if(response!!.code() == 200){
                    response.body()?.let {
                        Log.e("token","토큰생성 완료")
                    }
                }else{

                    Log.e("token","토큰생성 비완료")
                }
            }

            override fun onFailure(call: Call<List<Token>>?, t: Throwable?) {
                Log.e("retrofit Error", t!!.message)
            }
        })
    }

    fun signIn(){

        var intent = Intent(this, MainActivity::class.java)
        val postService = RetrofitUtil.retrofit!!.create(RetrofitServer::class.java)
        val res : Call<Signin> = postService.SignIn(loginId.text,loginPassword.text)
        Log.e("asdf", res.request().toString())
        res.enqueue(object : retrofit2.Callback<Signin> {
            override fun onResponse(call: Call<Signin>?, response: Response<Signin>?) {
                Log.d("Retrofit", response!!.code().toString())

                if(response!!.code() == 200){
                    response.body()?.let {
                            tokenservice()
                                Toast.makeText(applicationContext,"로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                    }
                }else{
                    loginId.error = "다시 입력해 주세요"
                    loginPassword.error = "다시 입력해 주세요"
                    Toast.makeText(applicationContext,"아이디나 비밀번호를 다시 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Signin>?, t: Throwable?) {
                Log.e("retrofit Error", t!!.message)
                Toast.makeText(applicationContext,"Sever Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
