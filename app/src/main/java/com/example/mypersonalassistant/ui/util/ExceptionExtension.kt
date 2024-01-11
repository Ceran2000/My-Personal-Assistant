package com.example.mypersonalassistant.ui.util

import com.example.mypersonalassistant.R
import java.util.concurrent.CancellationException

fun Exception.toLocalizedException(): Exception = when (this) {
    is CancellationException -> Exception()
    else -> Exception(StringProvider.getString(R.string.common_unknown_error))
}