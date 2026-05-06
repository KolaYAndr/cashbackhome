package org.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
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
import cashbackhome.composeapp.generated.resources.arrow_drop_down
import cashbackhome.composeapp.generated.resources.categories_screen_add_category_button
import cashbackhome.composeapp.generated.resources.categories_screen_empty_description
import cashbackhome.composeapp.generated.resources.categories_screen_empty_title
import cashbackhome.composeapp.generated.resources.categories_screen_wrong_data_format
import cashbackhome.composeapp.generated.resources.delete
import cashbackhome.composeapp.generated.resources.edit
import kotlinx.coroutines.launch
import kotlinx.datetime.daysUntil
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.presentation.cards.BankBadge
import org.homesharing.cashbackhome.presentation.home.LoadingScreen
import org.homesharing.cashbackhome.presentation.home.ScaffoldState
import org.homesharing.cashbackhome.presentation.mapper.categoryName
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs
import kotlin.time.Clock

private data class CashbackMonth(
    val year: Int,
    val monthNumber: Int,
    val title: String,
)

@Composable
internal fun CategoriesScreenRoot(
    viewModel: CategoriesScreenViewModel = koinViewModel(),
    scaffoldState: ScaffoldState,
    onAddCategoryClick: () -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    scaffoldState.updateSearchAndSortBar(false)
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
    /*
    может дважды один и тот же месяц появиться, если года разные
    В таком случае они будут отсортированы по возрастанию года сначала.
     */
    val months = remember(categories) {
        categories.availableCashbackMonths()
    }
    var selectedMonth by remember(categories) {
        mutableStateOf(months.firstOrNull())
    }
    val activeMonth = selectedMonth ?: months.firstOrNull()
    val visibleCategories = remember(categories, activeMonth) {
        activeMonth?.let { month ->
            categories.filter { category -> category.belongsToMonth(month) }
        }.orEmpty()
    }

    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        if (activeMonth != null) {
            MonthSelector(
                selectedMonth = activeMonth,
                months = months,
                onMonthSelected = { selectedMonth = it },
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = visibleCategories,
                key = { category -> category.cashbackRuleId }
            ) { category ->
                CashbackCard(
                    category = category,
                    onEditCategorySwipe = { onEditCategorySwipe(category) },
                    onDeleteCategorySwipe = { onDeleteCategorySwipe(category.cashbackRuleId) }
                )
            }
        }
    }
}

@Composable
private fun MonthSelector(
    selectedMonth: CashbackMonth,
    months: List<CashbackMonth>,
    onMonthSelected: (CashbackMonth) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = selectedMonth.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                painter = painterResource(Res.drawable.arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = month.title,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    },
                )
            }
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
                    BankBadge(category.bankCardName)

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
                        )
                    }
                }
                getExpirationDateTitle(category.expirationDate)?.let { expirationTitle ->
                    Text(
                        text = expirationTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = getDateColor(category.expirationDate),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = getPercents(category.percentage),
                    style = MaterialTheme.typography.headlineMedium,
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

internal fun getPercents(x: Double) = "${(x * 100).toInt()}%"

@Composable
private fun getDateColor(
    expirationDate: String,
): Color {
    val expiration = parseDate(expirationDate)
    if (expiration != null) {
        val daysLeft = daysUntilExpiration(
            startDate = getCurrentDate(),
            expirationDate = expiration,
        )

        if (daysLeft > 15)
            return MaterialTheme.colorScheme.onSurfaceVariant
        return MaterialTheme.colorScheme.error
    }
    if (expirationDate.isBlank()) {
        return MaterialTheme.colorScheme.onSurfaceVariant
    }
    return MaterialTheme.colorScheme.errorContainer
}

private fun getCurrentDate(): LocalDate {
    val currentMoment = Clock.System.now()
    val datetimeInSystemZone = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInSystemZone.date
}

@Composable
private fun getExpirationDateTitle(
    expirationDate: String,
): String? {
    if (expirationDate.isBlank()) {
        return null
    }

    parseDate(expirationDate)?.let { expiration ->
        return "до ${expiration.day} ${russianMonthNameGenitive(expiration.month.number)}"
    }

    return stringResource(Res.string.categories_screen_wrong_data_format)
}

private fun daysUntilExpiration(
    startDate: LocalDate?,
    expirationDate: LocalDate,
): Int {
    val currentDate = getCurrentDate()
    val countFromDate = if (startDate != null && currentDate < startDate) {
        startDate
    } else {
        currentDate
    }
    return countFromDate.daysUntil(expirationDate).coerceAtLeast(0)
}

private fun parseDate(value: String?): LocalDate? =
    runCatching {
        LocalDate.parse(value.orEmpty())
    }.getOrNull()

private fun List<CashbackRule>.availableCashbackMonths(): List<CashbackMonth> =
    flatMap { category -> category.availableMonths() }
        .distinct()
        .sortedWith(compareBy<CashbackMonth> { it.year }.thenBy { it.monthNumber })

private fun CashbackRule.belongsToMonth(month: CashbackMonth): Boolean =
    dateRange()?.let { (start, expiration) ->
        start <= month.lastDate && expiration >= month.firstDate
    } == true

private fun CashbackRule.availableMonths(): List<CashbackMonth> {
    val (start, expiration) = dateRange() ?: return emptyList()
    return monthsBetween(start, expiration)
}

private fun CashbackRule.dateRange(): Pair<LocalDate, LocalDate>? {
    val start = if (startDate.isBlank()) {
        null
    } else {
        parseDate(startDate) ?: return null
    }
    val expiration = if (expirationDate.isBlank()) {
        null
    } else {
        parseDate(expirationDate) ?: return null
    }
    if (expiration == null || start == null || expiration < start) {
        return null
    }
    return start to expiration
}

private fun monthsBetween(
    start: LocalDate,
    end: LocalDate,
): List<CashbackMonth> {
    val result = mutableListOf<CashbackMonth>()
    var year = start.year
    var monthNumber = start.month.number

    while (year < end.year || (year == end.year && monthNumber <= end.month.number)) {
        result += CashbackMonth(
            year = year,
            monthNumber = monthNumber,
            title = russianMonthName(monthNumber),
        )

        if (monthNumber == 12) {
            year += 1
            monthNumber = 1
        } else {
            monthNumber += 1
        }
    }

    return result
}

private val CashbackMonth.firstDate: LocalDate
    get() = LocalDate(year, monthNumber, 1)

private val CashbackMonth.lastDate: LocalDate
    get() = LocalDate(year, monthNumber, daysInMonth(year, monthNumber))

private fun daysInMonth(
    year: Int,
    monthNumber: Int,
): Int =
    when (monthNumber) {
        2 -> if (isLeapYear(year)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

private fun isLeapYear(year: Int): Boolean =
    year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

private fun russianMonthName(monthNumber: Int): String =
    when (monthNumber) {
        1 -> "Январь"
        2 -> "Февраль"
        3 -> "Март"
        4 -> "Апрель"
        5 -> "Май"
        6 -> "Июнь"
        7 -> "Июль"
        8 -> "Август"
        9 -> "Сентябрь"
        10 -> "Октябрь"
        11 -> "Ноябрь"
        12 -> "Декабрь"
        else -> ""
    }

private fun russianMonthNameGenitive(monthNumber: Int): String =
    when (monthNumber) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> ""
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
            shape = RoundedCornerShape(14.dp),
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

@Preview
@Composable
private fun CashBackCardPreview() {
    val test = CashbackRule(
        category = CashbackRule.CashbackCategory.GasStationsFuel,
        startDate = "2026-04-15",
        expirationDate = "2026-15-04",
        percentage = 0.05,
        maxAmount = 0.0,
        bankCardName = "Т-Банк",
        bankCardId = 0L
    )
    CashbackHomeTheme {
        CashbackCard(
            category = test,
            onEditCategorySwipe = {},
            onDeleteCategorySwipe = {},
        )
    }
}

@Preview
@Composable
private fun CategoryScreeenPreview() {
    val test = CashbackRule(
        category = CashbackRule.CashbackCategory.GasStationsFuel,
        startDate = "2026-04-15",
        expirationDate = "2026-11-04",
        percentage = 0.05,
        maxAmount = 0.0,
        bankCardName = "Т-Банк",
        bankCardId = 0L
    )
    CashbackHomeTheme {
        CategoriesScreen(
            categories = listOf(test),
            onEditCategorySwipe = {},
            onDeleteCategorySwipe = {}
        )
    }
}
