package org.homesharing.cashbackhome.presentation.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.model.BankCardDraft
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

private const val THRESHOLD = 5000L

internal class AddCardWithCashbacksViewModel(
    private val repository: CardCashbackRepository
) : ViewModel() {

    val state = repository.getAllCards().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(THRESHOLD),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(AddCardWithCashbacksUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: AddCardIntent) {
        when (intent) {
            is AddCardIntent.ExistingCardSelected -> handleExistingCardSelected(intent.cardId)
            is AddCardIntent.SwitchToNewCard -> handleSwitchToNewCard()
            is AddCardIntent.CardDraftChanged -> handleCardDraftChange(intent.cardDraft)
            is AddCardIntent.CardTypeChanged -> handleCardTypeChange(intent.cardType)
            is AddCardIntent.AddCashbackDraft -> handleAddCashbackDraft()
            is AddCardIntent.CashbackDraftChanged -> handleCashbackDraftChange(
                intent.index,
                intent.draft
            )

            is AddCardIntent.RemoveCashbackDraft -> handleRemoveCashbackDraft(intent.index)
            is AddCardIntent.SaveClicked -> handleSaveClicked()
            is AddCardIntent.ErrorShown -> handleErrorShown()
        }
    }

    private fun handleExistingCardSelected(cardId: Long?) {
        _uiState.update {
            it.copy(
                selectedCardId = cardId,
                isCreatingNewCard = cardId == null
            )
        }
    }

    private fun handleSwitchToNewCard() {
        _uiState.update {
            it.copy(
                selectedCardId = null,
                isCreatingNewCard = true
            )
        }
    }

    private fun handleCardDraftChange(cardDraft: BankCardDraft) {
        _uiState.update { it.copy(cardDraft = cardDraft) }
    }

    private fun handleCardTypeChange(cardType: BankCardType) {
        _uiState.update { it.copy(cardDraft = it.cardDraft.copy(cardType = cardType)) }
    }

    private fun handleAddCashbackDraft() {
        _uiState.update {
            it.copy(cashbackDrafts = it.cashbackDrafts + CashbackRuleDraft())
        }
    }

    private fun handleCashbackDraftChange(index: Int, draft: CashbackRuleDraft) {
        _uiState.update { state ->
            state.copy(cashbackDrafts = state.cashbackDrafts.toMutableList().apply {
                this[index] = draft
            })
        }
    }

    private fun handleRemoveCashbackDraft(index: Int) {
        _uiState.update { state ->
            state.copy(cashbackDrafts = state.cashbackDrafts.toMutableList().apply {
                removeAt(index)
            })
        }
    }

    private fun handleSaveClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                if (uiState.value.isCreatingNewCard) {
                    val newCard = BankCard(
                        bankName = uiState.value.cardDraft.bankName,
                        title = uiState.value.cardDraft.title,
                        cardType = uiState.value.cardDraft.cardType,
                    )
                    repository.upsertBankCard(newCard)
                    val newCardId = repository.getAllCards().first().last().cardId

                    uiState.value.cashbackDrafts.forEach { draft ->
                        val rule = CashbackRule(
                            percentage = draft.percentage,
                            category = draft.category,
                            maxAmount = draft.maxAmount,
                            startDate = draft.startDate,
                            expirationDate = draft.expirationDate,
                            bankCardName = "",
                            bankCardId = 0L
                        )
                        repository.upsertCashbackRule(rule)
                        val newRuleId =
                            repository.getAllCashbackRules().first().last().cashbackRuleId
                        repository.linkCardToRule(newCardId, newRuleId)
                    }
                } else {
                    uiState.value.selectedCardId?.let { cardId ->
                        uiState.value.cashbackDrafts.forEach { draft ->
                            val rule = CashbackRule(
                                percentage = draft.percentage,
                                category = draft.category,
                                maxAmount = draft.maxAmount,
                                startDate = draft.startDate,
                                expirationDate = draft.expirationDate,
                                bankCardName = "",
                                bankCardId = 0L
                            )
                            repository.upsertCashbackRule(rule)
                            val newRuleId =
                                repository.getAllCashbackRules().first().last().cashbackRuleId
                            repository.linkCardToRule(cardId, newRuleId)
                        }
                    }
                }
                _uiState.update { it.copy(isSaving = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, errorMessage = e.message) }
            }
        }
    }

    private fun handleErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}


sealed interface AddCardIntent {
    data class ExistingCardSelected(val cardId: Long?) : AddCardIntent
    data object SwitchToNewCard : AddCardIntent
    data class CardDraftChanged(val cardDraft: BankCardDraft) : AddCardIntent
    data class CardTypeChanged(val cardType: BankCardType) : AddCardIntent
    data object AddCashbackDraft : AddCardIntent
    data class CashbackDraftChanged(val index: Int, val draft: CashbackRuleDraft) : AddCardIntent
    data class RemoveCashbackDraft(val index: Int) : AddCardIntent
    data object SaveClicked : AddCardIntent
    data object ErrorShown : AddCardIntent
}

data class AddCardWithCashbacksUiState(
    val selectedCardId: Long? = null,
    val isCreatingNewCard: Boolean = true,
    val cardDraft: BankCardDraft = BankCardDraft(),
    val cashbackDrafts: List<CashbackRuleDraft> = emptyList(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)
