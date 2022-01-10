package ru.fefu.nedviga.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.nedviga.util.Result
import ru.fefu.nedviga.data.models.UserToken
import ru.fefu.nedviga.data.repositories.LoginRepository

class LoginViewModel:ViewModel() {
    private val loginRepository = LoginRepository()

    private val _dataFlow = MutableSharedFlow<Result<UserToken>>(replay = 0)
    val dataFlow get() = _dataFlow

    fun login(login:String, password:String) {
        viewModelScope.launch {
            loginRepository.login(login, password)
                .collect {
                    when(it) {
                        is Result.Success<*> -> _dataFlow.emit(it)
                        is Result.Error<*> -> _dataFlow.emit(it)
                    }
                }
        }
    }
}