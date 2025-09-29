package com.jikchin.app.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    vm: LoginViewModel,
    onClickGoogle: () -> Unit,
    onClickKakao: () -> Unit,
    onClickNaver: () -> Unit,
    onNext: (needProfile: Boolean) -> Unit
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) {
            onNext((state as LoginUiState.Success).needProfile)
        }
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = onClickGoogle, modifier = Modifier.fillMaxWidth()) {
            Text("Google로 계속")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onClickKakao, modifier = Modifier.fillMaxWidth()) {
            Text("Kakao로 계속")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onClickNaver, modifier = Modifier.fillMaxWidth()) {
            Text("Naver로 계속")
        }

        when (state) {
            is LoginUiState.Loading -> { Spacer(Modifier.height(12.dp)); Text("로그인 중...") }
            is LoginUiState.Error -> { Spacer(Modifier.height(12.dp)); Text((state as LoginUiState.Error).message, color = MaterialTheme.colorScheme.error) }
            else -> {}
        }
    }
}