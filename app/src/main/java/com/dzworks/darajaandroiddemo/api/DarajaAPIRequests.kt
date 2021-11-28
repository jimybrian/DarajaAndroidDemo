package com.dzworks.darajaandroiddemo.api

import android.util.Base64.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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
    val consumerKey = "nRI1MdnQ55qzB4cjLO3K0epbSF140F0C"
    val consumerSecret = "Z2GmqyF0warAYtGI"

    val appKeySecret = "";
    val auth = "";
    val shortCode = "174379";
    val lipaPasSKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

    //Sandbox Creds
    val initiatorSecurityCred = "RI6yzL99wVYbN8nOZfKtzJAFlU7hUScpEMWGJ1cPaCqaAqv90ayl+aCTddGpC3t19KnWGuNZ+XsBFC7Bie0iMt5/JL/nWxi0EULJcoi6DGJX2CajdI1njyN5FvfxX7arkv7kmSTf2r2WMQlByMQ7UXhtiw5Ke5Zeo8kinPwQs82Pmq8c2B2WQXz7BAT0FivND26EWOoxyYnQCzZDxzVvJFm6KpZRHwUItkQBGvqH/repnrfFfdTz6mxJlSatrYy/aeioqxQr9cSLFN2wxA52n+4ULkrA1HfyM89EP3pYYzuGanAJrOkmwTU5JWXz3oK0NmoxY0i2ENUr6JosOCcozw==";
    val initiatorName = "apitest443";
    val secCred = "443reset";

    fun getCurrentTimeStamp(): String? {
        return try {
            val dateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            val currentTime = Calendar.getInstance().time
            dateFormat.format(currentTime)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSTKPassword(timeStamp: String) : String{
        val password:String = "$shortCode$lipaPasSKey$timeStamp}"
        return encodeToString(password.toByteArray(charset("UTF-8")), NO_WRAP)
    }

    fun getAuthKey(): String{
        val authKey = "$consumerKey:$consumerSecret"
        val encodedAuthKey = encodeToString(authKey.toByteArray(charset("ISO-8859-1")), NO_WRAP)
        return "Basic $encodedAuthKey"
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