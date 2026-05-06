package org.homesharing.cashbackhome.presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.card_type_credit
import cashbackhome.composeapp.generated.resources.card_type_debit
import cashbackhome.composeapp.generated.resources.card_type_other
import cashbackhome.composeapp.generated.resources.cards_add_button
import cashbackhome.composeapp.generated.resources.cards_empty_description
import cashbackhome.composeapp.generated.resources.cards_empty_title
import cashbackhome.composeapp.generated.resources.delete
import cashbackhome.composeapp.generated.resources.edit
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType
import org.homesharing.cashbackhome.presentation.categories.SwipeBackground
import org.homesharing.cashbackhome.presentation.home.ButtonOnEmptyScreen
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.home.ScaffoldState
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CardsScreenRoot(
    viewModel: CardsViewModel = koinViewModel(),
    scaffoldState: ScaffoldState,
    onAddCardClick: () -> Unit,
    onEditCard: (BankCard) -> Unit,
    onCardClick: (BankCard) -> Unit,
) {
    scaffoldState.updateSearchAndSortBar(
        isWidened = true,
    )
    when (val state = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is CardsScreenState.Loading -> {
            scaffoldState.updateFab(false, onAddCardClick)
            LoadingScreen()
        }
        is CardsScreenState.EmptyScreen -> {
            scaffoldState.updateFab(false, onAddCardClick)
            EmptyCards(onAddCardClick = onAddCardClick)
        }
        is CardsScreenState.Cards -> {
            scaffoldState.updateFab(true, onAddCardClick)
            if (scaffoldState.searchAndSortBarConfig.value.isGrid) {
                CardsGrid(
                    cards = state.cards,
                    onDeleteCard = {
                        viewModel.deleteCashbackRuleById(it)
                    },
                    onEditCard = onEditCard,
                    onCardClick = onCardClick,
                )
            } else {
                CardsList(
                    cards = state.cards,
                    onDeleteCard = {
                        viewModel.deleteCashbackRuleById(it)
                    },
                    onEditCard = onEditCard,
                    onCardClick = onCardClick,
                )
            }
        }
    }
}

@Composable
private fun CardsList(
    cards: List<BankCard>,
    onEditCard: (BankCard) -> Unit,
    onDeleteCard: (Long) -> Unit,
    onCardClick: (BankCard) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = cards,
            key = { card -> card.cardId },
        ) { card ->
            CardListItem(
                card = card,
                onEditCardSwipe = { onEditCard(card) },
                onDeleteCardSwipe = { onDeleteCard(card.cardId) },
                onClick = { onCardClick(card) },
            )
        }
    }
}

@Composable
private fun CardsGrid(
    cards: List<BankCard>,
    onDeleteCard: (Long) -> Unit,
    onEditCard: (BankCard) -> Unit,
    onCardClick: (BankCard) -> Unit,
) {
    var overlayActiveItem by remember { mutableStateOf<Long?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = cards,
            key = { card -> card.cardId },
        ) { card ->
            CardGridItem(
                card = card,
                isLongPressed = (overlayActiveItem == card.cardId),
                onEditCategoryClick = { onEditCard(card) },
                onDeleteCategoryClick = { onDeleteCard(card.cardId) },
                onLongClick = { overlayActiveItem = card.cardId},
                onClick = { onCardClick(card) },
            )
        }
    }
}

@Composable
private fun CardListItem(
    card: BankCard,
    onDeleteCardSwipe: () -> Unit,
    onEditCardSwipe: () -> Unit,
    onClick: () -> Unit,
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
                SwipeToDismissBoxValue.StartToEnd -> onEditCardSwipe()
                SwipeToDismissBoxValue.EndToStart -> onDeleteCardSwipe()
                SwipeToDismissBoxValue.Settled -> Unit
            }
            coroutineScope.launch {
                dismissState.reset()
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BankBadge(card.bankName)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = card.bankName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = card.title.ifBlank { "••••" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                CardTypeChip(cardType = card.cardType)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCardItem() {
    CardListItem(
        BankCard(0L, "", ""),
        {},
        {},
        {},
    )
}

@Composable
private fun CardGridItem(
    card: BankCard,
    isLongPressed: Boolean,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
    onEditCategoryClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BankBadge(card.bankName)

                        Text(
                            text = card.bankName,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        text = card.title.ifBlank { "••••" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                CardTypeChip(cardType = card.cardType)
            }
            ItemActionOverlay(
                isVisible = isLongPressed,
                onEditClicked = onEditCategoryClick,
                onDeleteClicked = onDeleteCategoryClick,
            )
        }

    }
}

@Composable
fun ItemActionOverlay(
    isVisible: Boolean,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Logger.i { "Hello, I am $isVisible" }
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEditClicked) {
                    Icon(
                        painter = painterResource(Res.drawable.edit),
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        painter = painterResource(Res.drawable.delete),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun CardTypeChip(cardType: BankCardType) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = CircleShape,
    ) {
        Text(
            text = cardTypeLabel(cardType),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun EmptyCards(
    onAddCardClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(Res.string.cards_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(Res.string.cards_empty_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        ButtonOnEmptyScreen(
            onClick = onAddCardClick,
            text = stringResource(Res.string.cards_add_button)
        )
    }
}

@Composable
private fun cardTypeLabel(cardType: BankCardType): String = when (cardType) {
    BankCardType.Debit -> stringResource(Res.string.card_type_debit)
    BankCardType.Credit -> stringResource(Res.string.card_type_credit)
    else -> stringResource(Res.string.card_type_other)
}

@Preview
@Composable
private fun CardsListPreview() {
    CashbackHomeTheme {
        CardsList(
            cards = previewCards,
            {},
            {},
            {}
        )
    }
}

@Preview
@Composable
private fun CardsGridPreview() {
    CashbackHomeTheme {
        CardsGrid(
            cards = previewCards,
            {}, {}, {}
        )
    }
}

@Preview
@Composable
private fun EmptyCardsPreview() {
    CashbackHomeTheme {
        EmptyCards(onAddCardClick = {})
    }
}

private val previewCards = listOf(
    BankCard(
        cardId = 1,
        bankName = "Т-Банк",
        title = "Black",
        cardType = BankCardType.Debit,
    ),
    BankCard(
        cardId = 2,
        bankName = "Альфа-Банк",
        title = "asdfsfdsfsasfasfasfasfsdfsasdfsfasfasfafasfasfaf",
        cardType = BankCardType.Credit,
    ),
    BankCard(
        cardId = 3,
        bankName = "Сбер",
        title = "",
        cardType = BankCardType.Debit,
    ),
)