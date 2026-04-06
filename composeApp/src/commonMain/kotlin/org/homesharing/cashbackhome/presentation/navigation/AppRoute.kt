package org.homesharing.cashbackhome.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
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
    data object AddCardWithCashbacks : AppRoute()

    @Serializable
    data object EditCardWithCashback : AppRoute()

    @Serializable
    data object PersonalCabinet : AppRoute()
}
