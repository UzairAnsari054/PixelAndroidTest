package com.uzair.pixel.test.domain.repository

import com.uzair.pixel.test.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUsers(): Flow<Result<List<User>>>
    suspend fun toggleFollow(userId: Int)
}