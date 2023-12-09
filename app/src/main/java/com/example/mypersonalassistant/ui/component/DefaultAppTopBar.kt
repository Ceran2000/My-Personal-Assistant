package com.example.mypersonalassistant.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppTopBar(
    title: String,
    navController: NavController,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val canNavigateBack = navController.previousBackStackEntry != null
    val onNavigateBackClick: () -> Unit = { navController.navigateUp() }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = actions,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = contentDescription
                    )
                }
            }
        }
    )
}

@Composable
fun TopBarTextButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = text.uppercase(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}