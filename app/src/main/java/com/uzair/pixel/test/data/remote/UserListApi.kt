package com.uzair.pixel.test.data.remote

import com.uzair.pixel.test.domain.model.User

interface UserListApi {
    suspend fun fetch(): Result<List<User>>
}
