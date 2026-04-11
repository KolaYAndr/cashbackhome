package org.homesharing.cashbackhome.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import cashbackhome.composeapp.generated.resources.upsert_card_name_label
import cashbackhome.composeapp.generated.resources.upsert_card_name_placeholder
import cashbackhome.composeapp.generated.resources.upsert_card_privacy_hint
import cashbackhome.composeapp.generated.resources.upsert_card_save_button
import cashbackhome.composeapp.generated.resources.upsert_card_type_label
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.presentation.categories.DefaultTextInBox
import org.homesharing.cashbackhome.presentation.categories.SectionLabel
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val BankOptions = listOf(
    "Т-Банк",
    "Сбер",
    "Альфа-Банк",
    "ВТБ",
    "Газпромбанк",
    "Совкомбанк",
    "Ozon Банк",
    "Райффайзен Банк",
)

@Composable
internal fun AddCardScreenRoot(
    onBackClick: () -> Unit,
    onSavedSuccessfully: () -> Unit,
) {
    val viewModel: UpsertCardScreenViewModel = koinViewModel(
        parameters = { parametersOf(null) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is UpsertCardScreenState.UpsertCard -> {
                val state = uiState.value as UpsertCardScreenState.UpsertCard
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> {

            }
        }
    }

    when (uiState.value) {
        is UpsertCardScreenState.Loading -> {

        }
        is UpsertCardScreenState.UpsertCard -> {
            val state = uiState.value as UpsertCardScreenState.UpsertCard
            UpsertCardScreen(
                state = state,
                onBackClick = onBackClick,
                onBankNameChanged = viewModel::bankNameChanged,
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
) {
    val viewModel: UpsertCardScreenViewModel = koinViewModel(
        parameters = { parametersOf(card) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is UpsertCardScreenState.UpsertCard -> {
                val state = uiState.value as UpsertCardScreenState.UpsertCard
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> {

            }
        }
    }

    when (uiState.value) {
        is UpsertCardScreenState.Loading -> {

        }
        is UpsertCardScreenState.UpsertCard -> {
            val state = uiState.value as UpsertCardScreenState.UpsertCard
            UpsertCardScreen(
                state = state,
                onBackClick = onBackClick,
                onBankNameChanged = viewModel::bankNameChanged,
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
    onBankNameChanged: (String) -> Unit,
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
                options = BankOptions,
                onValueChange = onBankNameChanged,
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
                isSaving = state.forms.isSaving,
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
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BankSelectionField(
    value: String?,
    hasError: Boolean,
    options: List<String>,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.upsert_card_bank_label))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = value.orEmpty(),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    DefaultTextInBox(
                        text = stringResource(Res.string.upsert_card_bank_placeholder),
                        hasError = hasError,
                    )
                },
                colors = fieldColors(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_drop_down),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                },
                shape = RoundedCornerShape(14.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 15.dp)
                    )
                }
            }
        }
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

        DefaultTextInBox(text = stringResource(Res.string.upsert_card_privacy_hint), false)

        TextField(
            value = value.orEmpty(),
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                DefaultTextInBox(
                    text = stringResource(Res.string.upsert_card_name_placeholder),
                    hasError = hasError,
                )
            },
            colors = fieldColors(),
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
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
        MaterialTheme.colorScheme.background
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
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
            tint = contentColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ButtonAndMessage(
    onSubmitClick: () -> Unit,
    isSaving: Boolean,
    isEditing: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onSubmitClick,
            enabled = !isSaving,
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .fillMaxWidth()
                .padding(vertical = 15.dp)
                .height(55.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
        ) {
            Text(
                text = stringResource(
                    if (isEditing) {
                        Res.string.upsert_card_save_button
                    } else {
                        Res.string.upsert_card_add_button
                    }
                ),
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Text(
            text = stringResource(Res.string.upsert_card_privacy_hint),
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun fieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
)

@Composable
@Preview
private fun UpsertCardScreenPreview() {
    CashbackHomeTheme {
        UpsertCardScreen(
            state = UpsertCardScreenState.UpsertCard(
                forms = CardsForms(
                    bankName = "Т-Банк",
                    title = "Основная карта",
                    cardType = BankCardType.Debit,
                ),
                isEditing = false
            ),
            onBackClick = {},
            onBankNameChanged = {},
            onTitleChanged = {},
            onCardTypeSelected = {},
            onSubmitClick = {},
        )
    }
}
