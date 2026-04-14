package com.uzair.pixel.test.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uzair.pixel.test.R
import com.uzair.pixel.test.presentation.components.UserList
import com.uzair.pixel.test.presentation.components.UserMockData
import com.uzair.pixel.test.presentation.theme.DarkBlue
import com.uzair.pixel.test.presentation.theme.OffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreenRoot(
    viewModel: UserListViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.top_users_title),
                        color = OffWhite
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(DarkBlue)
            )
        }
    ) { innerPadding ->
        UserListScreen(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = { action ->
                viewModel.onAction(action)
            }
        )
    }
}

@Composable
private fun UserListScreen(
    modifier: Modifier = Modifier,
    state: UserListUiState,
    onAction: (UserListAction) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = OffWhite,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is UserListUiState.Loading -> CircularProgressIndicator(color = DarkBlue)

                is UserListUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = state.message.asString(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )

                        Button(
                            onClick = { onAction(UserListAction.OnRetryClick) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                is UserListUiState.Success -> {
                    UserList(
                        users = state.topUsers,
                        onToggleFollow = {
                            onAction(UserListAction.OnToggleFollow(it))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListScreenSuccessPreview() {
    val state = UserListUiState.Success(
        topUsers = UserMockData.users
    )
    UserListScreen(
        state = state,
        onAction = {},
        modifier = Modifier.fillMaxSize()
    )
}