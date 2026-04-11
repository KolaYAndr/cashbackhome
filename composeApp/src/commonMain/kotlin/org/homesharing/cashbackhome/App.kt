package org.homesharing.cashbackhome

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.homesharing.cashbackhome.presentation.cards.AddCardScreenRoot
import org.homesharing.cashbackhome.presentation.cards.EditCardScreenRoot
import org.homesharing.cashbackhome.presentation.categories.AddCategoryScreenRoot
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
                            AppRoute.AddCategoryScreen::class,
                            AppRoute.AddCategoryScreen.serializer()
                        )
                        subclass(
                            AppRoute.AddCardScreen::class,
                            AppRoute.AddCardScreen.serializer()
                        )
                        subclass(
                            AppRoute.PersonalCabinet::class,
                            AppRoute.PersonalCabinet.serializer()
                        )
                        subclass(
                            AppRoute.EditCardScreen::class,
                            AppRoute.EditCardScreen.serializer()
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
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<AppRoute.Home> {
                    HomeScreenRoot(
                        onAddCategoryClick = {
                            backStack.add(AppRoute.AddCategoryScreen)
                        },
                        onAddCardClick = {
                            backStack.add(AppRoute.AddCardScreen)
                        },
                        onEditCategoryClick = { category ->
                            backStack.add(AppRoute.EditCategoryScreen(category))
                        },
                        onEditCardClick = { card ->
                            backStack.add(AppRoute.EditCardScreen(card))
                        }
                    )
                }

                entry<AppRoute.AddCardScreen> {
                    AddCardScreenRoot(
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onSavedSuccessfully = {
                            backStack.removeLastOrNull()
                        },
                    )
                }

                entry<AppRoute.AddCategoryScreen> {
                    AddCategoryScreenRoot(
                        onSavedSuccessfully = {
                            backStack.removeLastOrNull()
                        },
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onAddCardClick = {
                            backStack.add(AppRoute.AddCardScreen)
                        }
                    )
                }

                entry<AppRoute.EditCategoryScreen> { key ->
                    EditCategoryScreenRoot(
                        category =  key.category,
                        onSavedSuccessfully = {
                            backStack.removeLastOrNull()
                        },
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onAddCardClick = {
                            backStack.add(AppRoute.AddCardScreen)
                        }
                    )
                }

                entry<AppRoute.PersonalCabinet> {
//                    TODO("Add personal cabinet screen")
                }

                entry<AppRoute.EditCardScreen> { key ->
                    EditCardScreenRoot(
                        card = key.card,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onSavedSuccessfully = {
                            backStack.removeLastOrNull()
                        },
                    )
                }
            },
        )
    }
}
