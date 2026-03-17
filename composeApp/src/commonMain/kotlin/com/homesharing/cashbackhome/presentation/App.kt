package com.homesharing.cashbackhome.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksScreen
import com.homesharing.cashbackhome.presentation.cards.CardsScreen
import com.homesharing.cashbackhome.presentation.categories.CategoriesScreenRoot
import com.homesharing.cashbackhome.presentation.navigation.Route
import com.homesharing.cashbackhome.presentation.promotions.PromotionsScreen
import com.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme

@Composable
fun App() {
    CashbackHomeTheme {
        val backStack = rememberNavBackStack(Route.Categories)

        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider =
                entryProvider {
                    entry<Route.Categories> {
                        CategoriesScreenRoot(
                            onCardsTabClick = {
                                while (backStack.isNotEmpty()) {
                                    backStack.removeLastOrNull()
                                }
                                backStack.add(Route.Categories)
                                backStack.add(Route.Cards)
                            },
                            onPromotionsTabClick = {
                                while (backStack.isNotEmpty()) {
                                    backStack.removeLastOrNull()
                                }
                                backStack.add(Route.Categories)
                                backStack.add(Route.Promotions)
                            },
                            onAddCategoryClick = {
                                while (backStack.isNotEmpty()) {
                                    backStack.removeLastOrNull()
                                }
                                backStack.add(Route.Categories)
                                backStack.add(Route.AddCardWithCashbacks)
                            },
                        )
                    }

                    entry<Route.Cards> {
                        CardsScreen(
                            onAddCardClick = { backStack.add(Route.AddCardWithCashbacks) },
                        )
                    }

                    entry<Route.Promotions> {
                        PromotionsScreen()
                    }

                    entry<Route.AddCardWithCashbacks> {
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
                },
        )
    }
}
