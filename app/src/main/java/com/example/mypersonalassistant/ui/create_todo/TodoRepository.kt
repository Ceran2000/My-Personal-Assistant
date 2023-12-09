package com.example.mypersonalassistant.ui.create_todo

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val database: FirebaseFirestore
) {
    
}