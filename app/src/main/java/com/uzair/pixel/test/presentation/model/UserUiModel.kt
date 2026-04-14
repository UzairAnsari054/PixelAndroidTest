package com.uzair.pixel.test.presentation.model

import androidx.compose.runtime.Stable
import java.util.Locale

@Stable
data class UserUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val reputation: Int,
    val isFollowed: Boolean,
) {
    val reputationDisplayFormat: String = when {
        reputation >= 1_000_000 -> String.format(Locale.US, "%.1fM", reputation / 1_000_000.0)
        reputation >= 1_000 -> String.format(Locale.US, "%.1fK", reputation / 1_000.0)
        else -> reputation.toString()
    }
}
