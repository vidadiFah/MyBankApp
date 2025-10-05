package com.example.mybankapp.domain.presenter

import com.example.mybankapp.data.model.Account

class AccountPresenter(private val view: AccountContracts.View): _root_ide_package_.com.example.mybankapp.domain.presenter.AccountContracts.Presenter {
    override fun loadAccounts() {
        val testMockList = arrayListOf<Account>()
        testMockList.add(Account(
            name = "o!Bank",
            balance = 1000,
            currency = "KGS"
        ))
        testMockList.add(Account(
            name = "mBank",
            balance = 100,
            currency = "USD"
        ))
        testMockList.add(Account(
            name = "Optima",
            balance = 10,
            currency = "EUR"
        ))
        view.showAccounts(testMockList)
    }
}