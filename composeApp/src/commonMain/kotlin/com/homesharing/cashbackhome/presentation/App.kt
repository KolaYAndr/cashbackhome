package com.homesharing.cashbackhome.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksScreen
import com.homesharing.cashbackhome.presentation.home.HomeScreenRoot
import com.homesharing.cashbackhome.presentation.navigation.AppRoute
import com.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme

@Composable
fun App() {
    CashbackHomeTheme {
        val backStack = rememberNavBackStack(AppRoute.Home)

        NavDisplay(
            backStack = backStack,
            onBack = {
                while (backStack.isNotEmpty()) {
                    backStack.removeLastOrNull()
                }
                backStack.add(AppRoute.Home)
            },
            entryProvider = entryProvider {
                entry<AppRoute.Home> {
                    HomeScreenRoot(
                        onAddCategoryClick = {
                            while (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                            backStack.add(AppRoute.AddCardWithCashbacks)
                        },
                    )
                }

                entry<AppRoute.AddCardWithCashbacks> {
                    AddCardWithCashbacksScreen(
                        onBackClick = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        },
                        onSavedSuccessfully = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        },
                    )
                }

                entry<AppRoute.PersonalCabinet> {
                    TODO("Add personal cabinet screen")
                }

                entry<AppRoute.EditCardWithCashback> {
                    TODO("Add edit category screen")
                }
            },
        )
    }
}
