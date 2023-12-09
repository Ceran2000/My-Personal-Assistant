package com.example.mypersonalassistant.ui.sign_in

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mypersonalassistant.navigation.mainNavigationRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavController,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val isSignedIn by signInViewModel.isSignedIn.collectAsState(false)
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                scope.launch {
                    if (result.data == null) return@launch
                    signInViewModel.signInWithIntent(result.data!!)
                }
            }
        }
    )

    val onButtonClick: () -> Unit = {
        scope.launch {
            val signInIntentSender = signInViewModel.signIn() ?: return@launch
            launcher.launch(IntentSenderRequest.Builder(signInIntentSender).build())
        }
    }

    LaunchedEffect(Unit) {
        signInViewModel.isSignedIn
            .onEach {
                if (it) navController.navigate(mainNavigationRoute)
            }
            .launchIn(this)
    }

    Box(contentAlignment = Alignment.Center) {
        Column {
            Text("Is signed? $isSignedIn")
            Button(onClick = onButtonClick) {
                Text("Zaloguj")
            }
        }
    }
}