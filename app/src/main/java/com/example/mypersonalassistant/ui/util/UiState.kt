package com.example.mypersonalassistant.ui.util

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
    data class ShowContent<T>(val data: T) : UiState<T>()
}
