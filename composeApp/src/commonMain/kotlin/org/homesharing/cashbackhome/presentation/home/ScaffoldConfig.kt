package org.homesharing.cashbackhome.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

internal data class FabConfig(
    val isVisible: Boolean = false,
    val onClick: () -> Unit = {}
)

internal data class SearchAndSortBarConfig(
    val isWidened: Boolean = false,
    val isGrid: Boolean = false,
)

internal class ScaffoldState {
    var fabConfig by mutableStateOf(FabConfig())
        private set
    var searchAndSortBarConfig by mutableStateOf(SearchAndSortBarConfig())
        private set

    fun updateFab(isVisible: Boolean, onClick: () -> Unit) {
        fabConfig = FabConfig(isVisible, onClick)
    }

    fun updateSearchAndSortBar(
        isWidened: Boolean,
        isGrid: Boolean,
    ) {
        searchAndSortBarConfig = SearchAndSortBarConfig(
            isWidened = isWidened,
            isGrid = isGrid
        )
    }
}

@Composable
internal fun rememberScaffoldState(): ScaffoldState {
    return remember { ScaffoldState() }
}
