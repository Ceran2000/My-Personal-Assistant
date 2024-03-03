package com.example.mypersonalassistant.util

import android.util.Log
import com.example.mypersonalassistant.MpaApplication

inline val <reified T> T.TAG: String get() = T::class.java.simpleName

inline fun <reified T> T.logd(message: String) = Log.d(MpaApplication.TAG, "$TAG: $message")