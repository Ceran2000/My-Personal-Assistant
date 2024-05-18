package com.example.mypersonalassistant.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mypersonalassistant.data.model.Note
import com.example.mypersonalassistant.data.remote.model.RemoteNote

@Composable
fun LatestNoteItem(note: Note) {
    //TODO: show note button
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LatestNoteEmptyState() {
    //TODO: add note button
   HomeItemEmptyState(message = "Nie masz żadnych notatek.")
}