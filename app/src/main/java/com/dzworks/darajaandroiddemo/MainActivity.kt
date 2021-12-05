package com.dzworks.darajaandroiddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dzworks.darajaandroiddemo.api.AuthToken
import com.dzworks.darajaandroiddemo.api.DarajaInfo
import com.dzworks.darajaandroiddemo.api.RetrofitClient
import com.dzworks.darajaandroiddemo.api.STKPushPayload
import com.dzworks.darajaandroiddemo.databinding.ActivityMainBinding
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var authTokenCall: retrofit2.Call<AuthToken>
    lateinit var stkPushCall: retrofit2.Call<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        binding.btnSendRequest.setOnClickListener {
            if(binding.etAmount.text.isNotEmpty() && binding.etPhoneNumber.text.isNotEmpty())
                getAuthToken()
        }
    }

    fun getAuthToken(){
        val authTokenCall = RetrofitClient().getRetrofitInstance()
            ?.getDarajaApis()?.getAuthToken(DarajaInfo.getAuthKey(),"client_credentials")

        authTokenCall?.enqueue(object : Callback<AuthToken>{
            override fun onResponse(
                call: retrofit2.Call<AuthToken>,
                response: Response<AuthToken>
            ) {
                if(response.isSuccessful){
                    val token = "Bearer ${response.body()?.access_token}"
                    initiateSTKPush(token)
                }
            }

            override fun onFailure(call: retrofit2.Call<AuthToken>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Cannot get auth token at the moment", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun initiateSTKPush(authToken: String){
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val amount = binding.etAmount.text.toString()
        val timeStamp = DarajaInfo.getCurrentTimeStamp().orEmpty()

        val payload = STKPushPayload(
            AccountReference = "STK_PUSH_DEMO",
            Amount = amount,
            BusinessShortCode = DarajaInfo.shortCode,
            CallBackURL = "https://example.com/anydomain",
            PartyA = phoneNumber,
            PartyB = DarajaInfo.shortCode,
            Password = DarajaInfo.getSTKPassword(timeStamp),
            PhoneNumber = phoneNumber,
            Timestamp = timeStamp,
            TransactionDesc = "STK PUSH DEMO",
            TransactionType = "CustomerPayBillOnline"
        )

        val stkPushCall = RetrofitClient().getRetrofitInstance()?.getDarajaApis()?.stkPush(authToken, payload)
        stkPushCall?.enqueue(object : Callback<Any>{
            override fun onResponse(call: retrofit2.Call<Any>, response: Response<Any>) {
                Toast.makeText(this@MainActivity, "STK Push initiated successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Cannot Initiate STK PUsh at the moment", Toast.LENGTH_SHORT).show()
            }

        })
    }

}