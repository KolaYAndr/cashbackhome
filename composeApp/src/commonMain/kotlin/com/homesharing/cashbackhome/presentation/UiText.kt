package com.homesharing.cashbackhome.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

internal sealed interface UiText {
    class StringResourceId(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is StringResourceId -> stringResource(resource = id, formatArgs = args)
        }
    }
}