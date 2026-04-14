package com.uzair.pixel.test.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uzair.pixel.test.R
import com.uzair.pixel.test.presentation.components.UserMockData
import com.uzair.pixel.test.presentation.model.UserUiModel
import com.uzair.pixel.test.presentation.theme.LightBlue

@Composable
fun UserListItem(
    user: UserUiModel,
    onToggleFollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = LightBlue.copy(alpha = 0.2f),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UserImage(
                imageUrl = user.imageUrl,
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = stringResource(
                        R.string.reputation_format,
                        user.reputationDisplayFormat
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FollowButton(
                isFollowed = user.isFollowed,
                onToggleFollow = onToggleFollow
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListItemPreview() {
    UserListItem(
        user = UserMockData.users.first(),
        onToggleFollow = {},
    )
}
