package com.uzair.pixel.test.presentation.mapper

import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.presentation.model.UserUiModel

fun User.toUserUi(): UserUiModel {
    return UserUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl,
        reputation = reputation,
        isFollowed = isFollowed
    )
}
