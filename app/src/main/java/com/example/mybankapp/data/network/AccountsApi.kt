package com.example.mybankapp.data.network

import com.example.mybankapp.data.model.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountsApi {

    @GET("accounts")
    fun getAccounts(): Call<List<Account>>

    @POST("accounts")
    fun addAccount(@Body account: Account): Call<Unit>

}