package com.homesharing.cashbackhome.presentation.categories

import com.homesharing.cashbackhome.domain.model.CashbackRuleDraft

internal sealed interface CategoriesScreenState {
    object EmptyScreen: CategoriesScreenState

    object  Loading: CategoriesScreenState

    data class Categories(val categories: List<CashbackRuleDraft>) : CategoriesScreenState
}