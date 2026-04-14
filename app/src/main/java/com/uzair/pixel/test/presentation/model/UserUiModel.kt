package com.uzair.pixel.test.presentation.model

import com.uzair.pixel.test.util.ReputationCountFormatter

data class UserUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val reputation: Int,
    val isFollowed: Boolean,
) {
    val reputationDisplayFormat: String =
        ReputationCountFormatter.format(reputation)
}
