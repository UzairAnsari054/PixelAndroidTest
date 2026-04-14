package com.uzair.pixel.test.domain.usecase

import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class ToggleFollowUseCase(
    private val userRepository: UserRepository
) {
    data class Params(val userId: Int)

    suspend operator fun invoke(params: Params) {
        return userRepository.toggleFollow(userId = params.userId)
    }
}