package org.homesharing.cashbackhome.presentation.home

import androidx.lifecycle.ViewModel
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.tab_categories
import cashbackhome.composeapp.generated.resources.tab_my_cards
import cashbackhome.composeapp.generated.resources.tab_promotions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

internal class HomeScreenViewModel : ViewModel() {
    private val _tabState: MutableStateFlow<Tab> = MutableStateFlow(Tab.Categories)
    val tabState: StateFlow<Tab> = _tabState.asStateFlow()

    fun switchToTab(tab: Tab) {
        _tabState.update { tab }
    }
}

private const val FIRST_TAB = 1
private const val SECOND_TAB = 2
private const val THIRD_TAB = 3

internal sealed class Tab(
    val name: StringResource,
    val hierarchyIndex: Int,
) {
    object Categories : Tab(
        name = Res.string.tab_categories,
        hierarchyIndex = FIRST_TAB,
    )

    object MyCards : Tab(
        name = Res.string.tab_my_cards,
        hierarchyIndex = SECOND_TAB,
    )

    object Promotions : Tab(
        name = Res.string.tab_promotions,
        hierarchyIndex = THIRD_TAB,
    )
}
