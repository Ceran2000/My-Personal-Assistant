package com.example.mypersonalassistant.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.ui.component.DefaultAppTopBar
import com.example.mypersonalassistant.ui.component.contentDescription
import com.example.mypersonalassistant.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    AppTheme {

        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }

        val notes by notesViewModel.notes.collectAsStateWithLifecycle()
        val showProgressBar by notesViewModel.showProgressBar

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DefaultAppTopBar(
                        title = stringResource(R.string.notes_appbar_title),
                        showProgressBar = showProgressBar,
                        navController = navController
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showBottomSheet = true }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = contentDescription)
                }
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes, key = { it.key }) { note ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1.0f, true)
                            ) {
                                Text(note.title)
                                Text(note.content)
                            }
                            IconButton(onClick = { notesViewModel.removeNote(note) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = contentDescription)
                            }
                        }
                    }
                }
            }
        }
        if (showBottomSheet) {
            BottomSheet(notesViewModel = notesViewModel, sheetState = sheetState) {
                showBottomSheet = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    notesViewModel: NotesViewModel,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    val noteTitle by notesViewModel.noteTitle.collectAsStateWithLifecycle()
    val noteContent by notesViewModel.noteContent.collectAsStateWithLifecycle()
    val addNoteButtonEnabled by notesViewModel.addNoteButtonEnabled.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = noteTitle,
                onValueChange = notesViewModel::onTitleChanged
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = noteContent,
                onValueChange = notesViewModel::onContentChanged
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                enabled = addNoteButtonEnabled,
                onClick = notesViewModel::onAddNoteClicked
            ) {
                Text(stringResource(R.string.notes_add_button))
            }
        }
    }
}