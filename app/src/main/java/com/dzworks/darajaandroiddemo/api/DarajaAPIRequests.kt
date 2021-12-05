package com.dzworks.darajaandroiddemo.api

import android.util.Base64.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


interface DarajaAPIRequests {
    @GET("/oauth/v1/generate")
    fun getAuthToken(
        @Header("authorization") token: String,
        @Query("grant_type") grantType: String
    ): Call<AuthToken>

    @POST("/mpesa/stkpush/v1/processrequest")
    fun stkPush(
        @Header("Authorization") authToken: String,
        @Body payload: STKPushPayload
    ): Call<Any>
}

object DarajaInfo {
//    val consumerKey = "nRI1MdnQ55qzB4cjLO3K0epbSF140F0C"
//    val consumerSecret = "Z2GmqyF0warAYtGI"

    val consumerKey = "AQo4rHgxO9bvP4lb4As7g0t6Ebngd7ms";
    val consumerSecret = "b5Szox0TC6qj8Pdb";

    val shortCode = "174379"
    val lipaPasSKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"

    fun getCurrentTimeStamp(): String? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH)
                .format(Calendar.getInstance().time)
            Timber.d("Timestamp $timeStamp")
            timeStamp
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSTKPassword(timeStamp: String) : String{
        val password:String = shortCode + lipaPasSKey + timeStamp
        return b64String(password)
    }

    fun getAuthKey(): String{
        val authKey = "$consumerKey:$consumerSecret"
        return "Basic ${b64String(authKey)}"
    }

    fun b64String(s:String):String{
        val bytes = s.toByteArray(charset("ISO-8859-1"))
        val auth = Base64.getEncoder().encodeToString(bytes);

        return  auth
    }
}

class RetrofitClient {
    val baseUrl = "https://sandbox.safaricom.co.ke"

    var instance: RetrofitClient? = null
    var darajaApi: DarajaAPIRequests? = null

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        darajaApi = retrofit.create(DarajaAPIRequests::class.java)
    }

    @Synchronized
    fun getRetrofitInstance(): RetrofitClient? {
        if (instance == null) {
            instance = RetrofitClient()
        }
        return instance
    }

    fun getDarajaApis(): DarajaAPIRequests? {
        return darajaApi
    }
}