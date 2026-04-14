package com.uzair.pixel.test.data.remote.model

import com.uzair.pixel.test.domain.model.User

data class UserNetworkModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val reputation: Int,
)

fun UserNetworkModel.toUser(): User {
    return User(
        id = id,
        name = name,
        imageUrl = imageUrl,
        reputation = reputation,
    )
}
