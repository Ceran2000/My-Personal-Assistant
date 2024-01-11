package com.example.mypersonalassistant.ui.util

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.mypersonalassistant.MpaApplication

fun AndroidViewModel.showToast(text: String) {
    val context = getApplication<MpaApplication>()
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}