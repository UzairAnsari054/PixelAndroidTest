package com.uzair.pixel.test.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzair.pixel.test.domain.usecase.FetchUsersUseCase
import com.uzair.pixel.test.domain.usecase.ToggleFollowUseCase
import com.uzair.pixel.test.presentation.mapper.toUserUi
import com.uzair.pixel.test.util.toErrorType
import com.uzair.pixel.test.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.map

class UserListViewModel(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val toggleFollowUseCase: ToggleFollowUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState

    init {
        observeUsers()
    }

    fun onAction(action: UserListAction) {
        when (action) {
            is UserListAction.OnToggleFollow -> toggleFollow(userId = action.user.id)
            UserListAction.OnRetryClick -> observeUsers()
        }
    }

    private fun observeUsers() {
        viewModelScope.launch {
            fetchUsersUseCase.invoke().collect { result ->
                result.fold(
                    onSuccess = { users ->
                        _uiState.value = UserListUiState.Success(
                            topUsers = users.map { it.toUserUi() }
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = mapError(error)
                    }
                )
            }
        }
    }

    private fun toggleFollow(userId: Int) {
        viewModelScope.launch {
            toggleFollowUseCase.invoke(params = ToggleFollowUseCase.Params(userId))
        }
    }

    private fun mapError(error: Throwable): UserListUiState.Error {
        return UserListUiState.Error(
            message = error.toErrorType().toUiText()
        )
    }
}