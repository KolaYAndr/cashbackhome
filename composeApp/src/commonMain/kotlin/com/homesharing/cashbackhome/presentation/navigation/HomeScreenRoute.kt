package com.homesharing.cashbackhome.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed interface HomeScreenRoute : NavKey {
    @Serializable
    data object CategoriesScreen : HomeScreenRoute

    @Serializable
    data object PromotionsScreen : HomeScreenRoute

    @Serializable
    data object MyCardsScreen : HomeScreenRoute
}
