package com.example.mypersonalassistant.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mypersonalassistant.ui.MpaApp
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyPersonalAssistantTheme {
                MpaApp()
            }
        }
    }
}