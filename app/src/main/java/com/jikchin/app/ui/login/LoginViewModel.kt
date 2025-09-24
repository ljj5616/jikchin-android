package com.jikchin.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jikchin.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val needProfile: Boolean) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state

    fun socialLogin(provider: String, token: String) {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading
            val r = repo.socialLogin(provider, token)
            _state.value = r.fold(
                onSuccess = { LoginUiState.Success(it.needProfile) },
                onFailure = { LoginUiState.Error(it.message ?: "로그인 실패") }
            )
        }
    }
}