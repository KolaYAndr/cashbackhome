package org.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.add_category_add_card
import cashbackhome.composeapp.generated.resources.add_category_card_label
import cashbackhome.composeapp.generated.resources.add_category_card_placeholder
import cashbackhome.composeapp.generated.resources.add_category_cashback_label
import cashbackhome.composeapp.generated.resources.add_category_category_label
import cashbackhome.composeapp.generated.resources.add_category_category_placeholder
import cashbackhome.composeapp.generated.resources.add_category_date_picker_cancel_button
import cashbackhome.composeapp.generated.resources.add_category_date_picker_clear_button
import cashbackhome.composeapp.generated.resources.add_category_date_picker_label
import cashbackhome.composeapp.generated.resources.add_category_date_picker_save_button
import cashbackhome.composeapp.generated.resources.add_category_date_picker_title
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule.CashbackCategory
import org.homesharing.cashbackhome.presentation.home.ChooseOrSaveButton
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Instant

private const val MinCashbackPercent = 1
private const val MaxCashbackPercent = 100

private data class DateRangeSelection(
    val startDate: LocalDate,
    val endDate: LocalDate,
)

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
            UpsertCategoryScreen(
                state = state,
                onUpsertCategoryClick = viewModel::upsertRule,
                onBackClick = onBackClick,
                onAddCardClick = onAddCardClick,
                onCategorySelected = viewModel::categorySelected,
                onCardSelected = viewModel::cardSelected,
                onDateRangeSelected = viewModel::dateRangeSelected,
                onUnlimitedDateChanged = viewModel::dateUnlimitedChanged,
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
            UpsertCategoryScreen(
                state = state,
                onUpsertCategoryClick = viewModel::upsertRule,
                onBackClick = onBackClick,
                onAddCardClick = onAddCardClick,
                onCategorySelected = viewModel::categorySelected,
                onCardSelected = viewModel::cardSelected,
                onDateRangeSelected = viewModel::dateRangeSelected,
                onUnlimitedDateChanged = viewModel::dateUnlimitedChanged,
                onPressCashback = viewModel::cashbackSelected,
            )
        }
    }
}

@Composable
private fun UpsertCategoryScreen(
    state: UpsertCategoriesScreenState.UpsertCategory,
    onUpsertCategoryClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onCategorySelected: (CashbackCategory) -> Unit,
    onCardSelected: (BankCard) -> Unit,
    onDateRangeSelected: (String, String) -> Unit,
    onUnlimitedDateChanged: (Boolean) -> Unit,
    onPressCashback: (Int) -> Unit,
) {
    val selectedCategory = state.forms.category
    val cards = state.cards
    val selectedCard = state.forms.card
    val startDate = state.forms.startDate
    val expirationDate = state.forms.expirationDate
    val isUnlimited = state.forms.isUnlimited
    val isDuplicate = state.forms.isDuplicate
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
            horizontalAlignment = Alignment.CenterHorizontally
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
                startDate = startDate,
                expirationDate = expirationDate,
                onDateRangeSelected = onDateRangeSelected,
                isUnlimited = isUnlimited,
                onCheck = onUnlimitedDateChanged,
            )

            ChooseCashback(
                cashbackPercent = cashbackPercent,
                onPressCashback = onPressCashback,
            )

            if (isDuplicate) {
                Text(
                    text = stringResource(Res.string.add_category_duplicate_hint),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.errorContainer,
                    textAlign = TextAlign.Center,
                )
            }

            ChooseOrSaveButton(
                onClick = onUpsertCategoryClick,
                text = if (state.isEditing) {
                    stringResource(Res.string.edit_category_submit_button)
                } else {
                    stringResource(Res.string.add_category_submit_button)
                },
            )
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
internal fun TipText(
    text: String,
    hasError: Boolean,
    isCentered: Boolean = false,
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        textAlign = if (isCentered) TextAlign.Center else TextAlign.Left,
        style = MaterialTheme.typography.bodySmall,
        color = if (hasError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        maxLines = 1
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
                    TipText(
                        text = stringResource(Res.string.add_category_category_placeholder),
                        hasError = hasError,
                        isCentered = true,
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
            TipText(stringResource(Res.string.add_category_no_card), hasError)

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
    selectedCard: BankCard?,
    options: List<BankCard>,
    onCardSelected: (BankCard) -> Unit,
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
                value = getBankCardTitle(selectedCard).orEmpty(),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    TipText(
                        text = stringResource(Res.string.add_category_card_placeholder),
                        hasError = hasError,
                        isCentered = true
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
                            Logger.i { "${getBankCardTitle(option)}" }
                            Text(
                                text = getBankCardTitle(option).orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onCardSelected(option)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

private fun getBankCardTitle(card: BankCard?) = if (card == null) {
    null
} else {
    "${card.title}, ${card.bankName}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseDate(
    hasError: Boolean,
    startDate: String?,
    expirationDate: String?,
    onDateRangeSelected: (String, String) -> Unit,
    isUnlimited: Boolean,
    onCheck: (Boolean) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedRange = parseDateRange(startDate, expirationDate)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.add_category_expiration_label),
        )

        TextField(
            value = if (isUnlimited) {
                ""
            } else {
                formatDateRange(startDate, expirationDate)
            },
            onValueChange = {},
            placeholder = {
                TipText(
                    text = stringResource(Res.string.add_category_expiration_placeholder),
                    hasError = hasError,
                    isCentered = true,
                )
            },
            colors = textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = !isUnlimited,
                    onClick = { showDatePicker = true },
                ),
            enabled = !isUnlimited,
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { showDatePicker = true },
                    enabled = !isUnlimited,
                ) {
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
                }
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

    if (showDatePicker) {
        CustomDateRangePickerDialog(
            initialStartDateMillis = selectedRange?.startDate?.toPickerMillis(),
            initialEndDateMillis = selectedRange?.endDate?.toPickerMillis(),
            onDismiss = { showDatePicker = false },
            onConfirm = {selectedStartMillis, selectedEndMillis ->
                val startDate = selectedStartMillis?.toPickerDate()
                val endDate = selectedEndMillis?.toPickerDate()
                if (startDate != null && endDate != null) {
                    val range = normalizeDateRange(startDate, endDate)
                    onDateRangeSelected(
                        range.startDate.toString(),
                        range.endDate.toString(),
                    )
                }
                showDatePicker = false
            }
        )
    }
}

@Composable
fun CustomDateRangePickerDialog(
    initialStartDateMillis: Long? = null,
    initialEndDateMillis: Long? = null,
    onDismiss: () -> Unit,
    onConfirm: (startDateMillis: Long?, endDateMillis: Long?) -> Unit
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDateMillis,
        initialSelectedEndDateMillis = initialEndDateMillis,
        initialDisplayedMonthMillis = initialStartDateMillis,
        yearRange = 2020..2035,
        initialDisplayMode = DisplayMode.Picker
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column {
                DateRangePicker(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    showModeToggle = true,
                    title = {
                        Text(
                            text = stringResource(Res.string.add_category_date_picker_title),
                            modifier = Modifier.padding(
                                start = 28.dp,
                                top = 22.dp,
                                end = 28.dp
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    headline = {
                        Text(
                            text = formatRangeHeadline(
                                startMillis = state.selectedStartDateMillis,
                                endMillis = state.selectedEndDateMillis
                            ),
                            modifier = Modifier.padding(
                                start = 28.dp,
                                top = 18.dp,
                                bottom = 18.dp
                            ),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,

                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        headlineContentColor = MaterialTheme.colorScheme.onBackground,
                        weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                        subheadContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationContentColor = MaterialTheme.colorScheme.onBackground,

                        dayContentColor = MaterialTheme.colorScheme.onBackground,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        selectedDayContentColor = MaterialTheme.colorScheme.background,

                        todayContentColor = MaterialTheme.colorScheme.primary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,

                        dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary
                            .copy(alpha = 0.18f),
                        dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onBackground,
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 14.dp)
                        .background(MaterialTheme.colorScheme.background),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            state.setSelection(null, null)
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.add_category_date_picker_clear_button),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(Res.string.add_category_date_picker_cancel_button),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        TextButton(
                            enabled = state.selectedStartDateMillis != null &&
                                    state.selectedEndDateMillis != null,
                            onClick = {
                                onConfirm(
                                    state.selectedStartDateMillis,
                                    state.selectedEndDateMillis
                                )
                                onDismiss()
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.add_category_date_picker_save_button),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun formatRangeHeadline(
    startMillis: Long?,
    endMillis: Long?
): String {
    return when {
        startMillis != null && endMillis != null ->
            "${formatShortDate(startMillis)} - ${formatShortDate(endMillis)}"

        startMillis != null ->
            "${formatShortDate(startMillis)} -"

        else ->
            stringResource(Res.string.add_category_date_picker_label)
    }
}

private fun formatShortDate(millis: Long): String {
    val date = millis.toPickerDate()

    val day = date.day
    val month = date.month.number.toString().padStart(2, '0')
    val year = (date.year % 100).toString().padStart(2, '0')

    return "$day.$month.$year"
}

private fun parseDateRange(
    startDate: String?,
    expirationDate: String?,
): DateRangeSelection? {
    val endDate = parseDate(expirationDate) ?: return null
    val parsedStartDate = parseDate(startDate) ?: endDate
    return normalizeDateRange(parsedStartDate, endDate)
}

private fun parseDate(value: String?): LocalDate? =
    runCatching {
        LocalDate.parse(value.orEmpty())
    }.getOrNull()

private fun normalizeDateRange(
    startDate: LocalDate,
    endDate: LocalDate,
): DateRangeSelection =
    if (endDate < startDate) {
        DateRangeSelection(
            startDate = endDate,
            endDate = startDate,
        )
    } else {
        DateRangeSelection(
            startDate = startDate,
            endDate = endDate,
        )
    }

private fun formatDateRange(
    startDate: String?,
    expirationDate: String?,
): String {
    val range = parseDateRange(startDate, expirationDate)
        ?: return expirationDate.orEmpty()
    return "${formatDate(range.startDate)} - ${formatDate(range.endDate)}"
}

private fun formatDate(date: LocalDate): String {
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    return "$day.$month.${date.year}"
}

private fun LocalDate.toPickerMillis(): Long =
    atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

private fun Long.toPickerDate(): LocalDate =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date

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
                label = "−",
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
            .size(28.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall.copy(
                lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
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
fun PreviewUpsertCategoryScreenLight() {
    CashbackHomeTheme {
        val func: (String, String) -> Unit = {x1, x2 ->}
        UpsertCategoryScreen(
            state = UpsertCategoriesScreenState.UpsertCategory(TextFields(), listOf()),
            onUpsertCategoryClick = {},
            onBackClick = {},
            onAddCardClick = {},
            onCategorySelected = {},
            onCardSelected = {},
            onDateRangeSelected = func,
            onUnlimitedDateChanged = {},
            onPressCashback = {}
        )
    }
}
