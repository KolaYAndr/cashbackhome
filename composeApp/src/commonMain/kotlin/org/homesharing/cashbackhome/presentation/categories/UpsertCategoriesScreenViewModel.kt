package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

internal class UpsertCategoriesScreenViewModel(
    private val applicationScope: CoroutineScope,
    private val repository: CardCashbackRepository,
    private val category: CashbackRule? = null,
) : ViewModel() {
    private val textFieldsFlow = MutableStateFlow(TextFields())

    init {
        if (category != null) {
            textFieldsFlow.update {
                TextFields(
                    id = category.cashbackRuleId,
                    category = category.category,
                    card = category.bankCardName,
                    date = category.expirationDate,
                    cashback = getValueFromPercent(category.percentage)
                )
            }
        }
    }

    val screenState = combine(
        textFieldsFlow,
        repository.getAllCards()
    ) {textFields, cards ->
        UpsertCategoriesScreenState.UpsertCategory(
            forms = textFields,
            cards = cards
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
            )
        }
    }

    fun cardSelected(card: String) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                card = card,
                hasError = false,
            )
        }
    }

    fun dateSelected(date: String) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                date = date,
                hasError = false
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
            bankCardName = forms.card ?: throw RuntimeException("Card is null"),
            percentage = getPercent(forms.cashback),
            category = forms.category ?: throw RuntimeException("Card is null"),
            maxAmount = 0.0,
            expirationDate = forms.date ?: throw RuntimeException("Card is null")
        )
        applicationScope.launch {
            try {
                repository.upsertCashbackRule(newCashbackRule)
                textFieldsFlow.update {
                    it.copy (
                        isSaving = false,
                        isSaved = true,
                    )
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
            forms.date == null ||
            forms.card == null
        ) {
            return true
        }
        return false
    }

    private fun getPercent(value: Int) = value.toDouble() / 100

    private fun getValueFromPercent(percent: Double) = (percent * 100).toInt()
}