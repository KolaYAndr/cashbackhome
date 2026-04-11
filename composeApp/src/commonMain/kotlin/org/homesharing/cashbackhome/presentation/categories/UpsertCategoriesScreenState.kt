package org.homesharing.cashbackhome.presentation.categories

import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

internal data class TextFields(
    val id: Long? = null,
    val category: CashbackRule.CashbackCategory? = null,
    val card: String? = null,
    val date: String? = null,
    val cashback: Int = 5,
    val isSaved: Boolean = false,
    val hasError: Boolean = false,
    val isSaving: Boolean = false,
)

internal sealed interface UpsertCategoriesScreenState {
    data object Loading: UpsertCategoriesScreenState

    data class UpsertCategory(
        val forms: TextFields,
        val cards: List<BankCard>,
        val isEditing: Boolean = false
    ): UpsertCategoriesScreenState
}