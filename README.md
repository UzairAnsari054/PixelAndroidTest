# PixelAndroidTest

`PixelAndroidTest` is a Kotlin Android app built with Jetpack Compose that fetches the top 20 StackOverflow users and displays them in a scrollable list. Each row shows the user's profile image, display name, reputation, and a locally simulated follow/unfollow action whose state persists between app launches.

## Features

- Fetches the top 20 StackOverflow users from the StackExchange API.
- Displays each user's avatar, name, and reputation.
- Supports local `Follow` / `Unfollow` actions per user.
- Persists follow state across sessions using `DataStore`.
- Shows an error state with a retry action when the device is offline or the server request fails.
- Built entirely with Kotlin and Jetpack Compose.
- Avoids third-party libraries as requested.

## API

The app currently requests users from:

`https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow`

## How It Works

On launch, `MainActivity` creates `UserListViewModel` using dependencies provided by `AppContainerDI`. The `ViewModel` starts observing the user stream immediately and exposes a `StateFlow` of UI state to the Compose screen.

The repository is responsible for:

- Checking network availability before making a request.
- Fetching and parsing the StackOverflow response using `HttpURLConnection`.
- Merging remote user data with locally stored follow information.
- Updating persisted follow state when the user toggles follow/unfollow.

## Architecture

The project uses a simple layered architecture focused on testability and separation of concerns:

- `presentation`
  Compose UI, screen state, UI models, and `UserListViewModel`.
- `domain`
  Core `User` model, repository contract, and use cases (`FetchUsersUseCase`, `ToggleFollowUseCase`).
- `data`
  Remote API access, JSON parsing, repository implementation, and local preference storage.
- `di`
  A lightweight manual dependency container (`AppContainerDI`).
- `util`
  Network checks, error mapping, and UI message helpers.

## Tech Stack

- Kotlin
- Jetpack Compose
- AndroidX ViewModel
- Kotlin Coroutines and `StateFlow`
- AndroidX DataStore Preferences
- `HttpURLConnection` for networking
- Manual JSON parsing and image loading


```bash
./gradlew test
```

Current status:

- The existing test sources do not currently compile against the latest production code.
- `./gradlew test` fails because the tests still reference older package names and outdated interfaces/models.

This means the production app code is present, but the test suite needs to be updated to match the current architecture before it can be considered passing.



https://github.com/user-attachments/assets/4f8907df-e585-465a-9406-617e16bad836




