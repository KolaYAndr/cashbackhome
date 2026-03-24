package org.homesharing.cashbackhome.presentation.categories

import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft

internal sealed interface CategoriesScreenState {
    object EmptyScreen: CategoriesScreenState

    object  Loading: CategoriesScreenState

    data class Categories(val categories: List<CashbackRuleDraft>) : CategoriesScreenState
}