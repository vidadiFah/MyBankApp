package com.example.mybankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mybankapp.data.model.Account
import com.example.mybankapp.databinding.ActivityAccountDetailsBinding
import com.example.mybankapp.databinding.DialogAddBinding
import com.example.mybankapp.di.AccountDetailsViewModel
import com.example.mybankapp.ui.adapter.AccountsAdapter
import com.example.mybankapp.ui.viewModel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import kotlin.toString

@AndroidEntryPoint
class AccountDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountDetailsBinding
    private val viewModel: AccountDetailsViewModel by viewModels()

    private lateinit var accountId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountId = intent.getStringExtra("account_id").toString()


        binding.btnEdit.setOnClickListener {
            viewModel.account.value?.let {
                showEditDialog(it)
            }
        }
        binding.btnDelete.setOnClickListener {
            showDeleteDialog(accountId)
        }

        subscribeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAccountByID(accountId)
    }


    private fun subscribeToLiveData(){
        viewModel.account.observe(this) {
            binding.tvAccountName.text = it.name
            val text = "${it.balance} ${it.currency}"
            binding.tvAccountBalance.text = text
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeleteDialog(id: String){
        AlertDialog.Builder(this)
            .setTitle("Вы уверены?")
            .setMessage("Удалить счет с индексом - $id?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAccount(id)
            }
            .setNegativeButton("Отмена") {_,_ ->}.show()
    }

    private fun showEditDialog(account: Account){
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding){
            account.run {
                etName.setText(name)
                etBalance.setText(balance.toString())
                etCurrency.setText(currency)

                AlertDialog.Builder(this@AccountDetailsActivity)
                    .setTitle("Изменение счета")
                    .setView(binding.root)
                    .setPositiveButton("Изменить"){ _,_ ->

                        val updatedAccount = account.copy (
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )

                        viewModel.updateAccountFully(updatedAccount)
                    }.show()
            }
        }

    }
}