package org.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.add_category_add_card
import cashbackhome.composeapp.generated.resources.add_category_card_label
import cashbackhome.composeapp.generated.resources.add_category_card_placeholder
import cashbackhome.composeapp.generated.resources.add_category_cashback_label
import cashbackhome.composeapp.generated.resources.add_category_category_label
import cashbackhome.composeapp.generated.resources.add_category_category_placeholder
import cashbackhome.composeapp.generated.resources.add_category_duplicate_hint
import cashbackhome.composeapp.generated.resources.add_category_expiration_label
import cashbackhome.composeapp.generated.resources.add_category_expiration_placeholder
import cashbackhome.composeapp.generated.resources.add_category_no_card
import cashbackhome.composeapp.generated.resources.add_category_submit_button
import cashbackhome.composeapp.generated.resources.add_category_title
import cashbackhome.composeapp.generated.resources.add_category_unlimited
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.arrow_drop_down
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.calendar
import cashbackhome.composeapp.generated.resources.edit_category_submit_button
import cashbackhome.composeapp.generated.resources.edit_category_title
import co.touchlab.kermit.Logger
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule.CashbackCategory
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val MinCashbackPercent = 1
private const val MaxCashbackPercent = 100

@Composable
internal fun EditCategoryScreenRoot(
    category: CashbackRule,
    onBackClick: () -> Unit,
    onSavedSuccessfully: () -> Unit,
    onAddCardClick: () -> Unit,
) {
    val viewModel = koinViewModel<UpsertCategoriesScreenViewModel>(
        parameters = { parametersOf(category) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        Logger.i { "LaunchedEffect" }
        when (uiState.value) {
            is UpsertCategoriesScreenState.UpsertCategory -> {
                val state = uiState.value as UpsertCategoriesScreenState.UpsertCategory
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> {

            }
        }
    }

    when(uiState.value) {
        is UpsertCategoriesScreenState.Loading -> {
            LoadingScreen()
        }
        else -> {
            val state = uiState.value as UpsertCategoriesScreenState.UpsertCategory
            AddCategoryScreen(
                state = state,
                onAddCategoryClick = viewModel::upsertRule,
                onBackClick = onBackClick,
                onAddCardClick = onAddCardClick,
                onCategorySelected = viewModel::categorySelected,
                onCardSelected = viewModel::cardSelected,
                onDateType = viewModel::dateSelected,
                onPressCashback = viewModel::cashbackSelected,
            )
        }
    }
}

@Composable
internal fun AddCategoryScreenRoot(
    onSavedSuccessfully: () -> Unit,
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit,
) {
    val viewModel = koinViewModel<UpsertCategoriesScreenViewModel>(
        parameters = { parametersOf(null) }
    )
    val uiState = viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value) {
        Logger.i { "LaunchedEffect" }
        when (uiState.value) {
            is UpsertCategoriesScreenState.UpsertCategory -> {
                val state = uiState.value as UpsertCategoriesScreenState.UpsertCategory
                if (state.forms.isSaved) {
                    onSavedSuccessfully()
                }
            }
            else -> {

            }
        }
    }

    when(uiState.value) {
        is UpsertCategoriesScreenState.Loading -> {
            LoadingScreen()
        }
        else -> {
            val state = uiState.value as UpsertCategoriesScreenState.UpsertCategory
            AddCategoryScreen(
                state = state,
                onAddCategoryClick = viewModel::upsertRule,
                onBackClick = onBackClick,
                onAddCardClick = onAddCardClick,
                onCategorySelected = viewModel::categorySelected,
                onCardSelected = viewModel::cardSelected,
                onDateType = viewModel::dateSelected,
                onPressCashback = viewModel::cashbackSelected,
            )
        }
    }
}

@Composable
private fun AddCategoryScreen(
    state: UpsertCategoriesScreenState.UpsertCategory,
    onAddCategoryClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onCategorySelected: (CashbackCategory) -> Unit,
    onCardSelected: (String) -> Unit,
    onDateType: (String) -> Unit,
    onPressCashback: (Int) -> Unit,
) {
    val selectedCategory = state.forms.category
    val cards = state.cards
    val selectedCard = state.forms.card
    val expirationDate = state.forms.date
    var isUnlimited by rememberSaveable { mutableStateOf(false) }
    val cashbackPercent = state.forms.cashback
    val categoryOptions = CashbackCategory.all.map {
        it to categoryName(it)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AddCategoryTopBar(
            title = if (state.isEditing) {
                stringResource(Res.string.edit_category_title)
            } else {
                stringResource(Res.string.add_category_title)
            },
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CategorySelectionFieldSection(
                hasError = state.forms.hasError,
                selectedCategory = selectedCategory,
                options = categoryOptions,
                onCategorySelected = onCategorySelected,
            )

            //Card
            if (cards.isEmpty()) {
                NoCards(
                    hasError = state.forms.hasError,
                    onAddCardClick = onAddCardClick
                )
            } else {
                CardSelectionFieldSection(
                    hasError = state.forms.hasError,
                    selectedCard = selectedCard,
                    options = cards,
                    onCardSelected = onCardSelected,
                )
            }

            ChooseDate(
                hasError = state.forms.hasError,
                expirationDate = expirationDate,
                onDateType = onDateType,
                isUnlimited = isUnlimited,
                onCheck = { checkBoxState ->
                    isUnlimited = checkBoxState
                    if (checkBoxState) {
                        onDateType(" ")
                    } else {
                        onDateType("")
                    }
                }
            )

            ChooseCashback(
                cashbackPercent = cashbackPercent,
                onPressCashback = onPressCashback,
            )

            //duplicate found
            Text(
                text = stringResource(Res.string.add_category_duplicate_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = onAddCategoryClick,
                modifier = Modifier
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
                    text = if (state.isEditing) {
                        stringResource(Res.string.edit_category_submit_button)
                    } else {
                        stringResource(Res.string.add_category_submit_button)
                    },
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCategoryTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
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
internal fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
internal fun DefaultTextInBox(
    text: String,
    hasError: Boolean
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        color = if (hasError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectionFieldSection(
    hasError: Boolean,
    selectedCategory: CashbackCategory?,
    options: List<Pair<CashbackCategory, String>>,
    onCategorySelected: (CashbackCategory) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.add_category_category_label))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = categoryName(selectedCategory),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    DefaultTextInBox(
                        stringResource(Res.string.add_category_category_placeholder),
                        hasError
                    )
                },
                colors = textFieldColors(),
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

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.second,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onCategorySelected(option.first)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun NoCards(
    hasError: Boolean,
    onAddCardClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        SectionLabel(stringResource(Res.string.add_category_card_label))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultTextInBox(stringResource(Res.string.add_category_no_card), hasError)

            Button(
                shape = RoundedCornerShape(14.dp),
                onClick = onAddCardClick,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = stringResource(Res.string.add_category_add_card),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardSelectionFieldSection(
    hasError: Boolean,
    selectedCard: String?,
    options: List<BankCard>,
    onCardSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(stringResource(Res.string.add_category_card_label))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedCard.orEmpty(),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    DefaultTextInBox(
                        stringResource(Res.string.add_category_card_placeholder),
                        hasError
                    )
                },
                colors = textFieldColors(),
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

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.bankName,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onCardSelected(option.bankName)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChooseDate(
    hasError: Boolean,
    expirationDate: String?,
    onDateType: (String) -> Unit,
    isUnlimited: Boolean,
    onCheck: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.add_category_expiration_label),
        )

        TextField(
            value = expirationDate.orEmpty(),
            onValueChange = onDateType,
            placeholder = {
                DefaultTextInBox(
                    stringResource(Res.string.add_category_expiration_placeholder),
                    hasError
                )
            },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUnlimited,
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isUnlimited) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            },
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                Checkbox(
                    checked = isUnlimited,
                    onCheckedChange = onCheck,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
            Text(
                text = stringResource(Res.string.add_category_unlimited),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ChooseCashback(
    cashbackPercent: Int,
    onPressCashback: (Int) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(stringResource(Res.string.add_category_cashback_label))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(19.dp),
        ) {
            val minusCashbackPercent =
                (cashbackPercent - 1).coerceAtLeast(MinCashbackPercent)
            StepperButton(
                label = "-",
                onClick = { onPressCashback(minusCashbackPercent) },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$cashbackPercent%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val plusCashbackPercent =
                (cashbackPercent + 1).coerceAtMost(MaxCashbackPercent)
            StepperButton(
                label = "+",
                onClick = { onPressCashback(plusCashbackPercent) },
            )
        }
    }
}

@Composable
private fun StepperButton(
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
internal fun textFieldColors() = TextFieldDefaults.colors(
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
fun PreviewAddCategoryScreenLight() {
    CashbackHomeTheme {
        AddCategoryScreenRoot(onSavedSuccessfully = {}, onBackClick = {}, onAddCardClick = {} )
    }
}
