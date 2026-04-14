package com.uzair.pixel.test.domain.usecase

import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FetchUsersUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> {
        return userRepository.observeUsers()
    }
}