package com.example.mybankapp.domain.presenter

import com.example.mybankapp.data.model.Account
import com.example.mybankapp.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPresenter(private val view: AccountContracts.View): AccountContracts.Presenter {
    override fun loadAccounts() {
        ApiClient.accountsApi.getAccounts().enqueue(object : Callback<List<Account>>{
            override fun onResponse(call: Call<List<Account>?>, response: Response<List<Account>?>) {
                if (response.isSuccessful) view.showAccounts(response.body() ?: emptyList())
            }

            override fun onFailure(call: Call<List<Account>?>, t: Throwable) {}
        })
    }

    override fun addAccount(account: Account) {
        ApiClient.accountsApi.addAccount(account).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful) loadAccounts()
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
            }
        })
    }
}