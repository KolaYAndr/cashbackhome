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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import cashbackhome.composeapp.generated.resources.profile_chevron_right
import cashbackhome.composeapp.generated.resources.profile_icon_description
import cashbackhome.composeapp.generated.resources.profile_name
import cashbackhome.composeapp.generated.resources.search
import cashbackhome.composeapp.generated.resources.search_categories_placeholder
import cashbackhome.composeapp.generated.resources.search_icon_description
import cashbackhome.composeapp.generated.resources.tune
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.presentation.cards.CardsScreen
import org.homesharing.cashbackhome.presentation.categories.AddCategoryScreenRoot
import org.homesharing.cashbackhome.presentation.categories.CategoriesScreenRoot
import org.homesharing.cashbackhome.presentation.categories.CategoriesScreenState
import org.homesharing.cashbackhome.presentation.categories.CategoriesScreenViewModel
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
    onEditCategoryClick: (CashbackRuleDraft) -> Unit,
) {
    val tabState by viewModel.tabState.collectAsStateWithLifecycle()

    HomeScreen(
        selectedTab = tabState,
        onAddCategoryClick = onAddCategoryClick,
        onTabClick = { viewModel.switchToTab(it) },
        onEditCategoryClick = onEditCategoryClick
    )
}

@Composable
private fun HomeScreen(
    selectedTab: Tab,
    onAddCategoryClick: () -> Unit,
    onTabClick: (Tab) -> Unit,
    onEditCategoryClick: (CashbackRuleDraft) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    val fabEnterTransition = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
            ) + fadeIn(
        // Fades should be strictly linear/tweened, not bouncy
        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
    )

// 2. The Exit Animation (Fast & Direct)
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
                visible = scaffoldState.fabConfig.isVisible,
                enter = fabEnterTransition,
                exit = fabExitTransition
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = scaffoldState.fabConfig.onClick
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

            SearchAndSortBar()

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
                        onEditCategorySwipe = onEditCategoryClick
                    )
                    Tab.MyCards -> CardsScreen(
                        scaffoldState = scaffoldState,
                        onAddCardClick = {}
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
            .padding(vertical = 12.dp),
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
                    .size(24.dp),
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
private fun HomeScreenPreviewLight() {
    CashbackHomeTheme {
        HomeScreen(Tab.Categories, {}, {}, {})
    }
}
