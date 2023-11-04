package com.example.mypersonalassistant.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MpaNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "screen1", modifier = modifier) {
        composable("screen1") {
            Box(Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "Screen1"
                    )
                    Button(onClick = { navController.navigate("screen2") }) {
                        Text("Go to Screen2")
                    }
                }
            }
        }
        composable("screen2") {
            Box(Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "Screen2"
                    )
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Go back")
                    }
                }
            }
        }
    }
}