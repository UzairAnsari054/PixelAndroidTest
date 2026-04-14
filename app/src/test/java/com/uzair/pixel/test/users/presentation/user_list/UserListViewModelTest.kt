package com.uzair.pixel.test.users.presentation.user_list

import com.uzair.pixel.test.users.domain.model.User
import com.uzair.pixel.test.users.domain.repository.FakeUserRepository
import com.uzair.pixel.test.users.domain.repository.UserRepository
import com.uzair.pixel.test.users.domain.storage.FakeFollowStorage
import com.uzair.pixel.test.users.presentation.user_list.model.UserUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `success state is emitted when API returns users`() {

        val users = listOf(
            User(1, "A", "", 100),
            User(2, "B", "", 200)
        )

        val viewModel = UserListViewModel(
            userRepository = FakeUserRepository(Result.success(users)),
            followStorage = FakeFollowStorage()
        )

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value

        assert(state is UserListState.Success)
    }

    @Test
    fun `error state is emitted when API fails`() {

        val viewModel = UserListViewModel(
            userRepository = FakeUserRepository(
                Result.failure(IOException("No internet"))
            ),
            followStorage = FakeFollowStorage()
        )

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value

        assert(state is UserListState.Error)
    }

    @Test
    fun `toggle follow updates storage`() {

        val storage = FakeFollowStorage()

        val viewModel = UserListViewModel(
            userRepository = FakeUserRepository(Result.success(emptyList())),
            followStorage = storage
        )

        dispatcher.scheduler.advanceUntilIdle()

        val user = UserUiModel(
            id = 1,
            name = "Test",
            imageUrl = "",
            reputation = 100,
            isFollowed = false
        )

        viewModel.onAction(UserListAction.OnToggleFollow(user))

        dispatcher.scheduler.advanceUntilIdle()

        assert(storage.followedUsersFlow.value.contains("1"))
    }

    @Test
    fun `retry triggers repository call`() {

        var callCount = 0

        val repo = object : UserRepository {
            override suspend fun getTopStackOverUsers(): Result<List<User>> {
                callCount++
                return Result.success(emptyList())
            }
        }

        val viewModel = UserListViewModel(
            userRepository = repo,
            followStorage = FakeFollowStorage()
        )

        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(UserListAction.OnRetryClick)

        dispatcher.scheduler.advanceUntilIdle()

        assert(callCount >= 2)
    }
}