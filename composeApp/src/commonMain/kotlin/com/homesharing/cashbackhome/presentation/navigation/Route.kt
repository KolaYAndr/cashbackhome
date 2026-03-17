package com.homesharing.cashbackhome.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface Route : NavKey {

    @Serializable
    data object Categories : Route

    @Serializable
    data object Cards : Route

    @Serializable
    data object Promotions : Route

    @Serializable
    data object AddCardWithCashbacks : Route
}
