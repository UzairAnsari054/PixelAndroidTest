package com.uzair.pixel.test.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.uzair.pixel.test.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserPreferences {
    val followedUsers: Flow<Set<Int>>
    suspend fun toggleFollow(userId: Int)
}

class UserPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferences {

    private companion object {
        val FOLLOWED_USERS_KEY = stringSetPreferencesKey("followed_users")
    }

    override val followedUsers: Flow<Set<Int>> =
        dataStore.data.map { prefs ->
            prefs[FOLLOWED_USERS_KEY]
                ?.asSequence()
                ?.mapNotNull(String::toIntOrNull)
                ?.toSet()
                ?: emptySet()
        }

    override suspend fun toggleFollow(userId: Int) {
        val userIdString = userId.toString()

        dataStore.edit { prefs ->
            val current = prefs[FOLLOWED_USERS_KEY].orEmpty()
            prefs[FOLLOWED_USERS_KEY] =
                if (userIdString in current) current - userIdString
                else current + userIdString
        }
    }
}
