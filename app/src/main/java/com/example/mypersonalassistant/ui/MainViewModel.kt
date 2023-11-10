package com.example.mypersonalassistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    val isSignedIn = authManager.isUserSignedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
        }
    }

}