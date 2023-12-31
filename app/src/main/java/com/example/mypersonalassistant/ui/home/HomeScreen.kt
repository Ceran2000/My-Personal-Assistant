package com.example.mypersonalassistant.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.navigation.todosNavigationRoute
import com.example.mypersonalassistant.ui.MainViewModel
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.notes.notesNavigationRoute
import com.example.mypersonalassistant.ui.theme.MyPersonalAssistantTheme
import com.example.mypersonalassistant.ui.todos.todosListNavigationRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }
    val closeDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }
    val currentUser by homeViewModel.currentUser.collectAsStateWithLifecycle()

    MyPersonalAssistantTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(text = currentUser?.email ?: "Brak", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Notatki") },
                        selected = false,
                        onClick = {
                            navController.navigate(notesNavigationRoute)
                            closeDrawer()
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Lista zadań") },
                        selected = false,
                        onClick = {
                            navController.navigate(todosNavigationRoute)
                            closeDrawer()
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Wyloguj") },
                        selected = false,
                        onClick = mainViewModel::signOut
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Home") },
                        navigationIcon = {
                            IconButton(onClick = { openDrawer() }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = contentDescription)
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {

                }
            }
        }
    }
}