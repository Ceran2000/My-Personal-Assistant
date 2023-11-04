package com.example.mypersonalassistant.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mypersonalassistant.navigation.MpaNavHost

@Composable
fun MpaApp() {
    Scaffold { padding ->
        MpaNavHost(modifier = Modifier.fillMaxSize().padding(padding))
    }
}