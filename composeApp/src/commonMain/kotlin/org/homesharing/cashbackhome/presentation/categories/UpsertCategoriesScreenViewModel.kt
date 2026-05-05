package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

internal class UpsertCategoriesScreenViewModel(
    private val repository: CardCashbackRepository,
    private val category: CashbackRule? = null,
) : ViewModel() {
    private val textFieldsFlow = MutableStateFlow(TextFields())

    init {
        if (category != null) {
            viewModelScope.launch {
                val bankCard = repository.getCard(category.bankCardId)
                textFieldsFlow.update {
                    TextFields(
                        id = category.cashbackRuleId,
                        category = category.category,
                        card = bankCard,
                        startDate = category.startDate,
                        expirationDate = category.expirationDate,
                        isUnlimited = category.expirationDate.isBlank(),
                        cashback = getValueFromPercent(category.percentage)
                    )
                }
            }
        }
    }

    val screenState = combine(
        textFieldsFlow,
        repository.getAllCards()
    ) {textFields, cards ->
        UpsertCategoriesScreenState.UpsertCategory(
            forms = textFields,
            cards = cards,
            isEditing = category != null,
        ) as UpsertCategoriesScreenState
    }
        .onStart { emit(UpsertCategoriesScreenState.Loading) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            UpsertCategoriesScreenState.Loading
        )

    fun categorySelected(category: CashbackRule.CashbackCategory) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                category = category,
                hasError = false,
                isDuplicate = false,
            )
        }
    }

    fun cardSelected(card: BankCard) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                card = card,
                hasError = false,
            )
        }
    }

    fun dateRangeSelected(
        startDate: String,
        expirationDate: String,
    ) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                startDate = startDate,
                expirationDate = expirationDate,
                isUnlimited = false,
                hasError = false
            )
        }
    }

    fun dateUnlimitedChanged(isUnlimited: Boolean) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                startDate = if (isUnlimited) " " else null,
                expirationDate = if (isUnlimited) " " else null,
                isUnlimited = isUnlimited,
                hasError = false,
            )
        }
    }

    fun cashbackSelected(cashback: Int) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                cashback = cashback
            )
        }
    }

    fun clearError() {
        textFieldsFlow.update {
            it.copy(
                hasError = false
            )
        }
    }

    fun upsertRule() {
        val forms = (screenState.value as UpsertCategoriesScreenState.UpsertCategory).forms
        if (checkNull(forms)) {
            textFieldsFlow.update {
                it.copy(hasError = true)
            }
            return
        }

        textFieldsFlow.update { it.copy(isSaving = true)}
        val newCashbackRule = CashbackRule(
            cashbackRuleId = forms.id ?: 0,
            bankCardId = forms.card?.cardId ?: throw RuntimeException("Card is null"),
            bankCardName = forms.card.bankName,
            percentage = getPercent(forms.cashback),
            category = forms.category ?: throw RuntimeException("Card is null"),
            maxAmount = 0.0,
            startDate = if (forms.isUnlimited) {
                " "
            } else {
                forms.startDate ?: throw RuntimeException("Start date is null")
            },
            expirationDate = if (forms.isUnlimited) {
                " "
            } else {
                forms.expirationDate ?: throw RuntimeException("Expiration date is null")
            }
        )
        viewModelScope.launch {
            try {
                when (val result = repository.upsertCashbackRule(newCashbackRule)) {
                    is SavedCategoryResult.Success -> {
                        val ruleId = result.id
                        repository.linkCardToRule(forms.card.cardId, ruleId)
                        textFieldsFlow.update {
                            it.copy (
                                isSaving = false,
                                isSaved = true,
                            )
                        }
                    }
                    is SavedCategoryResult.Duplicate -> {
                        textFieldsFlow.update {
                            it.copy (
                                isDuplicate = true
                            )
                        }
                    }
                    is SavedCategoryResult.Error -> {
                        textFieldsFlow.update {
                            it.copy (
                                hasError = true
                            )
                        }
                    }
                }
            }
            catch(_: Exception) {
                textFieldsFlow.update{
                    it.copy(
                        isSaving = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    private fun checkNull(forms: TextFields): Boolean {
        if (
            forms.category == null ||
            (
                !forms.isUnlimited &&
                    (forms.startDate.isNullOrBlank() || forms.expirationDate.isNullOrBlank())
                ) ||
            forms.card == null
        ) {
            return true
        }
        return false
    }

    private fun getPercent(value: Int) = value.toDouble() / 100

    private fun getValueFromPercent(percent: Double) = (percent * 100).toInt()
}

internal sealed interface SavedCategoryResult {
    data class Success(val id: Long): SavedCategoryResult
    data object Error: SavedCategoryResult
    data object Duplicate: SavedCategoryResult
}
