package com.pranaflow.ui.breathing.geometry

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranaflow.R
import com.pranaflow.model.ChakraInfo
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathState
import com.pranaflow.ui.theme.Gold
import com.pranaflow.ui.theme.GoldPale
import kotlin.math.abs

/**
 * Original image dimensions (bg_meditation.png).
 * Chakra bodyY values are fractions of THIS height, not the screen.
 */
private const val IMG_W = 768f
private const val IMG_H = 1376f

/**
 * All 7 chakra positions as fractions of the IMAGE height.
 * These match the bodyY values in Models.kt exactly.
 */
private data class ChakraDot(val imageY: Float, val mantra: String)

private val ALL_CHAKRAS = listOf(
    ChakraDot(0.3016f, "ॐ"),   // Sahasrara
    ChakraDot(0.3458f, ""),   // Ajna
    ChakraDot(0.3983f, "हं"),   // Vishuddha
    ChakraDot(0.4593f, "यं"),   // Anahata
    ChakraDot(0.5145f, "रं"),   // Manipura
    ChakraDot(0.5509f, "वं"),   // Svadhisthana
    ChakraDot(0.5872f, "लं")    // Muladhara
)

/**
 * Maps a Y position from image-space to screen-space,
 * accounting for ContentScale.Crop with Alignment.Center.
 *
 * This ensures chakra dots land on the exact body positions
 * regardless of screen size or aspect ratio.
 */
private fun imageYToScreenY(imageYFraction: Float, screenW: Float, screenH: Float): Float {
    val scale = maxOf(screenW / IMG_W, screenH / IMG_H)
    val scaledH = IMG_H * scale
    val offsetY = (scaledH - screenH) / 2f
    return imageYFraction * scaledH - offsetY
}

private fun imageXToScreenX(imageXFraction: Float, screenW: Float, screenH: Float): Float {
    val scale = maxOf(screenW / IMG_W, screenH / IMG_H)
    val scaledW = IMG_W * scale
    val offsetX = (scaledW - screenW) / 2f
    return imageXFraction * scaledW - offsetX
}

@Composable
fun ChakraBodyVisualization(
    chakraInfo: ChakraInfo,
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    isDark: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "chakra")

    val mantraPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chakra_pulse"
    )

    val ringExpand by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chakra_ring"
    )

    val glowBreath by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chakra_glow"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // ── Body Silhouette ──
        Image(
            painter = painterResource(id = if (isDark) R.drawable.bg_meditation else R.drawable.bg_meditation_light),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isDark) 0.5f else 0.25f),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // ── Chakra Dots + Active Glow ──
        val chakraColor = Color(chakraInfo.colorHex)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sw = size.width
            val sh = size.height

            // Image center X (body midline) → 0.5 of image
            val centerX = imageXToScreenX(0.5f, sw, sh)

            // Active chakra screen position
            val activeScreenY = imageYToScreenY(chakraInfo.bodyY, sw, sh)

            // Draw ALL 7 chakra dots
            for (dot in ALL_CHAKRAS) {
                val dotScreenY = imageYToScreenY(dot.imageY, sw, sh)
                val isActive = abs(dot.imageY - chakraInfo.bodyY) < 0.005f

                if (!isActive) {
                    // Inactive: small dim gold dot
                    drawCircle(
                        color = Gold.copy(alpha = 0.18f * glowIntensity),
                        radius = 4.dp.toPx(),
                        center = Offset(centerX, dotScreenY)
                    )
                    drawCircle(
                        color = Gold.copy(alpha = 0.08f * glowIntensity),
                        radius = 8.dp.toPx(),
                        center = Offset(centerX, dotScreenY),
                        style = Stroke(width = 0.5.dp.toPx())
                    )
                }
            }

            // ── Active Chakra Glow ──
            drawActiveChakraGlow(
                cx = centerX,
                cy = activeScreenY,
                ringExpand = ringExpand,
                glowBreath = glowBreath,
                mantraPulse = mantraPulse,
                glowIntensity = glowIntensity,
                chakraColor = chakraColor
            )
        }

        // ── Devanagari Mantra Text ──
        val density = LocalDensity.current
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val sw = constraints.maxWidth.toFloat()
            val sh = constraints.maxHeight.toFloat()
            val screenY = imageYToScreenY(chakraInfo.bodyY, sw, sh)
            val mantraFontSize = 32f * mantraPulse

            if (chakraInfo.mantra.isNotEmpty() && !chakraInfo.mantra.all { it.isDigit() }) Text(
                text = chakraInfo.mantra,
                style = TextStyle(
                    fontSize = mantraFontSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = chakraColor.copy(alpha = mantraPulse * glowIntensity)
                ),
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = screenY.toInt() + with(density) { 20.dp.roundToPx() }
                        )
                    }
            )
        }
    }
}

/**
 * Draw the glow rings, radial gradient, and center dot for the active chakra.
 */
private fun DrawScope.drawActiveChakraGlow(
    cx: Float,
    cy: Float,
    ringExpand: Float,
    glowBreath: Float,
    mantraPulse: Float,
    glowIntensity: Float,
    chakraColor: Color
) {
    val center = Offset(cx, cy)

    // Expanding rings (3 waves) — chakra color
    for (wave in 0..2) {
        val waveProgress = (ringExpand + wave * 0.33f) % 1f
        val waveR = 15.dp.toPx() + waveProgress * 50.dp.toPx() * glowBreath
        val waveAlpha = (1f - waveProgress) * 0.35f * glowIntensity
        drawCircle(
            color = chakraColor.copy(alpha = waveAlpha),
            radius = waveR,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )
    }

    // Core radial glow — chakra color
    val coreR = 35.dp.toPx() * glowBreath
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                chakraColor.copy(alpha = 0.6f * glowIntensity * glowBreath),
                chakraColor.copy(alpha = 0.2f * glowIntensity),
                chakraColor.copy(alpha = 0.05f * glowIntensity),
                Color.Transparent
            ),
            center = center,
            radius = coreR * 2.5f
        ),
        radius = coreR * 2.5f,
        center = center
    )

    // Bright center dot — chakra color
    drawCircle(
        color = chakraColor.copy(alpha = 0.95f * mantraPulse),
        radius = 6.dp.toPx(),
        center = center
    )
}
