package com.example.mybankapp.data.model

import android.icu.util.Currency

data class Account(
    val id: String? = null,
    val name: String? = null,
    val currency: String? = null,
    val balance: Int? = null,
    val isActive: Boolean? = null,
)
