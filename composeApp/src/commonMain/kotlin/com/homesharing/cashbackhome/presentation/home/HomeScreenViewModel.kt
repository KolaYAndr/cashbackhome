package com.homesharing.cashbackhome.presentation.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel : ViewModel(){
    private val _tabState: MutableStateFlow<Int> = MutableStateFlow(0)
    val tabState: StateFlow<Int> = _tabState.asStateFlow()

    fun switchToTab(tabId: Int) {
        _tabState.value = tabId
    }
}
