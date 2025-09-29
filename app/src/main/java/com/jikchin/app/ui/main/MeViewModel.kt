package com.jikchin.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jikchin.app.data.repository.MeRepository
import com.jikchin.app.domain.model.MeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeViewModel(
    private val repo: MeRepository
) : ViewModel() {

    private val _me = MutableStateFlow<MeResponse?>(null)
    val me: StateFlow<MeResponse?> = _me

    fun load() {
        viewModelScope.launch {
            runCatching { repo.getMe() }
                .onSuccess { _me.value = it }
                .onFailure { e-> e.printStackTrace() }
        }
    }
}