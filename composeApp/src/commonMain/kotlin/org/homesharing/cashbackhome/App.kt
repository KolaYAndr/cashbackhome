package org.homesharing.cashbackhome

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.flow.first
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.homesharing.cashbackhome.data.local.database.CardCashbackDao
import org.homesharing.cashbackhome.data.local.database.entity.ProfileSettings
import org.homesharing.cashbackhome.data.local.database.entity.ThemeMode
import org.homesharing.cashbackhome.domain.model.mockAuthenticatedUser
import org.homesharing.cashbackhome.presentation.cards.AddCardScreenRoot
import org.homesharing.cashbackhome.presentation.cards.BankPickerOptions
import org.homesharing.cashbackhome.presentation.cards.BankSelectionResult
import org.homesharing.cashbackhome.presentation.cards.CardDetailsScreenRoot
import org.homesharing.cashbackhome.presentation.cards.ChooseBankScreen
import org.homesharing.cashbackhome.presentation.cards.EditCardScreenRoot
import org.homesharing.cashbackhome.presentation.categories.AddCategoryScreenRoot
import org.homesharing.cashbackhome.presentation.categories.EditCategoryScreenRoot
import org.homesharing.cashbackhome.presentation.home.HomeScreenRoot
import org.homesharing.cashbackhome.presentation.navigation.AppRoute
import org.homesharing.cashbackhome.presentation.profile.ProfileScreenRoot
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.koin.compose.koinInject

@Composable
fun App() {
    val dao = koinInject<CardCashbackDao>()
    val profileSettings by dao.getProfileSettings().collectAsState(initial = null)
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (profileSettings?.themeMode ?: ThemeMode.System) {
        ThemeMode.System -> systemDarkTheme
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
    }

    LaunchedEffect(dao) {
        if (dao.getProfileSettings().first() == null) {
            dao.upsertProfileSettings(ProfileSettings())
        }
    }

    var authenticatedUser by remember { mutableStateOf(mockAuthenticatedUser()) }

    CashbackHomeTheme(darkTheme = darkTheme) {
        val knownBankNames = remember {
            BankPickerOptions.map { it.name }.toSet()
        }
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
                            AppRoute.CardDetailsScreen::class,
                            AppRoute.CardDetailsScreen.serializer()
                        )
                        subclass(
                            AppRoute.ChooseBankScreen::class,
                            AppRoute.ChooseBankScreen.serializer()
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
        var bankSelectionEventId by remember { mutableStateOf(0L) }
        var bankSelectionResult by remember { mutableStateOf<BankSelectionResult?>(null) }

        fun openChooseBankScreen(currentBankName: String?) {
            val isKnownBankName = currentBankName != null && currentBankName in knownBankNames
            backStack.add(
                AppRoute.ChooseBankScreen(
                    selectedBankName = currentBankName.takeIf { isKnownBankName },
                )
            )
        }

        fun publishBankSelectionResult(bankName: String) {
            bankSelectionEventId += 1
            bankSelectionResult = BankSelectionResult(
                bankName = bankName,
                eventId = bankSelectionEventId,
            )
        }

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
                        profileName = authenticatedUser.username,
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
                        },
                        onCardClick = { card ->
                            backStack.add(AppRoute.CardDetailsScreen(card))
                        },
                        onProfileClick = {
                            backStack.add(AppRoute.PersonalCabinet)
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
                        onChooseBankClick = ::openChooseBankScreen,
                        bankSelectionResult = bankSelectionResult,
                        onBankSelectionResultConsumed = {
                            bankSelectionResult = null
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
                    ProfileScreenRoot(
                        user = authenticatedUser,
                        onUserChanged = { authenticatedUser = it },
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                    )
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
                        onChooseBankClick = ::openChooseBankScreen,
                        bankSelectionResult = bankSelectionResult,
                        onBankSelectionResultConsumed = {
                            bankSelectionResult = null
                        },
                    )
                }

                entry<AppRoute.CardDetailsScreen> { key ->
                    CardDetailsScreenRoot(
                        card = key.card,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onAddCategoryClick = {
                            backStack.add(
                                AppRoute.AddCategoryScreen
                            )
                        },
                        onEditCategoryClick = { category ->
                            backStack.add(AppRoute.EditCategoryScreen(category))
                        },
                        onEditCardClick = { card ->
                            backStack.add(AppRoute.EditCardScreen(card))
                        },
                        onCardDeleted = {
                            backStack.removeLastOrNull()
                        },
                    )
                }

                entry<AppRoute.ChooseBankScreen> { key ->
                    ChooseBankScreen(
                        selectedBankName = key.selectedBankName,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onBankSelected = { bankName ->
                            publishBankSelectionResult(bankName)
                            backStack.removeLastOrNull()
                        },
                    )
                }
            },
        )
    }
}
