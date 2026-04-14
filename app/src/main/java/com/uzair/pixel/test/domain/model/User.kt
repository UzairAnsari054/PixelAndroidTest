package com.uzair.pixel.test.domain.model

data class User(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val reputation: Int,
    val isFollowed: Boolean = false,
)