package org.homesharing.cashbackhome.presentation.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.card_details_bind_category
import cashbackhome.composeapp.generated.resources.card_details_bind_new_category
import cashbackhome.composeapp.generated.resources.card_details_categories_title
import cashbackhome.composeapp.generated.resources.card_details_empty_description
import cashbackhome.composeapp.generated.resources.card_details_empty_title
import cashbackhome.composeapp.generated.resources.card_details_title
import cashbackhome.composeapp.generated.resources.card_details_unlimited
import cashbackhome.composeapp.generated.resources.card_details_until_date
import cashbackhome.composeapp.generated.resources.card_type_credit
import cashbackhome.composeapp.generated.resources.card_type_debit
import cashbackhome.composeapp.generated.resources.card_type_other
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.presentation.categories.SwipeBackground
import org.homesharing.cashbackhome.presentation.categories.getPercents
import org.homesharing.cashbackhome.presentation.home.ChooseOrSaveButton
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.home.colorUnderline
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CardDetailsScreenRoot(
    card: BankCard,
    onBackClick: () -> Unit,
    onAddCategoryClick: (BankCard) -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
    onEditCardClick: (BankCard) -> Unit,
    onCardDeleted: () -> Unit,
) {
    val viewModel: CardDetailsViewModel = koinViewModel(
        parameters = { parametersOf(card) }
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        CardDetailsScreenState.Loading -> LoadingScreen()
        is CardDetailsScreenState.Content -> {
            CardDetailsScreen(
                card = state.card,
                cashbacks = state.cashbacks,
                onBackClick = onBackClick,
                onAddCategoryClick = { onAddCategoryClick(state.card) },
                onEditCategoryClick = onEditCategoryClick,
                onDeleteCategoryClick = viewModel::deleteCashbackRuleById,
                onEditCardClick = { onEditCardClick(state.card) },
                onDeleteCardClick = {
                    viewModel.deleteCardById(state.card.cardId)
                    onCardDeleted()
                },
            )
        }
    }
}

@Composable
private fun CardDetailsScreen(
    card: BankCard,
    cashbacks: List<CashbackRule>,
    onBackClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
    onDeleteCategoryClick: (Long) -> Unit,
    onEditCardClick: () -> Unit,
    onDeleteCardClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CardDetailsTopBar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SwipeableCardSummary(
                card = card,
                onEditCardClick = onEditCardClick,
                onDeleteCardClick = onDeleteCardClick,
            )

            if (cashbacks.isEmpty()) {
                EmptyCardCashbacks(
                    modifier = Modifier.weight(1f),
                    onAddCategoryClick = onAddCategoryClick,
                )
            } else {
                Text(
                    text = stringResource(Res.string.card_details_categories_title),
                    modifier = Modifier.padding(top = 10.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = cashbacks,
                        key = { cashback -> cashback.cashbackRuleId },
                    ) { cashback ->
                        CashbackDetailsRow(
                            cashback = cashback,
                            onEditCategoryClick = { onEditCategoryClick(cashback) },
                            onDeleteCategoryClick = {
                                onDeleteCategoryClick(cashback.cashbackRuleId)
                            },
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            BindCategoryButton(
                                text = stringResource(Res.string.card_details_bind_new_category),
                                onClick = onAddCategoryClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardDetailsTopBar(
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.card_details_title),
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
private fun SwipeableCardSummary(
    card: BankCard,
    onEditCardClick: () -> Unit,
    onDeleteCardClick: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(positionalThreshold = { 0.5f })
    val coroutineScope = rememberCoroutineScope()

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            SwipeBackground(dismissState)
        },
        modifier = Modifier
            .fillMaxWidth(),
        onDismiss = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> onEditCardClick()
                SwipeToDismissBoxValue.EndToStart -> onDeleteCardClick()
                SwipeToDismissBoxValue.Settled -> Unit
            }
            coroutineScope.launch {
                dismissState.reset()
            }
        },
    ) {
        CardSummary(card = card)
    }
}

@Composable
private fun CardSummary(card: BankCard) {
    val shape = RoundedCornerShape(14.dp)
    val glowColor = Color(0xFFECF8A8)
    val glowAlphas = listOf(0.7f, 0.10f, 0.04f)

    val density = LocalDensity.current
    val glowBrush = with(density) {
        Brush.radialGradient(
            colorStops = arrayOf(
                0f to glowColor.copy(alpha = glowAlphas[0]),
                0.28f to glowColor.copy(alpha = glowAlphas[1]),
                0.48f to glowColor.copy(alpha = glowAlphas[2]),
                0.76f to Color.Transparent,
                1f to Color.Transparent,
            ),
            center = Offset(
                x = 282.dp.toPx(),
                y = 36.dp.toPx(),
            ),
            radius = 220.dp.toPx(),
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = shape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(MaterialTheme.colorScheme.background)
                .background(glowBrush)
                .padding(32.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = card.bankName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )

                    BankBadge(bankName = card.bankName)
                }
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${card.bankName.lowercase()}.ru",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .colorUnderline(MaterialTheme.colorScheme.onSurfaceVariant),
                    )

                    Text(
                        text = "89811766464",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .colorUnderline(MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
            }

            CardTypePill(
                cardType = card.cardType,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun CardTypePill(
    cardType: BankCardType,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Text(
            text = cardTypeLabel(cardType),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun CashbackDetailsRow(
    cashback: CashbackRule,
    onEditCategoryClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(positionalThreshold = { 0.5f })
    val coroutineScope = rememberCoroutineScope()

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            SwipeBackground(dismissState)
        },
        modifier = Modifier.fillMaxWidth(),
        onDismiss = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> onEditCategoryClick()
                SwipeToDismissBoxValue.EndToStart -> onDeleteCategoryClick()
                SwipeToDismissBoxValue.Settled -> Unit
            }
            coroutineScope.launch {
                dismissState.reset()
            }
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = categoryName(cashback.category),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                    )

                    Text(
                        text = expirationLabel(cashback.startDate, cashback.expirationDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }

                Text(
                    text = getPercents(cashback.percentage),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun EmptyCardCashbacks(
    modifier: Modifier = Modifier,
    onAddCategoryClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(Res.string.card_details_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(Res.string.card_details_empty_description),
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(6.dp))

        BindCategoryButton(
            text = stringResource(Res.string.card_details_bind_category),
            onClick = onAddCategoryClick,
        )
    }
}

@Composable
private fun BindCategoryButton(
    text: String,
    onClick: () -> Unit,
) {
    ChooseOrSaveButton(
        onClick = onClick,
        text = text
    )
}

@Composable
private fun expirationLabel(
    startDate: String,
    expirationDate: String,
): String {
    val trimmedDate = expirationDate.trim()
    if (trimmedDate.isBlank()) {
        return stringResource(Res.string.card_details_unlimited)
    }

    val parsedExpirationDate = parseDate(trimmedDate) ?: return trimmedDate
    val parsedStartDate = parseDate(startDate) ?: return trimmedDate

    return stringResource(
        Res.string.card_details_until_date,
        formatDate(parsedStartDate),
        formatDate(parsedExpirationDate)
    )
}

private fun parseDate(value: String?): LocalDate? =
    runCatching {
        LocalDate.parse(value.orEmpty())
    }.getOrNull()

private fun formatDate(date: LocalDate): String {
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    return "$day.$month.${date.year}"
}

@Composable
private fun cardTypeLabel(cardType: BankCardType): String = when (cardType) {
    BankCardType.Debit -> stringResource(Res.string.card_type_debit)
    BankCardType.Credit -> stringResource(Res.string.card_type_credit)
    else -> stringResource(Res.string.card_type_other)
}

@Preview
@Composable
private fun CardDetailsScreenPreview() {
    CashbackHomeTheme {
        CardDetailsScreen(
            card = BankCard(
                cardId = 1,
                title = "Black",
                bankName = "Т-Банк",
                cardType = BankCardType.Debit,
            ),
            cashbacks = listOf(
                CashbackRule(
                    cashbackRuleId = 1,
                    bankCardName = "T-Банк",
                    percentage = 0.05,
                    category = CashbackRule.CashbackCategory.Supermarkets,
                    maxAmount = null,
                    startDate = "2026-04-01",
                    expirationDate = "2026-04-13",
                    bankCardId = 0L,
                ),
                CashbackRule(
                    cashbackRuleId = 2,
                    bankCardName = "Т-Банк",
                    percentage = 0.03,
                    category = CashbackRule.CashbackCategory.Pharmacy,
                    maxAmount = null,
                    startDate = "2026-04-01",
                    expirationDate = "2026-04-13",
                    bankCardId = 0L,
                ),
            ),
            onBackClick = {},
            onAddCategoryClick = {},
            onEditCategoryClick = {},
            onDeleteCategoryClick = {},
            onEditCardClick = {},
            onDeleteCardClick = {},
        )
    }
}
