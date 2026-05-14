package org.homesharing.cashbackhome.presentation.categories

import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

internal data class TextFields(
    val id: Long? = null,
    val category: CashbackRule.CashbackCategory? = null,
    val card: BankCard? = null,
    val startDate: String? = null,
    val expirationDate: String? = null,
    val isUnlimited: Boolean = false,
    val cashback: Int = 5,
    val isSaved: Boolean = false,
    val hasError: Boolean = false,
    val isSaving: Boolean = false,
    val isDuplicate: Boolean = false,
)

internal sealed interface UpsertCategoriesScreenState {
    data object Loading: UpsertCategoriesScreenState

    data class UpsertCategory(
        val forms: TextFields,
        val cards: List<BankCard>,
        val isEditing: Boolean = false
    ): UpsertCategoriesScreenState
}
