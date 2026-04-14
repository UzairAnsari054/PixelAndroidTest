package com.uzair.pixel.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uzair.pixel.test.presentation.theme.PixelAndroidTestTheme
import com.uzair.pixel.test.presentation.UserListScreenRoot
import com.uzair.pixel.test.presentation.UserListViewModel
import com.uzair.pixel.test.presentation.UserListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PixelAndroidTestTheme {
                val context = LocalContext.current
                val app = context.applicationContext as StackOverflowApplication

                val factory = remember {
                    UserListViewModelFactory(
                        fetchUsersUseCase = app.containerDI.fetchUsersUseCase,
                        toggleFollowUseCase = app.containerDI.toggleFollowUseCase,
                    )
                }

                val viewModel: UserListViewModel = viewModel(factory = factory)

                UserListScreenRoot(viewModel = viewModel)
            }
        }
    }
}
