package com.example.mypersonalassistant.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalassistant.auth.AuthManager
import com.example.mypersonalassistant.auth.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    val currentUser: StateFlow<UserData?> = authManager.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

}