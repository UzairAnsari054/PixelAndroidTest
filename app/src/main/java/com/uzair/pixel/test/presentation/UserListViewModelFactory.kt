package com.uzair.pixel.test.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzair.pixel.test.domain.repository.UserRepository
import com.uzair.pixel.test.domain.usecase.FetchUsersUseCase
import com.uzair.pixel.test.domain.usecase.ToggleFollowUseCase

class UserListViewModelFactory(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val toggleFollowUseCase: ToggleFollowUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            return UserListViewModel(
                fetchUsersUseCase = fetchUsersUseCase,
                toggleFollowUseCase = toggleFollowUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}