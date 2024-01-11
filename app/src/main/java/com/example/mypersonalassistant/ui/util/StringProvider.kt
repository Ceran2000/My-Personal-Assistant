package com.example.mypersonalassistant.ui.util

import androidx.annotation.StringRes
import com.example.mypersonalassistant.MpaApplication

object StringProvider {
    fun getString(@StringRes resId: Int): String = MpaApplication.resources.getString(resId)
}