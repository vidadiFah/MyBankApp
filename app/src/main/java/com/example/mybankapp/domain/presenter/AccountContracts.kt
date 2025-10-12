package com.example.mybankapp.domain.presenter

import com.example.mybankapp.data.model.Account

interface AccountContracts {
    interface View{
        fun showAccounts(accountList: List<Account>)
    }

    interface Presenter{
        fun loadAccounts()
        fun addAccount(account: Account)
        fun updateAccountFully(updatedAccount: Account)
        fun updateAccountPartially(id: String, isChecked: Boolean)

        fun deleteAccount(id: String)
    }
}