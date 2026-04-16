package org.homesharing.cashbackhome.presentation.cards

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.choose_bank_empty_results
import cashbackhome.composeapp.generated.resources.choose_bank_search_placeholder
import cashbackhome.composeapp.generated.resources.choose_bank_title
import cashbackhome.composeapp.generated.resources.search
import org.homesharing.cashbackhome.presentation.categories.textFieldColors
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal data class BankSelectionResult(
    val bankName: String,
    val eventId: Long,
)

internal data class BankPickerOption(
    val name: String,
    val badgeText: String,
    val badgeBackgroundColor: Color,
    val badgeContentColor: Color,
    val badgeBorderColor: Color = Color.Transparent,
    val searchTerms: List<String> = emptyList(),
)

internal val BankPickerOptions = listOf(
    BankPickerOption(
        name = "T-Банк",
        badgeText = "T",
        badgeBackgroundColor = Color(0xFFFFDD2D),
        badgeContentColor = Color(0xFF111111),
        searchTerms = listOf("Тинькофф", "Tinkoff"),
    ),
    BankPickerOption(
        name = "Сбербанк",
        badgeText = "С",
        badgeBackgroundColor = Color(0xFFFFFFFF),
        badgeContentColor = Color(0xFF4CAF50),
        badgeBorderColor = Color(0xFFB7DDBD),
        searchTerms = listOf("Сбер"),
    ),
    BankPickerOption(
        name = "ВТБ",
        badgeText = "ВТБ",
        badgeBackgroundColor = Color(0xFFE7F1FF),
        badgeContentColor = Color(0xFF2A7DE1),
    ),
    BankPickerOption(
        name = "Альфа-Банк",
        badgeText = "A",
        badgeBackgroundColor = Color(0xFFFFFFFF),
        badgeContentColor = Color(0xFFE53935),
        badgeBorderColor = Color(0xFFE9D5D4),
        searchTerms = listOf("Альфа"),
    ),
    BankPickerOption(
        name = "Райффайзенбанк",
        badgeText = "R",
        badgeBackgroundColor = Color(0xFFFFE04A),
        badgeContentColor = Color(0xFF111111),
        searchTerms = listOf("Райфф"),
    ),
    BankPickerOption(
        name = "Газпромбанк",
        badgeText = "ГП",
        badgeBackgroundColor = Color(0xFFFFFFFF),
        badgeContentColor = Color(0xFF111111),
        badgeBorderColor = Color(0xFFD8DADF),
    ),
    BankPickerOption(
        name = "МТС Банк",
        badgeText = "МТС",
        badgeBackgroundColor = Color(0xFFFFFFFF),
        badgeContentColor = Color(0xFF111111),
        badgeBorderColor = Color(0xFFD8DADF),
    ),
    BankPickerOption(
        name = "Ozon Банк",
        badgeText = "O",
        badgeBackgroundColor = Color(0xFFE7F0FF),
        badgeContentColor = Color(0xFF1259FF),
        searchTerms = listOf("Озон"),
    ),
    BankPickerOption(
        name = "Совкомбанк",
        badgeText = "С",
        badgeBackgroundColor = Color(0xFFFBE7F1),
        badgeContentColor = Color(0xFFAD1457),
    ),
)

@Composable
internal fun ChooseBankScreen(
    selectedBankName: String?,
    onBackClick: () -> Unit,
    onBankSelected: (String) -> Unit,
) {
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    val filteredBanks = remember(searchQuery) {
        BankPickerOptions.filter { option ->
            option.matchesQuery(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ChooseBankTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            SearchBanksField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = filteredBanks,
                    key = { it.name },
                ) { bank ->
                    BankPickerRow(
                        bank = bank,
                        isSelected = bank.name == selectedBankName,
                        onClick = {
                            onBankSelected(bank.name)
                        },
                    )
                }

                if (filteredBanks.isEmpty()) {
                    item {
                        EmptySearchResult()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseBankTopBar(
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.choose_bank_title),
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
private fun SearchBanksField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(Res.string.choose_bank_search_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = textFieldColors(),
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        shape = RoundedCornerShape(16.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Composable
private fun BankPickerRow(
    bank: BankPickerOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.background
    }
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BankBadge(bankName = bank.name)

        Text(
            text = bank.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}

@Composable
internal fun BankBadge(
    bankName: String,
) {
    val bank = BankPickerOptions.find { it.name == bankName}!!
    val badgeTextStyle = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = if (bank.badgeText.length > 2) 10.sp else 16.sp,
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bank.badgeBackgroundColor)
            .border(
                width = 1.dp,
                color = bank.badgeBorderColor,
                shape = RoundedCornerShape(12.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = bank.badgeText,
            style = badgeTextStyle,
            color = bank.badgeContentColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptySearchResult() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.choose_bank_empty_results),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

private fun BankPickerOption.matchesQuery(query: String): Boolean {
    val normalizedQuery = query.trim()
    if (normalizedQuery.isBlank()) {
        return true
    }

    return name.contains(normalizedQuery, ignoreCase = true) ||
        searchTerms.any { term ->
            term.contains(normalizedQuery, ignoreCase = true)
        }
}

@Composable
@Preview
private fun ChooseBankScreenPreview() {
    CashbackHomeTheme {
        ChooseBankScreen(
            selectedBankName = "T-Банк",
            onBackClick = {},
            onBankSelected = {},
        )
    }
}
