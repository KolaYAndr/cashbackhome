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
    private val errorFlow = MutableStateFlow(false)

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
        errorFlow,
        repository.getAllCards()
    ) {textFields, error, cards ->
        UpsertCategoriesScreenState.Ready(
            forms = textFields,
            error = error,
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
                category = category
            )
        }
    }

    fun cardSelected(card: String) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                card = card
            )
        }
    }

    fun dateSelected(date: String) {
        textFieldsFlow.update { textFields ->
            textFields.copy(
                date = date
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
        val forms = (screenState.value as UpsertCategoriesScreenState.Ready).forms
        if (checkNull(forms)) {
            errorFlow.update {
                true
            }
            return
        }

        val newCashbackRule = CashbackRule(
            bankCardName = forms.card ?: throw RuntimeException("Card is null"),
            percentage = getPercent(forms.cashback),
            category = forms.category ?: throw RuntimeException("Card is null"),
            maxAmount = 0.0,
            expirationDate = forms.date ?: throw RuntimeException("Card is null")
        )
        applicationScope.launch {
            repository.upsertCashbackRule(newCashbackRule)
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