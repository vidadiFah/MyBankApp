package com.example.mybankapp.data.network

import com.example.mybankapp.data.model.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountDetailsApi {

    @GET("accounts/{id}")
    fun getAccountByID(
        @Path("id") id: String
    ): Call<Account>

    @PUT("accounts/{id}")
    fun updateAccountFully(
        @Path("id") id: String,
        @Body updatedAccount: Account
    ): Call<Unit>

    @DELETE("accounts/{id}")
    fun deleteAccount(
        @Path("id") id: String
    ): Call<Unit>
}