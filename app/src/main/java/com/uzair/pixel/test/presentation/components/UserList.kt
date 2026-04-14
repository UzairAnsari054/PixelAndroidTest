package com.uzair.pixel.test.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uzair.pixel.test.presentation.components.UserMockData
import com.uzair.pixel.test.presentation.model.UserUiModel

@Composable
fun UserList(
    users: List<UserUiModel>,
    onToggleFollow: (UserUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserListItem(
                user = user,
                modifier = Modifier.fillMaxWidth(),
                onToggleFollow = {
                    onToggleFollow(user)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    UserList(
        users = UserMockData.users,
        onToggleFollow = {},
        modifier = Modifier.fillMaxSize()
    )
}

