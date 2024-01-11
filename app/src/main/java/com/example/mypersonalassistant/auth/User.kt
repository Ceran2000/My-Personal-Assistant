package com.example.mypersonalassistant.auth

import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.ui.util.StringProvider

data class User(val userId: String, val name: String?, val email: String?, val imageUrl: String?) {
    val displayValue = name ?: email ?: StringProvider.getString(R.string.common_user_anonymous)
}