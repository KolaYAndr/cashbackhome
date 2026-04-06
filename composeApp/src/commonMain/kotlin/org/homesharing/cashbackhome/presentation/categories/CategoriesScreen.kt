package org.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.bankplaceholder
import cashbackhome.composeapp.generated.resources.categories_screen_add_category_button
import cashbackhome.composeapp.generated.resources.categories_screen_empty_description
import cashbackhome.composeapp.generated.resources.categories_screen_empty_title
import cashbackhome.composeapp.generated.resources.categories_screen_wrong_data_format
import cashbackhome.composeapp.generated.resources.delete
import cashbackhome.composeapp.generated.resources.edit
import cashbackhome.composeapp.generated.resources.filter_icon_description
import cashbackhome.composeapp.generated.resources.tune
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.home.ScaffoldState
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs
import kotlin.time.Clock

@Composable
internal fun CategoriesScreenRoot(
    viewModel: CategoriesScreenViewModel = koinViewModel(),
    scaffoldState: ScaffoldState,
    onAddCategoryClick: () -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    scaffoldState.updateSearchAndSortBar(false, false)
    when (val state = uiState.value) {
        is CategoriesScreenState.EmptyScreen -> {
            scaffoldState.updateFab(false, onAddCategoryClick)
            EmptyCategories(onAddCategoryClick)
        }
        is CategoriesScreenState.Categories -> {
            scaffoldState.updateFab(true, onAddCategoryClick)
            CategoriesScreen(
                categories = state.categories,
                onEditCategorySwipe = onEditCategoryClick,
                onDeleteCategorySwipe = { viewModel.deleteCashbackRuleById(it) }
            )
        }
        is CategoriesScreenState.Loading -> {
            scaffoldState.updateFab(false, onAddCategoryClick)
            LoadingScreen()
        }
    }
}

@Composable
private fun CategoriesScreen(
    categories: List<CashbackRule>,
    onEditCategorySwipe: (CashbackRule) -> Unit,
    onDeleteCategorySwipe: (Long) -> Unit,
) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = categories,
            key = { category -> category.cashbackRuleId}
        ) { category ->
            CashbackCard(
                category = category,
                onEditCategorySwipe = { onEditCategorySwipe(category) },
                onDeleteCategorySwipe = { onDeleteCategorySwipe(category.cashbackRuleId) }
            )
        }
    }
}

@Composable
private fun CashbackCard(
    category: CashbackRule,
    onEditCategorySwipe: () -> Unit,
    onDeleteCategorySwipe: () -> Unit,
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
                SwipeToDismissBoxValue.StartToEnd -> onEditCategorySwipe()
                SwipeToDismissBoxValue.EndToStart -> onDeleteCategorySwipe()
                SwipeToDismissBoxValue.Settled -> Unit
            }
            coroutineScope.launch {
                dismissState.reset()
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(27.dp)
            ) {
                Row (
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Image(
                        painter = painterResource(Res.drawable.bankplaceholder),
                        contentDescription = null,
                        modifier = Modifier.size(53.dp),
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            text = categoryName(category.category),
                            lineHeight = 22.sp,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = category.bankCardName,
                            lineHeight = 19.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text(
                    text = getExpirationDaysTitle(category.expirationDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = getDateColor(category.expirationDate),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = getPercents(category.percentage),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
internal fun SwipeBackground(state: SwipeToDismissBoxState) {
    val revealedWidth = revealedWidth(state)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        when (state.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> {
                SwipeActionStripe(
                    modifier = Modifier.align(Alignment.CenterStart),
                    width = revealedWidth,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    iconRes = Res.drawable.edit,
                )
            }

            SwipeToDismissBoxValue.EndToStart -> {
                SwipeActionStripe(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    width = revealedWidth,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    iconRes = Res.drawable.delete,
                )
            }

            SwipeToDismissBoxValue.Settled -> Unit
        }
    }
}

@Composable
private fun SwipeActionStripe(
    modifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    iconRes: DrawableResource,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun revealedWidth(state: SwipeToDismissBoxState): Dp {
    val offsetPx = runCatching { state.requireOffset() }.getOrDefault(0f)
    return LocalDensity.current.run {
        abs(offsetPx).toDp()
    }
}

private fun getPercents(x: Double) = "${(x * 100).toInt()}%"

@Composable
private fun getDateColor(until: String): Color {
    runCatching{ LocalDate.parse(until) }
        .onSuccess {
            val dateNow = getCurrentDate()
            val daysLeft = (it - dateNow).days

            if (daysLeft > 15)
                return MaterialTheme.colorScheme.onSurfaceVariant
            return MaterialTheme.colorScheme.error
        }
    return MaterialTheme.colorScheme.errorContainer
}

private fun getCurrentDate(): LocalDate {
    val currentMoment = Clock.System.now()
    val datetimeInSystemZone = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInSystemZone.date
}

@Composable
private fun getExpirationDaysTitle(x: String): String {
    var returnValue = stringResource(Res.string.categories_screen_wrong_data_format)
    runCatching{ LocalDate.parse(x) }
        .onSuccess {
            val res = StringBuilder("до ")

            val monthNumberToString = mapOf(
                1 to " января",
                2 to " февраля",
                3 to " марта",
                4 to " апреля",
                5 to " мая",
                6 to " июня",
                7 to " июля",
                8 to " августа",
                9 to " сентября",
                10 to " октября",
                11 to " ноября",
                12 to " декабря"
            )
            res.append(it.day)
            res.append(monthNumberToString[it.month.number])

            returnValue = res.toString()
        }

    return returnValue
}

@Preview
@Composable
private fun CashBackCardPreview() {
    val test = CashbackRule(
        category = CashbackRule.CashbackCategory.GasStationsFuel,
        expirationDate = "2026-15-04",
        percentage = 0.05,
        maxAmount = 0.0,
        bankCardName = "T-bank"
    )
    CashbackCard(
        category = test,
        onEditCategorySwipe = {},
        onDeleteCategorySwipe = {},
    )
}

@Composable
private fun EmptyCategories(onAddCategoryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(Res.string.categories_screen_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(Res.string.categories_screen_empty_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(
            onClick = onAddCategoryClick,
            modifier = Modifier
                .padding(top = 13.dp, bottom = 13.dp)
                .height(45.dp)
                .width(193.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
        ) {
            Text(
                text = stringResource(Res.string.categories_screen_add_category_button),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}
