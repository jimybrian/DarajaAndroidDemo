package com.dzworks.darajaandroiddemo.api

import com.google.gson.annotations.SerializedName

data class STKPushPayload(
    @SerializedName("AccountReference") val AccountReference: String,
    @SerializedName("Amount") val Amount: String,
    @SerializedName("BusinessShortCode") val BusinessShortCode: String,
    @SerializedName("CallBackURL") val CallBackURL: String,
    @SerializedName("PartyA") val PartyA: String,
    @SerializedName("PartyB") val PartyB: String,
    @SerializedName("Password") val Password: String,
    @SerializedName("PhoneNumber") val PhoneNumber: String,
    @SerializedName("Timestamp") val Timestamp: String,
    @SerializedName("TransactionDesc") val TransactionDesc: String,
    @SerializedName("TransactionType") val TransactionType: String
)