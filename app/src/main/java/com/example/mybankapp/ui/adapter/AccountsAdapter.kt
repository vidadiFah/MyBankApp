package com.example.mybankapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mybankapp.data.model.Account
import com.example.mybankapp.databinding.ItemAccountBinding


class AccountsAdapter(
    val onSwitchToggle: (String, Boolean) -> Unit,
    val onClick: (String) -> Unit
) : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    private val items = mutableListOf<Account>()

    fun submitList(data: List<Account>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) = with(binding) {
            tvName.text = account.name
            val text = "${account.balance} ${account.currency}"
            tvBalance.text = text

            switcher.isChecked = account.isActive == true
            switcher.setOnCheckedChangeListener { _, isChecked ->
                account.id?.let{
                    onSwitchToggle(it, isChecked)
                }
            }

            itemView.setOnClickListener {
                account.id?.let {
                    onClick(it)
                }
            }
        }
    }
}