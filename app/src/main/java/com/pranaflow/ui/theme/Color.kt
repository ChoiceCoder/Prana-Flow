package com.pranaflow.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Complete color palette for Prana Flow.
 * All UI components read from [LocalPranaColors] so dark/light switch is automatic.
 */
data class PranaColors(
    val background: Color,
    val elevated: Color,
    val surface: Color,
    val gold: Color,
    val goldBright: Color,
    val goldDark: Color,
    val goldPale: Color,
    val goldGlow: Color,
    val goldBorder: Color,
    val goldDimText: Color,
    val goldSoftText: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val divider: Color,
    val isDark: Boolean
)

val DarkPranaColors = PranaColors(
    background = Color(0xFF0A0A0A),
    elevated = Color(0xFF111111),
    surface = Color(0xFF161310),
    gold = Color(0xFFD4AF37),
    goldBright = Color(0xFFE8C84B),
    goldDark = Color(0xFF9A7B1F),
    goldPale = Color(0xFFF0D98C),
    goldGlow = Color(0x1AD4AF37),
    goldBorder = Color(0x33D4AF37),
    goldDimText = Color(0x80D4AF37),
    goldSoftText = Color(0xB3D4AF37),
    textPrimary = Color(0xFFF0D98C),
    textSecondary = Color(0x80D4AF37),
    textMuted = Color(0xFF5A5040),
    divider = Color(0xFF1E1A14),
    isDark = true
)

val LightPranaColors = PranaColors(
    background = Color(0xFFEDE4D3),      // Warm cream
    elevated = Color(0xFFF5F0E5),         // Light warm card
    surface = Color(0xFFE3DAC6),          // Warm surface
    gold = Color(0xFF8A6A12),             // Dark rich gold
    goldBright = Color(0xFFA07C18),       // Medium gold
    goldDark = Color(0xFF5C4810),         // Very deep gold
    goldPale = Color(0xFF14100A),         // Very dark brown text
    goldGlow = Color(0x408A6A12),         // Strong gold glow
    goldBorder = Color(0xFFB8A878),       // Darker visible border
    goldDimText = Color(0xFF504020),      // Dark warm secondary
    goldSoftText = Color(0xFF403518),     // Dark body text
    textPrimary = Color(0xFF14100A),      // Near-black
    textSecondary = Color(0xFF504020),    // Dark warm secondary
    textMuted = Color(0xFF887858),        // Muted but readable
    divider = Color(0xFFC8BC98),          // Stronger warm divider
    isDark = false
)

val LocalPranaColors = staticCompositionLocalOf { DarkPranaColors }

// ── Backward-compatible accessors ───────────────────────────────
// Existing code uses these directly. They now delegate to the local.
// In composables, use PranaTheme.colors.xxx instead for new code.

val Void            get() = DarkPranaColors.background
val VoidElevated    get() = DarkPranaColors.elevated
val VoidSurface     get() = DarkPranaColors.surface
val Gold            get() = DarkPranaColors.gold
val GoldBright      get() = DarkPranaColors.goldBright
val GoldDark        get() = DarkPranaColors.goldDark
val GoldPale        get() = DarkPranaColors.goldPale
val GoldGlow        get() = DarkPranaColors.goldGlow
val GoldBorder      get() = DarkPranaColors.goldBorder
val GoldDimText     get() = DarkPranaColors.goldDimText
val GoldSoftText    get() = DarkPranaColors.goldSoftText
val TextPrimary     get() = DarkPranaColors.textPrimary
val TextSecondary   get() = DarkPranaColors.textSecondary
val TextMuted       get() = DarkPranaColors.textMuted
val DividerColor    get() = DarkPranaColors.divider

/**
 * Access current theme colors inside composables.
 */
object PranaTheme {
    val colors: PranaColors
        @Composable get() = LocalPranaColors.current
}
