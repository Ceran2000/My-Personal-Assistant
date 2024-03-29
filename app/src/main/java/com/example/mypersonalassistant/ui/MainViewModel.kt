package com.example.mypersonalassistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    val redirectToMainScreen = authManager.isUserSignedIn
        .filter { it }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val redirectToSignInScreen = authManager.isUserSignedIn
        .filter { !it }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

}