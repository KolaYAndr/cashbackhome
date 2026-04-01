package org.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.bankplaceholder
import cashbackhome.composeapp.generated.resources.categories_screen_add_category_button
import cashbackhome.composeapp.generated.resources.categories_screen_empty_description
import cashbackhome.composeapp.generated.resources.categories_screen_empty_title
import cashbackhome.composeapp.generated.resources.categories_screen_wrong_data_format
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
internal fun CategoriesScreenRoot(
    viewModel: CategoriesScreenViewModel = koinViewModel(),
    onAddCategoryClick: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is CategoriesScreenState.EmptyScreen -> EmptyCategories(onAddCategoryClick)
        is CategoriesScreenState.Categories -> CategoriesScreen(state.categories)
        is CategoriesScreenState.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun CategoriesScreen(
    categories: List<CashbackRule>
) {
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = categories,
            key = { category -> category.cashbackRuleId}
        ) { category ->
            CashbackCard(category)
        }
    }
}

@Composable
private fun CashbackCard(category: CashbackRule) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    painter = painterResource(Res.drawable.bankplaceholder),
                    contentDescription = null,
                    modifier = Modifier.size(53.dp),
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp) ) {
                    Text(
                        text = category.category.name,
                        lineHeight = 22.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Bank name",
                        lineHeight = 19.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = getExpirationDaysTitle(category.expirationDate),
                style = MaterialTheme.typography.bodySmall,
                color = getDateColor(category.expirationDate)
            )
            Text(
                text = getPercents(category.percentage),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
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
        title = "My supermarkets",
        category = CashbackRule.CashbackCategory.Groceries,
        expirationDate = "2026-15-04",
        percentage = 0.05,
        maxAmount = 0.0
    )
    CashbackCard(test)
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

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}