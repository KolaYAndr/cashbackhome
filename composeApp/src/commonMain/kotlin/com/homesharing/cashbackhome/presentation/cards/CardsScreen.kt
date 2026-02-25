package com.homesharing.cashbackhome.presentation.cards

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
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.homesharing.cashbackhome.domain.models.BankCardWithCashback
import com.homesharing.cashbackhome.domain.models.CashbackRule
import homesharing.composeapp.generated.resources.Res
import homesharing.composeapp.generated.resources.add
import homesharing.composeapp.generated.resources.arrow_drop_down
import homesharing.composeapp.generated.resources.arrow_drop_up
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CardsScreen(
    viewModel: CardsViewModel = koinViewModel(),
) {
    val cards by viewModel.uiState.collectAsStateWithLifecycle()

    CardsScreen(
        cards = cards,
        onAddCardClick = {},
        onAddCashbackClick = {},
        onCardClick = {},
    )
}

@Composable
private fun CardsScreen(
    cards: List<BankCardWithCashback>,
    onAddCardClick: () -> Unit,
    onAddCashbackClick: (Long) -> Unit,
    onCardClick: (Long) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCardClick) {
                Icon(
                    painter = painterResource(Res.drawable.add),
                    contentDescription = "Add card"
                )
            }
        }
    ) { innerPadding ->
        if (cards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No cards yet")
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
            .clickable { onCardClick() }
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

                IconButton(onClick = { expanded = !expanded }) {
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

            if (expanded) {
                Spacer(Modifier.height(8.dp))

                if (cardWithCashback.cashbacks.isEmpty()) {
                    Text(
                        text = "No cashback rules",
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
                    Text("Add cashback rule")
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
        val categoryName = when (rule.category) {
            CashbackRule.CashbackCategory.Groceries -> "Groceries"
            CashbackRule.CashbackCategory.Cafe -> "Cafe"
            CashbackRule.CashbackCategory.Restaurant -> "Restaurant"
            CashbackRule.CashbackCategory.Travel -> "Travel"
            CashbackRule.CashbackCategory.OnlineShopping -> "Online shopping"
            CashbackRule.CashbackCategory.Flowers -> "Flowers"
            CashbackRule.CashbackCategory.Pharmacy -> "Pharmacy"
            CashbackRule.CashbackCategory.Other -> "Other"
        }

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
