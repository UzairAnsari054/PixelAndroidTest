package com.uzair.pixel.test.data.remote.api

import com.uzair.pixel.test.domain.model.User

interface UserApi {
    suspend fun fetch(): Result<List<User>>
}
