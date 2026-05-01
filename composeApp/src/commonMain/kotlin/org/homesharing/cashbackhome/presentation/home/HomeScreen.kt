package org.homesharing.cashbackhome.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.add
import cashbackhome.composeapp.generated.resources.chevron_right_icon_description
import cashbackhome.composeapp.generated.resources.default_profile_picture
import cashbackhome.composeapp.generated.resources.filter_icon_description
import cashbackhome.composeapp.generated.resources.grid_view
import cashbackhome.composeapp.generated.resources.grid_view_icon_description
import cashbackhome.composeapp.generated.resources.list_view_icon_description
import cashbackhome.composeapp.generated.resources.profile_chevron_right
import cashbackhome.composeapp.generated.resources.profile_icon_description
import cashbackhome.composeapp.generated.resources.profile_name
import cashbackhome.composeapp.generated.resources.search
import cashbackhome.composeapp.generated.resources.search_cards_placeholder
import cashbackhome.composeapp.generated.resources.search_categories_placeholder
import cashbackhome.composeapp.generated.resources.search_promotions_placeholder
import cashbackhome.composeapp.generated.resources.tune
import cashbackhome.composeapp.generated.resources.view_list
import co.touchlab.kermit.Logger
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.presentation.cards.CardsScreenRoot
import org.homesharing.cashbackhome.presentation.categories.CategoriesScreenRoot
import org.homesharing.cashbackhome.presentation.categories.TipText
import org.homesharing.cashbackhome.presentation.promotions.PromotionsScreen
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val ANIMATION_DURATION = 200

@Composable
internal fun HomeScreenRoot(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onAddCategoryClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
    onEditCardClick: (BankCard) -> Unit,
    onCardClick: (BankCard) -> Unit,
) {
    val tabState by viewModel.tabState.collectAsStateWithLifecycle()
    val isGrid by viewModel.isGrid.collectAsStateWithLifecycle()
    Logger.i { "$tabState. $isGrid" }

    HomeScreen(
        selectedTab = tabState,
        isGrid = isGrid,
        onGridStateChanged = viewModel::updateIsGridState,
        onAddCategoryClick = onAddCategoryClick,
        onAddCardClick = onAddCardClick,
        onTabClick = { viewModel.switchToTab(it) },
        onEditCategoryClick = onEditCategoryClick,
        onEditCardClick = onEditCardClick,
        onCardClick = onCardClick,
    )
}

@Composable
private fun HomeScreen(
    selectedTab: Tab,
    isGrid: Boolean,
    onGridStateChanged: (Boolean) -> Unit,
    onAddCategoryClick: () -> Unit,
    onAddCardClick: () -> Unit,
    onTabClick: (Tab) -> Unit,
    onEditCategoryClick: (CashbackRule) -> Unit,
    onEditCardClick: (BankCard) -> Unit,
    onCardClick: (BankCard) -> Unit,
) {
    val scaffoldState = rememberScaffoldState(isGrid)
    Logger.i { scaffoldState.searchAndSortBarConfig.value.toString() }

    val fabEnterTransition = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
            ) + fadeIn(animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
    )

    val fabExitTransition = scaleOut(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    ) + fadeOut(
        animationSpec = tween(durationMillis = 100)
    )

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = scaffoldState.fabConfig.value.isVisible,
                enter = fabEnterTransition,
                exit = fabExitTransition
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = scaffoldState.fabConfig.value.onClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.add),
                        contentDescription = "Add",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
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
            val tabs = listOf(
                Tab.Categories,
                Tab.MyCards,
                Tab.Promotions
            )

            Row(
                modifier = Modifier.padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                tabs.forEach { tab ->
                    ScreenTab(
                        title = stringResource(tab.name),
                        selected = tab == selectedTab,
                        onClick = { onTabClick(tab) }
                    )
                }
            }

            SearchAndSortBar(
                selectedTab = selectedTab,
                searchAndSortBarConfig = scaffoldState.searchAndSortBarConfig.value,
                onGridIconClick = {
                    onGridStateChanged(true)
                    scaffoldState.updateSearchAndSortBar(
                        isWidened = true,
                        newGridState = true
                    )
                },
                onListIconClick = {
                    onGridStateChanged(false)
                    scaffoldState.updateSearchAndSortBar(
                        isWidened = true,
                        newGridState = false
                    )
                }
            )

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState.hierarchyIndex > initialState.hierarchyIndex) {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(durationMillis = ANIMATION_DURATION)
                        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(ANIMATION_DURATION)
                            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
                        )
                    } else {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = ANIMATION_DURATION)
                        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))).togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(ANIMATION_DURATION)
                            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
                        )
                    }
                },
                contentKey = { selectedTab }
            ) {
                when (selectedTab) {
                    Tab.Categories -> CategoriesScreenRoot(
                        scaffoldState = scaffoldState,
                        onAddCategoryClick = onAddCategoryClick,
                        onEditCategoryClick = onEditCategoryClick
                    )
                    Tab.MyCards -> CardsScreenRoot(
                        scaffoldState = scaffoldState,
                        onAddCardClick = onAddCardClick,
                        onEditCard = onEditCardClick,
                        onCardClick = onCardClick,
                    )
                    Tab.Promotions -> PromotionsScreen(scaffoldState)
                }
            }
        }
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
            contentDescription = stringResource(Res.string.chevron_right_icon_description),
            modifier = Modifier.size(width = 10.dp, height = 12.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
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
            color = if (selected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = if (selected) {
                Modifier.colorUnderline(MaterialTheme.colorScheme.primary)
            } else {
                Modifier
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchBarX(
    modifier: Modifier,
    placeholder: String,
    searchQuery: String? = null,
    onValueChange: (String) -> Unit = {},
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 8.dp),
        onValueChange = onValueChange,
        singleLine = true,
        value = searchQuery.orEmpty(),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.orEmpty().isEmpty()) {
                        TipText(placeholder, false)
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun SearchAndSortBar(
    selectedTab: Tab,
    searchAndSortBarConfig: SearchAndSortBarConfig,
    onGridIconClick: () -> Unit,
    onListIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBarX(
            modifier = Modifier.weight(1f),
            placeholder = stringResource(
                when (selectedTab) {
                    Tab.Categories -> Res.string.search_categories_placeholder
                    Tab.MyCards -> Res.string.search_cards_placeholder
                    Tab.Promotions -> Res.string.search_promotions_placeholder
                }
            )
        )

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            if (!searchAndSortBarConfig.isWidened) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tune),
                        contentDescription = stringResource(Res.string.filter_icon_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp),
                    )
                }
            } else {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tune),
                        contentDescription = stringResource(Res.string.filter_icon_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp),
                    )
                }

                IconButton(
                    onClick = onGridIconClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.grid_view),
                        contentDescription = stringResource(Res.string.grid_view_icon_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp),
                    )
                }

                IconButton(
                    onClick = onListIconClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.view_list),
                        contentDescription = stringResource(Res.string.list_view_icon_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

internal fun Modifier.colorUnderline(
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
private fun HomeScreenPreviewLight() {
    CashbackHomeTheme {
        HomeScreen(Tab.Categories,
            false,
            {},
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}


@Composable
internal fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}
