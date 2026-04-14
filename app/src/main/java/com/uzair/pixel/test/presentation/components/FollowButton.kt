package com.uzair.pixel.test.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uzair.pixel.test.R
import com.uzair.pixel.test.presentation.theme.DarkBlue

@Composable
fun FollowButton(
    isFollowed: Boolean,
    onToggleFollow: () -> Unit
) {
    if (isFollowed) {
        OutlinedButton(
            onClick = onToggleFollow,
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Text(stringResource(R.string.unfollow))
        }
    } else {
        Button(
            onClick = onToggleFollow,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkBlue
            )
        ) {
            Text(stringResource(R.string.follow))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowButtonPreview() {
    FollowButton(
        isFollowed = false,
        onToggleFollow = {}
    )
}