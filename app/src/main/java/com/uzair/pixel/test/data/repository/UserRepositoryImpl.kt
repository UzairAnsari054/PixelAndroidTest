package com.uzair.pixel.test.data.repository

import com.uzair.pixel.test.data.remote.UserListApi
import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.domain.repository.UserRepository
import com.uzair.pixel.test.data.local.UserPreferences
import com.uzair.pixel.test.util.NetworkConnectivityMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.io.IOException

class UserRepositoryImpl(
    private val networkConnectivityMonitor: NetworkConnectivityMonitor,
    private val userListApi: UserListApi,
    private val userPreferences: UserPreferences
) : UserRepository {
    override fun observeUsers(): Flow<Result<List<User>>> =
        flow {
            if (!networkConnectivityMonitor.isAvailable()) {
                emit(Result.failure(IOException()))
                return@flow
            }

            emit(userListApi.fetch())
        }.combine(userPreferences.followedUsers) { result, followedIds ->
            result.map { users ->
                users.applyFollowedIds(followedIds)
            }
        }

    override suspend fun toggleFollow(userId: Int) {
        userPreferences.toggleFollow(userId)
    }

    private fun List<User>.applyFollowedIds(followedIds: Set<Int>): List<User> {
        return map { user ->
            user.copy(isFollowed = user.id in followedIds)
        }
    }
}
