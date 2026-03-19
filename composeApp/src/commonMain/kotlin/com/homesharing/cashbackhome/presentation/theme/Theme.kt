package com.homesharing.cashbackhome.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        background = LightBackground,
        onBackground = LightOnBackground,
        surface = LightSurface,
        onSurface = LightOnSurface,
        onSurfaceVariant = TextSecondary,
        primary = AccentPrimary,
        onPrimary = LightOnBackground,
        primaryContainer = AccentPrimaryPressed,
        onPrimaryContainer = LightOnBackground,
        error = WarningExpiration,
        errorContainer = Delete,
        secondary = ButtonPrimary,
        onSecondary = OnButtonPrimary,
        secondaryContainer = ButtonPrimaryPressed,
        onSecondaryContainer = OnButtonPrimary,
        tertiary = ButtonSecondary,
        onTertiary = OnButtonSecondary,
        tertiaryContainer = ButtonSecondaryPressed,
        onTertiaryContainer = OnButtonSecondary,
    )

private val DarkColorScheme =
    darkColorScheme(
        background = DarkBackground,
        onBackground = DarkOnBackground,
        surface = DarkSurface,
        onSurface = DarkOnSurface,
        onSurfaceVariant = TextSecondary,
        primary = AccentPrimary,
        onPrimary = LightOnBackground,
        primaryContainer = AccentPrimaryPressed,
        onPrimaryContainer = LightOnBackground,
        error = WarningExpiration,
        errorContainer = Delete,
        secondary = ButtonPrimary,
        onSecondary = OnButtonPrimary,
        secondaryContainer = ButtonPrimaryPressed,
        onSecondaryContainer = OnButtonPrimary,
        tertiary = ButtonSecondary,
        onTertiary = OnButtonSecondary,
        tertiaryContainer = ButtonSecondaryPressed,
        onTertiaryContainer = OnButtonSecondary,
    )

@Composable
internal fun CashbackHomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        content = content,
    )
}
