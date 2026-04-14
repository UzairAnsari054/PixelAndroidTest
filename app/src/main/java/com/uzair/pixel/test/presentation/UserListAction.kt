package com.uzair.pixel.test.presentation

import com.uzair.pixel.test.presentation.model.UserUiModel

sealed interface UserListAction {
    class OnToggleFollow(val user: UserUiModel) : UserListAction
    data object OnRetryClick : UserListAction
}