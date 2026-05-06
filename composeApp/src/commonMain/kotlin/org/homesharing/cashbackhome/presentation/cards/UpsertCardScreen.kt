package org.homesharing.cashbackhome.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.arrow_drop_down
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.card
import cashbackhome.composeapp.generated.resources.card_type_credit
import cashbackhome.composeapp.generated.resources.card_type_debit
import cashbackhome.composeapp.generated.resources.upsert_card_add_button
import cashbackhome.composeapp.generated.resources.upsert_card_add_title
import cashbackhome.composeapp.generated.resources.upsert_card_bank_label
import cashbackhome.composeapp.generated.resources.upsert_card_bank_placeholder
import cashbackhome.composeapp.generated.resources.upsert_card_edit_title
import cashbackhome.composeapp.generated.resources.upsert_card_name_hint
import cashbackhome.composeapp.generated.resources.upsert_card_name_label
import cashbackhome.composeapp.generated.resources.upsert_card_name_placeholder
import cashbackhome.composeapp.generated.resources.upsert_card_privacy_hint
import cashbackhome.composeapp.generated.resources.upsert_card_save_button
import cashbackhome.composeapp.generated.resources.upsert_card_type_label
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.presentation.categories.SectionLabel
import org.homesharing.cashbackhome.presentation.categories.TipText
import org.homesharing.cashbackhome.presentation.categories.textFieldColors
import org.homesharing.cashbackhome.presentation.home.ChooseOrSaveButton
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.homesharing.cashbackhome.presentation.theme.LightOnBackground
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun AddCardScreenRoot(
    onBackClick: () -> Unit,
    onSavedSuccessfully: () -> Unit,
    onChooseBankClick: (String?) -> Unit,
    bankSelectionResult: BankSelectionResult?,
    onBankSelectionResultConsumed: () -> Unit,
) {
    val viewModel: UpsertCardScreenViewModel = koinViewModel(
        parameters = { parametersOf(null) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(bankSelectionResult?.eventId) {
        bankSelectionResult?.let { result ->
            viewModel.bankSelected(result.bankName)
            onBankSelectionResultConsumed()
        }
    }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is UpsertCardScreenState.UpsertCard -> {
                val state = uiState.value as UpsertCardScreenState.UpsertCard
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> Unit
        }
    }

    when (uiState.value) {
        is UpsertCardScreenState.Loading -> Unit
        is UpsertCardScreenState.UpsertCard -> {
            val state = uiState.value as UpsertCardScreenState.UpsertCard
            UpsertCardScreen(
                state = state,
                onBackClick = onBackClick,
                onChooseBankClick = onChooseBankClick,
                onTitleChanged = viewModel::titleChanged,
                onCardTypeSelected = viewModel::cardTypeSelected,
                onSubmitClick = viewModel::upsertCard,
            )
        }
    }
}

@Composable
internal fun EditCardScreenRoot(
    card: BankCard,
    onBackClick: () -> Unit,
    onSavedSuccessfully: () -> Unit,
    onChooseBankClick: (String?) -> Unit,
    bankSelectionResult: BankSelectionResult?,
    onBankSelectionResultConsumed: () -> Unit,
) {
    val viewModel: UpsertCardScreenViewModel = koinViewModel(
        parameters = { parametersOf(card) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(bankSelectionResult?.eventId) {
        bankSelectionResult?.let { result ->
            viewModel.bankSelected(result.bankName)
            onBankSelectionResultConsumed()
        }
    }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is UpsertCardScreenState.UpsertCard -> {
                val state = uiState.value as UpsertCardScreenState.UpsertCard
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> Unit
        }
    }

    when (uiState.value) {
        is UpsertCardScreenState.Loading -> Unit
        is UpsertCardScreenState.UpsertCard -> {
            val state = uiState.value as UpsertCardScreenState.UpsertCard
            UpsertCardScreen(
                state = state,
                onBackClick = onBackClick,
                onChooseBankClick = onChooseBankClick,
                onTitleChanged = viewModel::titleChanged,
                onCardTypeSelected = viewModel::cardTypeSelected,
                onSubmitClick = viewModel::upsertCard,
            )
        }
    }
}

@Composable
private fun UpsertCardScreen(
    state: UpsertCardScreenState.UpsertCard,
    onBackClick: () -> Unit,
    onChooseBankClick: (String?) -> Unit,
    onTitleChanged: (String) -> Unit,
    onCardTypeSelected: (BankCardType) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        UpsertCardTopBar(
            title = stringResource(
                if (state.isEditing) {
                    Res.string.upsert_card_edit_title
                } else {
                    Res.string.upsert_card_add_title
                }
            ),
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            BankSelectionField(
                value = state.forms.bankName,
                hasError = state.forms.hasError,
                onChooseBankClick = {
                    onChooseBankClick(state.forms.bankName)
                },
            )

            LabeledTextField(
                value = state.forms.title,
                hasError = state.forms.hasError,
                onValueChange = onTitleChanged,
            )

            CardTypeSection(
                selectedCardType = state.forms.cardType,
                onCardTypeSelected = onCardTypeSelected,
            )

            ButtonAndMessage(
                onSubmitClick = onSubmitClick,
                isEditing = state.isEditing,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpsertCardTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = stringResource(Res.string.back_button_description),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun BankSelectionField(
    value: String?,
    hasError: Boolean,
    onChooseBankClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.upsert_card_bank_label))

        ListBankSelectionField(
            value = value,
            hasError = hasError,
            onChooseBankClick = onChooseBankClick,
        )
    }
}

@Composable
private fun ListBankSelectionField(
    value: String?,
    hasError: Boolean,
    onChooseBankClick: () -> Unit,
) {
    val isEmpty = value.isNullOrBlank()
    val displayText = value ?: stringResource(Res.string.upsert_card_bank_placeholder)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onChooseBankClick)
            .padding(horizontal = 13.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isEmpty) {
            TipText(
                text = displayText,
                hasError = hasError,
                isCentered = true
            )
        } else {
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Icon(
            painter = painterResource(Res.drawable.arrow_drop_down),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd),
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun LabeledTextField(
    value: String?,
    hasError: Boolean,
    onValueChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.upsert_card_name_label))

        TipText(
            text = stringResource(Res.string.upsert_card_name_hint),
            hasError = false,
        )

        TextField(
            value = value.orEmpty(),
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                TipText(
                    text = stringResource(Res.string.upsert_card_name_placeholder),
                    hasError = hasError,
                    isCentered = true,
                )
            },
            colors = textFieldColors(),
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )
    }
}

@Composable
private fun CardTypeSection(
    selectedCardType: BankCardType,
    onCardTypeSelected: (BankCardType) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.upsert_card_type_label))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            CardTypeButton(
                label = stringResource(Res.string.card_type_credit),
                selected = selectedCardType == BankCardType.Credit,
                onClick = { onCardTypeSelected(BankCardType.Credit) },
                modifier = Modifier.weight(1f),
            )

            CardTypeButton(
                label = stringResource(Res.string.card_type_debit),
                selected = selectedCardType == BankCardType.Debit,
                onClick = { onCardTypeSelected(BankCardType.Debit) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CardTypeButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = modifier
            .height(40.dp)
            .clip(shape)
            .background(containerColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.card),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = LightOnBackground,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = LightOnBackground,
            maxLines = 1,
        )
    }
}

@Composable
private fun ButtonAndMessage(
    onSubmitClick: () -> Unit,
    isEditing: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ChooseOrSaveButton(
            onClick = onSubmitClick,
            text = if (isEditing) {
                stringResource(Res.string.upsert_card_save_button)
            } else {
                stringResource(Res.string.upsert_card_add_button)
            }
        )

        Text(
            text = stringResource(Res.string.upsert_card_privacy_hint),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
private fun UpsertCardScreenPreview() {
    CashbackHomeTheme {
        UpsertCardScreen(
            state = UpsertCardScreenState.UpsertCard(
                forms = CardsForms(
                    bankName = "T-Банк",
                    title = "Основная карта",
                    cardType = BankCardType.Debit,
                ),
                isEditing = false,
            ),
            onBackClick = {},
            onChooseBankClick = {},
            onTitleChanged = {},
            onCardTypeSelected = {},
            onSubmitClick = {},
        )
    }
}
