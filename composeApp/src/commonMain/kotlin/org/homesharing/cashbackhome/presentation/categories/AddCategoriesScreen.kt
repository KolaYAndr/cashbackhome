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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cashbackhome.composeapp.generated.resources.Res
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
import org.homesharing.cashbackhome.domain.model.BankCardDraft
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val DefaultCashbackPercent = 5
private const val MinCashbackPercent = 1
private const val MaxCashbackPercent = 100

@Composable
internal fun AddCategoryScreenRoot(
    viewModel: AddCategoriesScreenViewModel = koinViewModel(),
    cards: List<BankCardDraft> = listOf(),
    onAddCategoryClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit,
) {
    AddCategoryScreen(
        onAddCategoryClick = {
            viewModel.upsertRule(it)
            onAddCategoryClick()
        },
        onBackClick = onBackClick,
        onAddCardClick = onAddCardClick
    )
}

@Composable
private fun AddCategoryScreen(
    cards: List<BankCardDraft> = listOf(),
    onAddCategoryClick: (CashbackRuleDraft) -> Unit,
    onBackClick: () -> Unit,
    onAddCardClick: () -> Unit,
) {
    var selectedCard by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCategory by rememberSaveable {
        mutableStateOf<CashbackRuleDraft.CashbackCategory?>(null)
    }
    var expirationDate by rememberSaveable { mutableStateOf("") }
    var isUnlimited by rememberSaveable { mutableStateOf(false) }
    var cashbackPercent by rememberSaveable { mutableStateOf(DefaultCashbackPercent) }

    val categoryOptions = CashbackRuleDraft.CashbackCategory.all.map {
        it to categoryName(it)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AddCategoryTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CategorySelectionFieldSection(
                label = stringResource(Res.string.add_category_category_label),
                placeholder = stringResource(Res.string.add_category_category_placeholder),
                selectedValue = selectedCategory,
                options = categoryOptions,
                onOptionSelected = { selectedCategory = it },
            )

            //Card
            if (cards.isEmpty()) {
                NoCards(
                    placeholder = stringResource(Res.string.add_category_no_card),
                    onAddCardClick = onAddCardClick
                )
            } else {
                CardSelectionFieldSection(
                    label = stringResource(Res.string.add_category_card_label),
                    placeholder = stringResource(Res.string.add_category_card_placeholder),
                    selectedValue = selectedCard,
                    options = cards,
                    onOptionSelected = { selectedCard = it },
                )
            }

            ChooseDate(
                expirationDate = expirationDate,
                onType = { expirationDate = it },
                isUnlimited = isUnlimited,
                onCheck = {
                    isUnlimited = it
                    if (it) {
                        expirationDate = ""
                    }
                }
            )

            ChooseCashback(
                cashbackPercent = cashbackPercent,
                onPressMinus = {
                    cashbackPercent = (cashbackPercent - 1).coerceAtLeast(MinCashbackPercent)
                },
                onPressPlus = {
                    cashbackPercent = (cashbackPercent + 1).coerceAtMost(MaxCashbackPercent)
                }
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
                onClick = {
                    onAddCategoryClick(
                        CashbackRuleDraft(
                            percentage = cashbackPercent.toDouble() / 100,
                            category = selectedCategory ?: CashbackRuleDraft.CashbackCategory.Other,
                            expirationDate = expirationDate
                        )
                    )
                },
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
                    text = stringResource(Res.string.add_category_submit_button),
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
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.add_category_title),
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
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun DefaultTextInBox(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        color = color,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectionFieldSection(
    label: String,
    placeholder: String,
    selectedValue: CashbackRuleDraft.CashbackCategory?,
    options: List<Pair<CashbackRuleDraft.CashbackCategory, String>>,
    onOptionSelected: (CashbackRuleDraft.CashbackCategory) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = label)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = categoryName(selectedValue),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    DefaultTextInBox(placeholder)
                },
                colors = dropdownTextFieldColors(),
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
                            onOptionSelected(option.first)
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
    placeholder: String,
    onAddCardClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultTextInBox(placeholder)

        Button(
            onClick = onAddCardClick,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            DefaultTextInBox(placeholder, MaterialTheme.colorScheme.onBackground)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardSelectionFieldSection(
    label: String,
    placeholder: String,
    selectedValue: String?,
    options: List<BankCardDraft>,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(text = label)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedValue.orEmpty(),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    DefaultTextInBox(placeholder)
                },
                colors = dropdownTextFieldColors(),
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
                            onOptionSelected(option.bankName)
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
    expirationDate: String,
    onType: (String) -> Unit,
    isUnlimited: Boolean,
    onCheck: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.add_category_expiration_label),
        )

        FormTextField(
            value = expirationDate,
            onValueChange = onType,
            placeholder = stringResource(Res.string.add_category_expiration_placeholder),
            enabled = !isUnlimited,
            trailingContent = {
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
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = isUnlimited,
                onCheckedChange = onCheck,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
            Text(
                text = stringResource(Res.string.add_category_unlimited),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        placeholder = {
            DefaultTextInBox(placeholder)
        },
        colors = dropdownTextFieldColors(),
        trailingIcon = trailingContent,
        shape = RoundedCornerShape(14.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Composable
private fun ChooseCashback(
    cashbackPercent: Int,
    onPressMinus: () -> Unit,
    onPressPlus: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SectionLabel(stringResource(Res.string.add_category_cashback_label))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(19.dp),
        ) {
            StepperButton(
                label = "-",
                onClick = onPressMinus,
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

            StepperButton(
                label = "+",
                onClick = onPressPlus,
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
private fun dropdownTextFieldColors() = TextFieldDefaults.colors(
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
        AddCategoryScreenRoot(onAddCategoryClick = {}, onBackClick = {}, onAddCardClick = {} )
    }
}
