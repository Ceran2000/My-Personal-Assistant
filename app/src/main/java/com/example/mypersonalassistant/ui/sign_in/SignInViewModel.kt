package com.example.mypersonalassistant.ui.sign_in

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    suspend fun signIn() = authManager.signIn()

    suspend fun signInWithIntent(intent: Intent) = authManager.signInWithIntent(intent)

}