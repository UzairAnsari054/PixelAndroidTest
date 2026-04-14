package com.uzair.pixel.test.util

import com.uzair.pixel.test.R
import org.json.JSONException
import java.io.IOException
import java.net.SocketTimeoutException

sealed class ErrorType {
    data class GenericError(val error: Throwable) : ErrorType()
    data class NetworkError(val error: Throwable) : ErrorType()
    data class ApiError(val error: Throwable) : ErrorType()
}

fun Throwable.toErrorType(): ErrorType {
    return when (this) {
        is SocketTimeoutException -> ErrorType.NetworkError(error = this)
        is IOException -> ErrorType.NetworkError(error = this)
        is JSONException -> ErrorType.ApiError(error = this)
        else -> ErrorType.GenericError(error = this)
    }
}

fun ErrorType.toUiText(): UiMessage {
    val stringRes = when (this) {
        is ErrorType.GenericError -> R.string.error_unknown
        is ErrorType.NetworkError -> R.string.error_no_internet
        is ErrorType.ApiError -> R.string.error_request_timeout
    }

    return UiMessage.StringResource(stringRes)
}
