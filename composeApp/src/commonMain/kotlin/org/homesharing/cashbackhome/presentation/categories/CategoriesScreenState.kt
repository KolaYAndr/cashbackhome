package org.homesharing.cashbackhome.presentation.categories

import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

internal sealed interface CategoriesScreenState {
    object EmptyScreen: CategoriesScreenState

    object  Loading: CategoriesScreenState

    data class Categories(val categories: List<CashbackRule>) : CategoriesScreenState
}