package org.homesharing.cashbackhome

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksScreen
import org.homesharing.cashbackhome.presentation.categories.EditCategoryScreenRoot
import org.homesharing.cashbackhome.presentation.home.HomeScreenRoot
import org.homesharing.cashbackhome.presentation.navigation.AppRoute
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme

@Composable
fun App() {
    CashbackHomeTheme {
        val backStack = rememberNavBackStack(
            configuration = SavedStateConfiguration {
                serializersModule = SerializersModule {
                    polymorphic(NavKey::class) {
                        subclass(AppRoute.Home::class, AppRoute.Home.serializer())
                        subclass(
                            AppRoute.AddCardWithCashbacks::class,
                            AppRoute.AddCardWithCashbacks.serializer()
                        )
                        subclass(
                            AppRoute.PersonalCabinet::class,
                            AppRoute.PersonalCabinet.serializer()
                        )
                        subclass(
                            AppRoute.EditCardWithCashback::class,
                            AppRoute.EditCardWithCashback.serializer()
                        )
                        subclass(
                            AppRoute.EditCategoryScreen::class,
                            AppRoute.EditCategoryScreen.serializer()
                        )
                    }
                }
            },
            AppRoute.Home
        )

        NavDisplay(
            backStack = backStack,
            onBack = {
                backStack.removeLastOrNull()
            },
            entryProvider = entryProvider {
                entry<AppRoute.Home> {
                    HomeScreenRoot(
                        onAddCategoryClick = {
                            backStack.add(AppRoute.AddCardWithCashbacks)
                        },
                        onEditCategoryClick = {
                            backStack.add(AppRoute.EditCategoryScreen)
                        }
                    )
                }

                entry<AppRoute.AddCardWithCashbacks> {
                    AddCardWithCashbacksScreen(
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onSavedSuccessfully = {
                            backStack.removeLastOrNull()
                        },
                    )
                }

                entry<AppRoute.EditCategoryScreen> {
                    EditCategoryScreenRoot()
                }

                entry<AppRoute.PersonalCabinet> {
//                    TODO("Add personal cabinet screen")
                }

                entry<AppRoute.EditCardWithCashback> {
//                    TODO("Add edit category screen")
                }
            },
        )
    }
}
