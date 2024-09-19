package com.example.technicaltask.service

import android.util.Log
import com.example.technicaltask.BuildConfig
import com.example.technicaltask.model.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.logging.Level
import java.util.logging.Logger

class UserServiceImpl: UserService {
    init {
        Logger.getLogger(OkHttpClient::class.java.name).level = Level.FINE
    }

    private val client = OkHttpClient()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val token = BuildConfig.GIT_TOKEN

    override suspend fun getAllUsers(): List<User?>? {
        return withContext(Dispatchers.IO) {
            val httpRequest = Request.Builder()
                .url(HttpRoutes.USERS_URL)
                .get()
                .addHeader("Authorization", "token $token")
                .build()

            try {
                client.newCall(httpRequest).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("getAllUsers", "Request failed with code: ${response.code}")
                        return@withContext null
                    }

                    val responseBody = response.body?.string()
                    parseUsersListResponse(responseBody)
                }
            } catch (e: Exception) {
                Log.e("getAllUsers", e.localizedMessage, e)
                null
            }
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            val httpRequest = Request.Builder()
                .url("${HttpRoutes.USERS_URL}/$username")
                .get()
                .addHeader("Authorization", "token $token")
                .build()

            try {
                client.newCall(httpRequest).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("getUserByUsername", "Request failed with code: ${response.code}")
                        return@withContext null
                    }

                    val responseBody = response.body?.string()
                    parseUserResponse(responseBody)
                }
            } catch (e: Exception) {
                Log.e("getUserByUsername", e.localizedMessage, e)
                null
            }
        }
    }

    private fun parseUsersListResponse(responseBody: String?): List<User>? {
        return responseBody?.let {
            val adapter: JsonAdapter<List<User>> = moshi.adapter(
                Types.newParameterizedType(List::class.java, User::class.java)
            )
            adapter.fromJson(it)
        }
    }

    private fun parseUserResponse(responseBody: String?): User? {
        return responseBody?.let {
            val adapter: JsonAdapter<User> = moshi.adapter(User::class.java)
            adapter.fromJson(it)
        }
    }
}