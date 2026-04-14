package com.uzair.pixel.test.data.remote

import com.uzair.pixel.test.util.CONNECT_TIMEOUT_MS
import com.uzair.pixel.test.util.REQUEST_GET
import com.uzair.pixel.test.util.READ_TIMEOUT_MS
import com.uzair.pixel.test.util.USERS_ENDPOINT
import com.uzair.pixel.test.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UserListApiImpl(
    private val parser: UserListNetworkParser
) : UserListApi {
    override suspend fun fetch(): Result<List<User>> =
        withContext(Dispatchers.IO) {
            try {
                val connection = createConnection()

                try {
                    val responseCode = connection.responseCode
                    val responseBody = readResponse(connection, responseCode)

                    when (responseCode) {
                        in 200..299 -> {
                            if (responseBody.isBlank()) {
                                Result.failure(IOException())
                            } else {
                                val users = withContext(Dispatchers.Default) {
                                    parser.parseJson(responseBody)
                                        .map(UserNetworkModel::toUser)
                                }
                                Result.success(users)
                            }
                        }

                        else -> {
                            Result.failure(IOException("Unexpected response: $responseCode"))
                        }
                    }

                } finally {
                    connection.disconnect()
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun createConnection(): HttpURLConnection {
        return (URL(USERS_ENDPOINT).openConnection() as HttpURLConnection).apply {
            requestMethod = REQUEST_GET
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            doInput = true
        }
    }

    private fun readResponse(
        connection: HttpURLConnection,
        responseCode: Int
    ): String {
        val stream = if (responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }

        return stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
            .orEmpty()
    }
}
