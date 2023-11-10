package com.example.mypersonalassistant.ui.sign_in

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    val isSignedIn = authManager.isUserSignedIn.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    suspend fun signIn() = authManager.signIn()

    suspend fun signInWithIntent(intent: Intent) {
        val result = authManager.signInWithIntent(intent)
        Log.d("MyPersonalAssistant", "### $result")
    }

}