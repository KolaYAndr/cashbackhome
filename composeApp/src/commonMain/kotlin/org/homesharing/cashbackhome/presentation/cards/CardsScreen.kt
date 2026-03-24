package org.homesharing.cashbackhome.presentation.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.add
import cashbackhome.composeapp.generated.resources.arrow_drop_down
import cashbackhome.composeapp.generated.resources.arrow_drop_up
import cashbackhome.composeapp.generated.resources.card_add
import cashbackhome.composeapp.generated.resources.cards_empty
import cashbackhome.composeapp.generated.resources.cashback_rule_add
import cashbackhome.composeapp.generated.resources.cashback_rules_empty
import cashbackhome.composeapp.generated.resources.category_cafe
import cashbackhome.composeapp.generated.resources.category_flowers
import cashbackhome.composeapp.generated.resources.category_groceries
import cashbackhome.composeapp.generated.resources.category_online_shopping
import cashbackhome.composeapp.generated.resources.category_other
import cashbackhome.composeapp.generated.resources.category_pharmacy
import cashbackhome.composeapp.generated.resources.category_restaurant
import cashbackhome.composeapp.generated.resources.category_travel
import org.homesharing.cashbackhome.domain.entity.BankCardWithCashback
import org.homesharing.cashbackhome.domain.entity.CashbackRule
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CardsScreen(
    viewModel: CardsViewModel = koinViewModel(),
    onAddCardClick: () -> Unit
) {
    val cards by viewModel.uiState.collectAsStateWithLifecycle()

    CardsScreen(
        cards = cards,
        onAddCardClick = onAddCardClick,
        onAddCashbackClick = {},
        onCardClick = {},
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CardsScreen(
    cards: List<BankCardWithCashback>?,
    onAddCardClick: () -> Unit,
    onAddCashbackClick: (Long) -> Unit,
    onCardClick: (Long) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCardClick) {
                Icon(
                    painter = painterResource(Res.drawable.add),
                    contentDescription = stringResource(Res.string.card_add)
                )
            }
        }
    ) { innerPadding ->
        if (cards.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (cards == null) {
                    LoadingIndicator()
                } else {
                    Text(stringResource(Res.string.cards_empty))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = cards,
                    key = { it.card.cardId }
                ) { item ->
                    CardItem(
                        cardWithCashback = item,
                        onCardClick = { onCardClick(item.card.cardId) },
                        onAddCashbackClick = { onAddCashbackClick(item.card.cardId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CardItem(
    cardWithCashback: BankCardWithCashback,
    onCardClick: () -> Unit,
    onAddCashbackClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = cardWithCashback.card.bankName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = cardWithCashback.card.mask,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        painter = painterResource(
                            if (expanded) {
                                Res.drawable.arrow_drop_up
                            } else {
                                Res.drawable.arrow_drop_down
                            }
                        ),
                        contentDescription = null
                    )
                }
            }

            AnimatedContent(expanded) { isExpanded ->
                if (isExpanded) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(8.dp)
                            ).padding(horizontal = 12.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))
                        if (cardWithCashback.cashbacks.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.cashback_rules_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            cardWithCashback.cashbacks.forEach { rule ->
                                CashbackRuleItem(rule = rule)
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        TextButton(onClick = onAddCashbackClick) {
                            Icon(
                                painter = painterResource(Res.drawable.add),
                                contentDescription = null
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(Res.string.cashback_rule_add))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CashbackRuleItem(rule: CashbackRule) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val categoryName = stringResource(
            when (rule.category) {
                CashbackRule.CashbackCategory.Groceries -> Res.string.category_groceries
                CashbackRule.CashbackCategory.Cafe -> Res.string.category_cafe
                CashbackRule.CashbackCategory.Restaurant -> Res.string.category_restaurant
                CashbackRule.CashbackCategory.Travel -> Res.string.category_travel
                CashbackRule.CashbackCategory.OnlineShopping -> Res.string.category_online_shopping
                CashbackRule.CashbackCategory.Flowers -> Res.string.category_flowers
                CashbackRule.CashbackCategory.Pharmacy -> Res.string.category_pharmacy
                CashbackRule.CashbackCategory.Other -> Res.string.category_other
            }
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = rule.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${rule.percentage}%",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
