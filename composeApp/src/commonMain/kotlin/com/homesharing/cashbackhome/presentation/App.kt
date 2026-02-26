package com.homesharing.cashbackhome.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksScreen
import com.homesharing.cashbackhome.presentation.cards.CardsScreen
import com.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme

@Composable
fun App() {
    CashbackHomeTheme {
        var showDetails by remember { mutableStateOf(false) }

        if (showDetails) {
            AddCardWithCashbacksScreen(
                onBackClick = { showDetails = false },
                onSavedSuccessfully = { showDetails = false }
            )
        } else {
            CardsScreen(
                onAddCardClick = { showDetails = true }
            )
        }
    }
}