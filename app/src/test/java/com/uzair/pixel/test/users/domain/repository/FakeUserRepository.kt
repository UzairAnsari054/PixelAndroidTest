package com.uzair.pixel.test.users.domain.repository

import com.uzair.pixel.test.users.domain.model.User

class FakeUserRepository(
    private val result: Result<List<User>>
) : UserRepository {

    override suspend fun getTopStackOverUsers(): Result<List<User>> {
        return result
    }
}