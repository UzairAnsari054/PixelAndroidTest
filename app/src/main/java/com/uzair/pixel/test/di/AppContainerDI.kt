package com.uzair.pixel.test.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.preferences.preferencesDataStore
import com.uzair.pixel.test.data.local.UserPreferencesImpl
import com.uzair.pixel.test.data.remote.UserListApiImpl
import com.uzair.pixel.test.data.remote.UserListApi
import com.uzair.pixel.test.data.repository.UserRepositoryImpl
import com.uzair.pixel.test.data.remote.UserListNetworkParser
import com.uzair.pixel.test.util.NetworkConnectivityMonitorImpl
import com.uzair.pixel.test.domain.repository.UserRepository
import com.uzair.pixel.test.data.local.UserPreferences
import com.uzair.pixel.test.domain.usecase.FetchUsersUseCase
import com.uzair.pixel.test.domain.usecase.ToggleFollowUseCase
import com.uzair.pixel.test.util.NetworkConnectivityMonitor

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class AppContainerDI(context: Context) {

    private val appContext = context.applicationContext

    private val connectivityManager: ConnectivityManager by lazy {
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val parser: UserListNetworkParser by lazy {
        UserListNetworkParser()
    }

    private val dataStore by lazy {
        appContext.dataStore
    }

    val userListApi: UserListApi by lazy {
        UserListApiImpl(parser = parser)
    }

    val networkConnectivityMonitor: NetworkConnectivityMonitor by lazy {
        NetworkConnectivityMonitorImpl(connectivityManager = connectivityManager)
    }

    val userPreferences: UserPreferences by lazy {
        UserPreferencesImpl(dataStore = dataStore)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            networkConnectivityMonitor = networkConnectivityMonitor,
            userListApi = userListApi,
            userPreferences = userPreferences
        )
    }

    val fetchUsersUseCase: FetchUsersUseCase by lazy {
        FetchUsersUseCase(userRepository = userRepository)
    }

    val toggleFollowUseCase: ToggleFollowUseCase by lazy {
        ToggleFollowUseCase(userRepository = userRepository)
    }
}
