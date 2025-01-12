package com.example.mypersonalassistant.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mypersonalassistant.navigation.Main
import com.example.mypersonalassistant.navigation.MpaNavHost
import com.example.mypersonalassistant.ui.sign_in.SignIn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MpaApp(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        mainViewModel.redirectToMainScreen
            .onEach { navController.redirectToMainScreen() }
            .launchIn(this)

        mainViewModel.redirectToSignInScreen
            .onEach { navController.redirectToSignInScreen() }
            .launchIn(this)
    }

    Scaffold { padding ->
        MpaNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            navController = navController
        )
    }
}

private fun NavHostController.redirectToMainScreen() {
    popBackStack()
    navigate(Main)
}

private fun NavHostController.redirectToSignInScreen() {
    popBackStack()
    navigate(SignIn)
}