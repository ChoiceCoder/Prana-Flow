package com.pranaflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val PranaDarkScheme = darkColorScheme(
    primary = DarkPranaColors.gold,
    onPrimary = DarkPranaColors.background,
    primaryContainer = DarkPranaColors.goldDark,
    onPrimaryContainer = DarkPranaColors.goldPale,
    secondary = DarkPranaColors.goldDark,
    onSecondary = DarkPranaColors.goldPale,
    background = DarkPranaColors.background,
    onBackground = DarkPranaColors.goldPale,
    surface = DarkPranaColors.elevated,
    onSurface = DarkPranaColors.goldPale,
    surfaceVariant = DarkPranaColors.surface,
    onSurfaceVariant = DarkPranaColors.goldDimText,
    outline = DarkPranaColors.goldBorder
)

private val PranaLightScheme = lightColorScheme(
    primary = LightPranaColors.gold,
    onPrimary = Color.White,
    primaryContainer = LightPranaColors.surface,
    onPrimaryContainer = LightPranaColors.textPrimary,
    secondary = LightPranaColors.goldDark,
    onSecondary = Color.White,
    background = LightPranaColors.background,
    onBackground = LightPranaColors.textPrimary,
    surface = LightPranaColors.elevated,
    onSurface = LightPranaColors.textPrimary,
    surfaceVariant = LightPranaColors.surface,
    onSurfaceVariant = LightPranaColors.goldDimText,
    outline = LightPranaColors.goldBorder
)

@Composable
fun PranaFlowTheme(
    isDarkMode: Boolean = true,
    content: @Composable () -> Unit
) {
    val pranaColors = if (isDarkMode) DarkPranaColors else LightPranaColors
    val colorScheme = if (isDarkMode) PranaDarkScheme else PranaLightScheme

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !isDarkMode
        )
    }

    CompositionLocalProvider(LocalPranaColors provides pranaColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PranaTypography,
            content = content
        )
    }
}
