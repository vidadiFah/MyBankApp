package com.example.mybankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybankapp.data.model.Account
import com.example.mybankapp.databinding.ActivityMainBinding
import com.example.mybankapp.databinding.DialogAddBinding
import com.example.mybankapp.ui.viewModel.AccountViewModel
import com.example.mybankapp.ui.adapter.AccountsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountsAdapter
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        subscribeToLiveData()
        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeToLiveData(){
        viewModel.accounts.observe(this) {
            adapter.submitList(it)
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isChecked ->
                viewModel.updateAccountPartially(id, isChecked)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun showAddDialog(){
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Добавление нового счета")
                .setView(binding.root)
                .setPositiveButton("Добавить"){ _,_ ->
                    val account = Account (
                        name = etName.text.toString(),
                        balance = etBalance.text.toString().toInt(),
                        currency = etCurrency.text.toString()
                    )
                    viewModel.addAccount(account)
                }.show()
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

                AlertDialog.Builder(this@MainActivity)
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