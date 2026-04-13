package org.homesharing.cashbackhome.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

internal data class FabConfig(
    val isVisible: Boolean = false,
    val onClick: () -> Unit = {}
)

internal data class SearchAndSortBarConfig(
    val isWidened: Boolean = false,
    val isGrid: Boolean = false,
)

internal class ScaffoldState(private val isGrid: Boolean) {
    var fabConfig = mutableStateOf(FabConfig())
        private set
    var searchAndSortBarConfig = mutableStateOf(SearchAndSortBarConfig(isGrid = isGrid))
        private set

    fun updateFab(isVisible: Boolean, onClick: () -> Unit) {
        fabConfig.value = FabConfig(isVisible, onClick)
    }

    fun updateSearchAndSortBar(
        isWidened: Boolean,
        newGridState: Boolean,
    ) {
        searchAndSortBarConfig.value = SearchAndSortBarConfig(
            isWidened = isWidened,
            isGrid = newGridState,
        )
    }

    fun updateSearchAndSortBar(
        isWidened: Boolean,
    ) {
        searchAndSortBarConfig.value = SearchAndSortBarConfig(
            isWidened = isWidened,
            isGrid = this.searchAndSortBarConfig.value.isGrid,
        )
    }
}

@Composable
internal fun rememberScaffoldState(isGrid: Boolean = false): ScaffoldState {
    return remember { ScaffoldState(isGrid) }
}
