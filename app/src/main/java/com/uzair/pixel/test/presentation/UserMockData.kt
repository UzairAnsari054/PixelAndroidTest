package com.uzair.pixel.test.presentation

import com.uzair.pixel.test.presentation.model.UserUiModel

object UserMockData {

    val users = listOf(
        UserUiModel(
            id = 1,
            name = "Uzair Ansari",
            imageUrl = "",
            reputation = 1200,
            isFollowed = false
        ),
        UserUiModel(
            id = 2,
            name = "John Doe",
            imageUrl = "",
            reputation = 45000,
            isFollowed = true
        ),
        UserUiModel(
            id = 3,
            name = "Jane Smith",
            imageUrl = "",
            reputation = 987000,
            isFollowed = false
        )
    )
}