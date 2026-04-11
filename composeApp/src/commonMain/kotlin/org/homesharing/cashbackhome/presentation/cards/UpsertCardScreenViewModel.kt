package org.homesharing.cashbackhome.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

internal class UpsertCardScreenViewModel(
    private val repository: CardCashbackRepository,
    card: BankCard? = null,
) : ViewModel() {
    private val _formsFlow = MutableStateFlow(CardsForms())
    val screenState = combine(
        _formsFlow,
        MutableStateFlow(card.toScreenState()),
    ) {forms, card ->
        if (forms == CardsForms()) {
            card
        } else {
            UpsertCardScreenState.UpsertCard(forms, card.isEditing)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        UpsertCardScreenState.Loading
    )

    fun bankNameChanged(bankName: String) {
        _formsFlow.update {
            it.copy(
                bankName = bankName,
                hasError = false,
            )
        }
    }

    fun titleChanged(title: String) {
        _formsFlow.update {
            it.copy(
                title = title,
                hasError = false,
            )
        }
    }

    fun cardTypeSelected(cardType: BankCardType) {
        _formsFlow.update {
            it.copy(cardType = cardType)
        }
    }

    fun upsertCard() {
        val state = screenState.value as UpsertCardScreenState.UpsertCard
        if (state.forms.title.isNullOrBlank() || state.forms.bankName == null) {
            _formsFlow.update { it.copy(hasError = true) }
            return
        }

        _formsFlow.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try {
                repository.upsertBankCard(
                    BankCard(
                        cardId = state.forms.cardId,
                        bankName = state.forms.bankName.trim(),
                        title = state.forms.title.trim(),
                        cardType = state.forms.cardType,
                    )
                )
                _formsFlow.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                    )
                }
            } catch (_: Exception) {
                _formsFlow.update {
                    it.copy(
                        isSaving = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    private fun BankCard?.toScreenState(): UpsertCardScreenState.UpsertCard {
        return if (this == null) {
            UpsertCardScreenState.UpsertCard(CardsForms(), false)
        } else {
            val cardForm = CardsForms(
                cardId = cardId,
                bankName = bankName,
                title = title,
                cardType = when (cardType) {
                    BankCardType.Credit -> BankCardType.Credit
                    else -> BankCardType.Debit
                },
            )
            _formsFlow.update { cardForm }
            UpsertCardScreenState.UpsertCard(
                forms = cardForm,
                isEditing = true
            )
        }
    }
}
