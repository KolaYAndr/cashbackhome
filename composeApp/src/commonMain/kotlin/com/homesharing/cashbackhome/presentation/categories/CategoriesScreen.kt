package com.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import homesharing.composeapp.generated.resources.Res
import homesharing.composeapp.generated.resources.add_category_button
import homesharing.composeapp.generated.resources.categories_empty_description
import homesharing.composeapp.generated.resources.categories_empty_title
import homesharing.composeapp.generated.resources.default_profile_picture
import homesharing.composeapp.generated.resources.filter_icon_description
import homesharing.composeapp.generated.resources.profile_chevron_right
import homesharing.composeapp.generated.resources.profile_icon_description
import homesharing.composeapp.generated.resources.profile_name
import homesharing.composeapp.generated.resources.search
import homesharing.composeapp.generated.resources.search_categories_placeholder
import homesharing.composeapp.generated.resources.search_icon_description
import homesharing.composeapp.generated.resources.tab_categories
import homesharing.composeapp.generated.resources.tab_my_cards
import homesharing.composeapp.generated.resources.tab_promotions
import homesharing.composeapp.generated.resources.tune
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun CategoriesScreenRoot(
    viewModel: CategoriesScreenViewModel = koinInject(),
    onCardsTabClick: () -> Unit,
    onPromotionsTabClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
) {
    CategoriesScreen(
        onCardsTabClick,
        onPromotionsTabClick,
        onAddCategoryClick,
    )
}

@Composable
private fun CategoriesScreen(
    onCardsTabClick: () -> Unit,
    onPromotionsTabClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .fitInside(
                    RectRulers.innermostOf(
                        WindowInsetsRulers.SafeDrawing.current,
                        WindowInsetsRulers.Ime.current,
                    ),
                ).background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))

        Header()

        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ScreenTab(
                title = stringResource(Res.string.tab_categories),
                selected = true,
                onClick = {},
            )
            ScreenTab(
                title = stringResource(Res.string.tab_my_cards),
                selected = false,
                onClick = onCardsTabClick,
            )
            ScreenTab(
                title = stringResource(Res.string.tab_promotions),
                selected = false,
                onClick = onPromotionsTabClick,
            )
        }

        SearchBar()

        EmptyCategories(onAddCategoryClick)
    }
}

@Composable
private fun Header(name: String = stringResource(Res.string.profile_name)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.default_profile_picture),
            contentDescription = stringResource(Res.string.profile_icon_description),
            modifier = Modifier.size(30.dp),
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.width(6.dp))

        Image(
            painter = painterResource(Res.drawable.profile_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(width = 10.dp, height = 12.dp),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ScreenTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier =
                Modifier
                    .height(1.dp)
                    .width(if (selected) 72.dp else 0.dp)
                    .background(if (selected) MaterialTheme.colorScheme.onBackground else Color.Transparent),
        )
    }
}

@Composable
private fun SearchBar() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.search),
                contentDescription = stringResource(Res.string.search_icon_description),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = stringResource(Res.string.search_categories_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            painter = painterResource(Res.drawable.tune),
            contentDescription = stringResource(Res.string.filter_icon_description),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier =
                Modifier
                    .padding(start = 10.dp)
                    .size(18.dp),
        )
    }
}

@Composable
private fun EmptyCategories(onAddCategoryClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.categories_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = stringResource(Res.string.categories_empty_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onAddCategoryClick,
            modifier =
                Modifier
                    .width(150.dp)
                    .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
        ) {
            Text(
                text = stringResource(Res.string.add_category_button),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Preview
@Composable
private fun CategoriesScreenPreview() {
    CashbackHomeTheme(false) {
        CategoriesScreen({}, {}, {})
    }
}
