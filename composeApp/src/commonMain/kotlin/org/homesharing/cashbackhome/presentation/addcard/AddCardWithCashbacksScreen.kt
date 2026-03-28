package org.homesharing.cashbackhome.presentation.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.add_card
import cashbackhome.composeapp.generated.resources.add_card_cashback_title
import cashbackhome.composeapp.generated.resources.add_cashback_rule_button
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.bank_name_field_label
import cashbackhome.composeapp.generated.resources.card
import cashbackhome.composeapp.generated.resources.card_bank_label
import cashbackhome.composeapp.generated.resources.card_mask_field_label
import cashbackhome.composeapp.generated.resources.card_mask_label
import cashbackhome.composeapp.generated.resources.cashback_rules_section_title
import cashbackhome.composeapp.generated.resources.new_card_dropdown_item
import cashbackhome.composeapp.generated.resources.remove_rule_button
import cashbackhome.composeapp.generated.resources.rule_category_field_label
import cashbackhome.composeapp.generated.resources.rule_percentage_field_label
import cashbackhome.composeapp.generated.resources.rule_title_field_label
import cashbackhome.composeapp.generated.resources.save_button
import cashbackhome.composeapp.generated.resources.selected_card_label
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.domain.model.BankCardDraft
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun AddCardWithCashbacksScreen(
    viewModel: AddCardWithCashbacksViewModel = koinInject(),
    onBackClick: () -> Unit,
    onSavedSuccessfully: () -> Unit,
) {
    val existingCards by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddCardWithCashbacksScreenContent(
        uiState = uiState,
        existingCards = existingCards,
        onIntent = viewModel::onIntent,
        onSaveClick = {
            viewModel.onIntent(AddCardIntent.SaveClicked)
            onSavedSuccessfully()
        },
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCardWithCashbacksScreenContent(
    uiState: AddCardWithCashbacksUiState,
    existingCards: List<BankCard>,
    onIntent: (AddCardIntent) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.add_card_cashback_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = stringResource(Res.string.back_button_description)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    readOnly = true,
                    value = if (uiState.isCreatingNewCard) {
                        stringResource(Res.string.new_card_dropdown_item)
                    } else {
                        existingCards
                            .find { it.cardId == uiState.selectedCardId }
                            ?.let { "${it.bankName} - ${it.mask}" }.orEmpty()
                    },
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.selected_card_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.add_card),
                                contentDescription = null,
                                modifier = Modifier.background(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        },
                        text = { Text(stringResource(Res.string.new_card_dropdown_item)) },
                        onClick = {
                            onIntent(AddCardIntent.SwitchToNewCard)
                            expanded = false
                        }
                    )
                    existingCards.forEach { card ->
                        DropdownMenuItem(
                            text = { Text("${card.bankName} - ${card.mask}") },
                            onClick = {
                                onIntent(AddCardIntent.ExistingCardSelected(card.cardId))
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.card),
                                    contentDescription = null,
                                    modifier = Modifier.background(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }
                        )
                    }
                }
            }

            if (uiState.isCreatingNewCard) {
                CardDraftForm(uiState.cardDraft) { onIntent(AddCardIntent.CardDraftChanged(it)) }
            } else {
                uiState.selectedCardId?.let { cardId ->
                    existingCards.find { it.cardId == cardId }?.let { card ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(stringResource(Res.string.card_bank_label, card.bankName))
                                Text(stringResource(Res.string.card_mask_label, card.mask))
                            }
                        }
                    }
                }
            }

            Text(
                text = stringResource(Res.string.cashback_rules_section_title),
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp
                )
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(uiState.cashbackDrafts) { index, draft ->
                    CashbackRuleDraftItem(
                        draft = draft,
                        onRemove = { onIntent(AddCardIntent.RemoveCashbackDraft(index)) },
                        onDraftChange = { updatedDraft ->
                            onIntent(
                                AddCardIntent.CashbackDraftChanged(
                                    index,
                                    updatedDraft
                                )
                            )
                        }
                    )
                }
                item {
                    Button(
                        onClick = { onIntent(AddCardIntent.AddCashbackDraft) }
                    ) {
                        Text(stringResource(Res.string.add_cashback_rule_button))
                    }
                }
            }

            Button(
                onClick = onSaveClick,
                enabled = !uiState.isSaving && (if (uiState.isCreatingNewCard) uiState.cardDraft.bankName.isNotBlank() else uiState.selectedCardId != null),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator()
                } else {
                    Text(stringResource(Res.string.save_button))
                }
            }

            uiState.errorMessage?.let {
                LaunchedEffect(it) {
                    snackbarHostState.showSnackbar(it)
                    onIntent(AddCardIntent.ErrorShown)
                }
            }
        }
    }
}

@Composable
private fun CardDraftForm(
    draft: BankCardDraft,
    onDraftChange: (BankCardDraft) -> Unit
) {
    Column {
        TextField(
            value = draft.bankName,
            onValueChange = { onDraftChange(draft.copy(bankName = it)) },
            label = { Text(stringResource(Res.string.bank_name_field_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = draft.mask,
            onValueChange = { onDraftChange(draft.copy(mask = it)) },
            label = { Text(stringResource(Res.string.card_mask_field_label)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CashbackRuleDraftItem(
    draft: CashbackRuleDraft,
    onRemove: () -> Unit,
    onDraftChange: (CashbackRuleDraft) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = remember { CashbackRuleDraft.CashbackCategory.all }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            value = draft.percentage.toString(),
            onValueChange = { onDraftChange(draft.copy(percentage = it.toDoubleOrNull() ?: 0.0)) },
            label = { Text(stringResource(Res.string.rule_percentage_field_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                value = categoryName(draft.category),
                onValueChange = {},
                label = { Text(stringResource(Res.string.rule_category_field_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(categoryName(category)) },
                        onClick = {
                            onDraftChange(draft.copy(category = category))
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = onRemove,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(Res.string.remove_rule_button))
        }
    }
}
