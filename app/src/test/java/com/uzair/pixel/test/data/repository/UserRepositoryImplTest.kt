package com.uzair.pixel.test.data.repository

import com.uzair.pixel.test.data.local.UserPreferences
import com.uzair.pixel.test.data.remote.UserListApi
import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.util.NetworkConnectivityMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    @Test
    fun givenNetworkUnavailable_whenObserveUsers_thenEmitFailure() = runTest {
        val repository = UserRepositoryImpl(
            networkConnectivityMonitor = FakeNetworkConnectivityMonitor(isAvailable = false),
            userListApi = FakeUserListApi(Result.success(emptyList())),
            userPreferences = FakeUserPreferences()
        )

        val result = repository.observeUsers().first()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun givenFollowedIdsInPreferences_whenObserveUsers_thenApplyFollowedState() = runTest {
        val repository = UserRepositoryImpl(
            networkConnectivityMonitor = FakeNetworkConnectivityMonitor(isAvailable = true),
            userListApi = FakeUserListApi(Result.success(listOf(FIRST_USER, SECOND_USER))),
            userPreferences = FakeUserPreferences(setOf(SECOND_USER_ID))
        )

        val users = repository.observeUsers().first().getOrThrow()

        assertEquals(false, users[0].isFollowed)
        assertEquals(true, users[1].isFollowed)
    }

    @Test
    fun givenUserId_whenToggleFollow_thenDelegateToPreferences() = runTest {
        val preferences = FakeUserPreferences()
        val repository = UserRepositoryImpl(
            networkConnectivityMonitor = FakeNetworkConnectivityMonitor(isAvailable = true),
            userListApi = FakeUserListApi(Result.success(emptyList())),
            userPreferences = preferences
        )

        repository.toggleFollow(TOGGLED_USER_ID)

        assertEquals(listOf(TOGGLED_USER_ID), preferences.toggledIds)
    }

    private class FakeNetworkConnectivityMonitor(
        private val isAvailable: Boolean
    ) : NetworkConnectivityMonitor {
        override fun isAvailable(): Boolean = isAvailable
        override val isConnected: Flow<Boolean> = MutableStateFlow(isAvailable)
    }

    private class FakeUserListApi(
        private val result: Result<List<User>>
    ) : UserListApi {
        override suspend fun fetch(): Result<List<User>> = result
    }

    private class FakeUserPreferences(initialFollowedIds: Set<Int> = emptySet()) : UserPreferences {
        private val followedState = MutableStateFlow(initialFollowedIds)
        val toggledIds = mutableListOf<Int>()

        override val followedUsers: Flow<Set<Int>> = followedState

        override suspend fun toggleFollow(userId: Int) {
            toggledIds += userId
            followedState.value = if (userId in followedState.value) {
                followedState.value - userId
            } else {
                followedState.value + userId
            }
        }
    }

    private companion object {
        const val FIRST_USER_ID = 1
        const val SECOND_USER_ID = 2
        const val TOGGLED_USER_ID = 7
        const val FIRST_USER_NAME = "Uzair"
        const val SECOND_USER_NAME = "Karan"
        const val EMPTY_IMAGE_URL = ""
        const val FIRST_USER_REPUTATION = 100
        const val SECOND_USER_REPUTATION = 200

        val FIRST_USER = User(
            id = FIRST_USER_ID,
            name = FIRST_USER_NAME,
            imageUrl = EMPTY_IMAGE_URL,
            reputation = FIRST_USER_REPUTATION
        )

        val SECOND_USER = User(
            id = SECOND_USER_ID,
            name = SECOND_USER_NAME,
            imageUrl = EMPTY_IMAGE_URL,
            reputation = SECOND_USER_REPUTATION
        )
    }
}
