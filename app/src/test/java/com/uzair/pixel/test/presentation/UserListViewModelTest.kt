package com.uzair.pixel.test.presentation

import com.uzair.pixel.test.domain.model.User
import com.uzair.pixel.test.domain.repository.UserRepository
import com.uzair.pixel.test.domain.usecase.FetchUsersUseCase
import com.uzair.pixel.test.domain.usecase.ToggleFollowUseCase
import com.uzair.pixel.test.presentation.model.UserUiModel
import com.uzair.pixel.test.util.UiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenUsersFromRepository_whenViewModelInitializes_thenEmitSuccessState() = runTest(dispatcher) {
        val repository = FakeUserRepository()
        repository.emit(Result.success(listOf(FOLLOWED_USER)))

        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UserListUiState.Success)
        val successState = state as UserListUiState.Success
        assertEquals(
            listOf(FOLLOWED_USER_UI_MODEL),
            successState.topUsers
        )
    }

    @Test
    fun givenFailureFromRepository_whenViewModelInitializes_thenEmitErrorState() = runTest(dispatcher) {
        val repository = FakeUserRepository()
        repository.emit(Result.failure(IOException(OFFLINE_MESSAGE)))

        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UserListUiState.Error)
        val errorState = state as UserListUiState.Error
        assertTrue(errorState.message is UiMessage.StringResource)
    }

    @Test
    fun givenToggleFollowAction_whenHandled_thenDelegateUserIdToRepository() = runTest(dispatcher) {
        val repository = FakeUserRepository()
        repository.emit(Result.success(emptyList()))
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        viewModel.onAction(UserListAction.OnToggleFollow(NOT_FOLLOWED_USER_UI_MODEL))

        advanceUntilIdle()

        assertEquals(listOf(NOT_FOLLOWED_USER_ID), repository.toggledUserIds)
    }

    @Test
    fun givenRetryAction_whenHandled_thenTriggerFreshObserveCall() = runTest(dispatcher) {
        val repository = FakeUserRepository()
        repository.emit(Result.success(emptyList()))
        val viewModel = createViewModel(repository)

        advanceUntilIdle()
        assertEquals(1, repository.observeCallCount)

        viewModel.onAction(UserListAction.OnRetryClick)

        advanceUntilIdle()

        assertEquals(2, repository.observeCallCount)
    }

    private fun createViewModel(repository: FakeUserRepository): UserListViewModel {
        return UserListViewModel(
            fetchUsersUseCase = FetchUsersUseCase(repository),
            toggleFollowUseCase = ToggleFollowUseCase(repository)
        )
    }

    private class FakeUserRepository : UserRepository {
        private val resultFlow = MutableStateFlow<Result<List<User>>>(Result.success(emptyList()))
        var observeCallCount: Int = 0
            private set
        val toggledUserIds = mutableListOf<Int>()

        fun emit(result: Result<List<User>>) {
            resultFlow.value = result
        }

        override fun observeUsers(): Flow<Result<List<User>>> {
            observeCallCount++
            return resultFlow
        }

        override suspend fun toggleFollow(userId: Int) {
            toggledUserIds += userId
        }
    }

    private companion object {
        const val FOLLOWED_USER_ID = 1
        const val NOT_FOLLOWED_USER_ID = 42
        const val FOLLOWED_USER_NAME = "Uzair"
        const val NOT_FOLLOWED_USER_NAME = "Test"
        const val IMAGE_URL = "img"
        const val EMPTY_IMAGE_URL = ""
        const val FOLLOWED_USER_REPUTATION = 1_200
        const val NOT_FOLLOWED_USER_REPUTATION = 100
        const val OFFLINE_MESSAGE = "offline"

        val FOLLOWED_USER = User(
            id = FOLLOWED_USER_ID,
            name = FOLLOWED_USER_NAME,
            imageUrl = IMAGE_URL,
            reputation = FOLLOWED_USER_REPUTATION,
            isFollowed = true
        )

        val FOLLOWED_USER_UI_MODEL = UserUiModel(
            id = FOLLOWED_USER_ID,
            name = FOLLOWED_USER_NAME,
            imageUrl = IMAGE_URL,
            reputation = FOLLOWED_USER_REPUTATION,
            isFollowed = true
        )

        val NOT_FOLLOWED_USER_UI_MODEL = UserUiModel(
            id = NOT_FOLLOWED_USER_ID,
            name = NOT_FOLLOWED_USER_NAME,
            imageUrl = EMPTY_IMAGE_URL,
            reputation = NOT_FOLLOWED_USER_REPUTATION,
            isFollowed = false
        )
    }
}
