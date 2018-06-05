package com.example.pc.chatting.util

import android.text.Editable
import com.example.pc.chatting.SignUp
import com.example.pc.chatting.Signin
import com.example.pc.chatting.data.Token
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Multipart



/**
 * Created by pc on 2018-02-13.
 */
interface RetrofitServer {
    // iwin247.info:3000
    @FormUrlEncoded
    @POST("/signin")
    fun SignIn(@Field("email") email: Editable,
               @Field("passwd") passwd: Editable) : Call<Signin>

    @Multipart
    @POST("/signup")
    fun SignUp(@Part("email") email: Editable,
               @Part("passwd") passwd: Editable,
               @Part("name") name: Editable,
               @Part("phone") phone: Editable,
               @Part img : MultipartBody.Part) : Call<SignUp>


    @GET("/auto/{token}")
    fun Token(@Path("token") token : String) : Call<Token>
}