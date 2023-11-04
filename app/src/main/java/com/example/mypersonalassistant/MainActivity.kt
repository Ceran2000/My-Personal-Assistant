package com.example.mypersonalassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mypersonalassistant.ui.MpaApp
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme
import com.google.android.gms.auth.api.identity.SignInClient

class MainActivity : ComponentActivity() {

    private lateinit var oneTapClient: SignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MyPersonalAssistantTheme {
                MpaApp()
            }
        }
    }
}