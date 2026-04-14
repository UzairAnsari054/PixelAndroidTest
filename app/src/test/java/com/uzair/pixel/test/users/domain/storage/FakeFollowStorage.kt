package com.uzair.pixel.test.users.domain.storage

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeFollowStorage : FollowStorage {

    private val _followed = MutableStateFlow(setOf<String>())

    override val followedUsersFlow: StateFlow<Set<String>> = _followed

    override suspend fun toggleFollow(userId: String) {
        _followed.value =
            if (_followed.value.contains(userId)) {
                _followed.value - userId
            } else {
                _followed.value + userId
            }
    }
}