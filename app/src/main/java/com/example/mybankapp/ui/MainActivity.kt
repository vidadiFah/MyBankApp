package com.example.mybankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybankapp.R
import com.example.mybankapp.data.model.Account
import com.example.mybankapp.databinding.ActivityMainBinding
import com.example.mybankapp.databinding.DialogAddBinding
import com.example.mybankapp.domain.presenter.AccountContracts
import com.example.mybankapp.domain.presenter.AccountPresenter
import com.example.mybankapp.ui.adapter.AccountsAdapter

class MainActivity : AppCompatActivity(), AccountContracts.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountsAdapter
    private lateinit var presenter: AccountPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        presenter = AccountPresenter(this)

        binding.btnAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
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
                    presenter.addAccount(account)
                }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isChecked ->
                presenter.updateAccountPartially(id, isChecked)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    override fun showAccounts(accountList: List<Account>) {
        adapter.submitList(accountList)
    }

    private fun showDeleteDialog(id: String){
        AlertDialog.Builder(this)
            .setTitle("Вы уверены?")
            .setMessage("Удалить счет с индексом - $id?")
            .setPositiveButton("Удалить") { _, _ ->
                presenter.deleteAccount(id)
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

                        presenter.updateAccountFully(updatedAccount)
                    }.show()
            }
        }
    }
}