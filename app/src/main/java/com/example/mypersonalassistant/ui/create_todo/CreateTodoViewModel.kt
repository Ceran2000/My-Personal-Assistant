package com.example.mypersonalassistant.ui.create_todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

}