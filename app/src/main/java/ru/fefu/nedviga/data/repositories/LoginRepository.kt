package ru.fefu.nedviga.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.fefu.nedviga.util.Result
import ru.fefu.nedviga.data.models.User
import ru.fefu.nedviga.data.models.UserToken
import ru.fefu.nedviga.data.network.ApiInterface
import ru.fefu.nedviga.data.network.App

class LoginRepository {
    private val activityApi = App.INSTANCE.retrofit.create(ApiInterface::class.java)

    suspend fun login(login:String, password:String): Flow<Result<UserToken>> =
        flow<Result<UserToken>> {
            emit(
                Result.Success(
                    activityApi.login(login, password)
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)

    suspend fun getProfile(): Flow<Result<User>> =
        flow<Result<User>> {
            emit(
                Result.Success(
                    activityApi.getProfile()
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)

    suspend fun logout(): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    activityApi.logout()
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)
}