package com.example.mypersonalassistant.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.theme.AppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
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

    AppTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(homeViewModel, navController, homeViewModel::signOut, closeDrawer)
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(stringResource(R.string.home_appbar_title)) },
                        navigationIcon = {
                            IconButton(onClick = openDrawer) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = contentDescription)
                            }
                        }
                    )
                }
            ) { padding ->
                HomeScreenContent(
                    modifier = Modifier.padding(padding),
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
) {
    val greetingMessage by homeViewModel.greetingMessage.collectAsStateWithLifecycle()
    val latestNote by homeViewModel.latestNote.collectAsStateWithLifecycle()
    val latestTaskList by homeViewModel.latestTaskList.collectAsStateWithLifecycle()

    LazyColumn(modifier = modifier) {
        item("greeting") {
            Text(
                text = greetingMessage,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            Text(
                text = "Dobrze, że jesteś!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }

        item("latest_note") {
            Headline("Ostatnia notatka")
            latestNote.Component()
        }

        item("latest_task_list") {
            //TODO: pokazuje najstarszą a nie najnowszą notatkę
            Headline("Ostatnia lista zadań")
            latestTaskList.Component()
        }
    }
}

@Composable
private fun Headline(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
    )
}