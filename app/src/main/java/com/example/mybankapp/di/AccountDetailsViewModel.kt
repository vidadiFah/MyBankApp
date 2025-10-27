package com.example.mybankapp.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybankapp.data.model.Account
import com.example.mybankapp.data.network.AccountDetailsApi
import com.example.mybankapp.data.network.AccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val accountDetailsApi: AccountDetailsApi
) : ViewModel() {
    private val _account = MutableLiveData<Account>()
    var account: LiveData<Account> = _account

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getAccountByID(id : String) {
        accountDetailsApi.getAccountByID(id).handleAccountResponse(
            onSuccess = { _account.value = it },
            onError = { _error.value = it }
        )
    }

    fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let { id ->
            accountDetailsApi.updateAccountFully(id, updatedAccount).handleAccountResponse(
                onSuccess = { getAccountByID(id)},
                onError = { _error.value = it }
            )
        }
    }

    fun deleteAccount(id: String) {
        accountDetailsApi.deleteAccount(id).handleAccountResponse(
            onError = { _error.value = it }
        )
    }

    private fun <T> Call<T>.handleAccountResponse(
        onSuccess: (T) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val result = response.body()
                if (result != null && response.isSuccessful) {
                    onSuccess(result)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                onError(t.message.toString())
            }
        })
    }
}