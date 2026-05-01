package org.homesharing.cashbackhome.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

@Serializable
internal sealed class AppRoute : NavKey {
    @Serializable
    data object Home : AppRoute()

    @Serializable
    data object AddCategoryScreen : AppRoute()

    @Serializable
    data class EditCategoryScreen(val category: CashbackRule): AppRoute()

    @Serializable
    data object AddCardScreen : AppRoute()

    @Serializable
    data class EditCardScreen(val card: BankCard) : AppRoute()

    @Serializable
    data class CardDetailsScreen(val card: BankCard) : AppRoute()

    @Serializable
    data class ChooseBankScreen(
        val selectedBankName: String? = null,
    ) : AppRoute()

    @Serializable
    data object PersonalCabinet : AppRoute()
}
