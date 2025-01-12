package com.example.mypersonalassistant.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.navigation.NotesNav
import com.example.mypersonalassistant.navigation.TaskListsNav

@Composable
fun DrawerContent(
    homeViewModel: HomeViewModel,
    navController: NavController,
    signOut: () -> Unit,
    closeDrawer: () -> Unit
) {
    val currentUser by homeViewModel.currentUser.collectAsStateWithLifecycle()

    ModalDrawerSheet {
        currentUser?.let {
            Text(text = it.displayValue, modifier = Modifier.padding(16.dp))
            HorizontalDivider()
        }
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.home_drawer_notes_button)) },
            selected = false,
            onClick = {
                navController.navigate(NotesNav)
                closeDrawer()
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.home_drawer_task_lists_button)) },
            selected = false,
            onClick = {
                navController.navigate(TaskListsNav)
                closeDrawer()
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.home_drawer_log_out_button)) },
            selected = false,
            onClick = signOut
        )
    }
}