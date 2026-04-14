package com.uzair.pixel.test.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Immutable
sealed interface UiMessage {
    data class DynamicString(val value: String) : UiMessage

    class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UiMessage

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}