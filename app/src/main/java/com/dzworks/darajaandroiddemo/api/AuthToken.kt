package com.dzworks.darajaandroiddemo.api

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("expires_in") val expires_in: String
)