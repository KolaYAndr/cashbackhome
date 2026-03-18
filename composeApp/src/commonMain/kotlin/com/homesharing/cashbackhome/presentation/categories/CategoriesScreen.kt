package com.homesharing.cashbackhome.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        modifier = Modifier
            .fillMaxSize()
            .fitInside(
                RectRulers.innermostOf(
                    WindowInsetsRulers.SafeDrawing.current,
                    WindowInsetsRulers.Ime.current,
                ),
            ).background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
    ) {
        Header()

        Row(
            modifier = Modifier.padding(bottom = 5.dp),
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

        SearchAndSortBar()

        EmptyCategories(onAddCategoryClick)
    }
}

@Composable
private fun Header(name: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.default_profile_picture),
            contentDescription = stringResource(Res.string.profile_icon_description),
            modifier = Modifier.size(30.dp),
        )

        Text(
            text = name ?: stringResource(Res.string.profile_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

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
        if (selected) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.colorUnderline(MaterialTheme.colorScheme.primary),
            )
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier) {
    Row(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.search),
            contentDescription = stringResource(Res.string.search_icon_description),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp),
        )

        Text(
            text = stringResource(Res.string.search_categories_placeholder),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchAndSortBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBar(Modifier.weight(1f))

        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(Res.drawable.tune),
                contentDescription = stringResource(Res.string.filter_icon_description),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
            )
        }
    }
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
            text = stringResource(Res.string.categories_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(Res.string.categories_empty_description),
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
                text = stringResource(Res.string.add_category_button),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

private fun Modifier.colorUnderline(
    color: Color,
    thickness: Dp = 1.dp,
    yOffset: Dp = 0.dp,          // move line up/down if needed
    horizontalInset: Dp = 0.dp,  // left/right inset
): Modifier = this.drawWithContent {
    drawContent()

    val stroke = thickness.toPx()
    val inset = horizontalInset.toPx()
    val y = size.height - stroke / 2 + yOffset.toPx()

    drawLine(
        color = color,
        start = Offset(inset, y),
        end = Offset(size.width - inset, y),
        strokeWidth = stroke,
    )
}


@Composable
@Preview
private fun CategoriesScreenPreviewLight() {
    CashbackHomeTheme {
        CategoriesScreen({}, {}, {})
    }
}

@Composable
@Preview
private fun CategoriesScreenPreviewDark() {
    CashbackHomeTheme(true) {
        CategoriesScreen({}, {}, {})
    }
}
