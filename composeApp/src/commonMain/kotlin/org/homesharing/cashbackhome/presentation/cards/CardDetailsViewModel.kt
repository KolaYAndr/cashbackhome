package org.homesharing.cashbackhome.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

private const val THRESHOLD = 5_000L

internal class CardDetailsViewModel(
    private val applicationScope: CoroutineScope,
    private val repository: CardCashbackRepository,
    initialCard: BankCard,
) : ViewModel() {
    val uiState = combine(
        repository.getAllCards(),
        repository.getAllCashbackRules(),
    ) { cards, cashbacks ->
        val card = cards.firstOrNull { it.cardId == initialCard.cardId } ?: initialCard
        CardDetailsScreenState.Content(
            card = card,
            cashbacks = cashbacks.filter { cashback ->
                cashback.bankCardName == card.bankName
            },
        ) as CardDetailsScreenState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(THRESHOLD),
        initialValue = CardDetailsScreenState.Loading,
    )

    fun deleteCardById(cardId: Long) {
        applicationScope.launch {
            repository.deleteBankCardById(cardId)
        }
    }

    fun deleteCashbackRuleById(ruleId: Long) {
        applicationScope.launch {
            repository.deleteCashbackRuleById(ruleId)
        }
    }
}
