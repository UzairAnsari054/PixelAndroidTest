package com.uzair.pixel.test.presentation

import com.uzair.pixel.test.presentation.model.UserUiModel
import com.uzair.pixel.test.presentation.util.UiMessage


sealed interface UserListUiState {
    object Loading : UserListUiState
    data class Success(val topUsers: List<UserUiModel>) : UserListUiState
    data class Error(val message: UiMessage) : UserListUiState
}