package com.pranaflow.ui.breathing.geometry

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathState
import com.pranaflow.ui.theme.Gold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Flower of Life — used for Bhramari (Bee Breath).
 *
 * The Flower of Life is one of the oldest sacred geometry symbols:
 * overlapping circles arranged in a hexagonal pattern, representing
 * the interconnectedness of all life.
 *
 * Visual structure:
 * - Central seed circle
 * - 6 primary petals (first ring)
 * - 12 secondary petals (second ring)
 * - Enclosing outer rings
 * - Pulsing resonance waves radiating outward (the "hum")
 * - Intersection glow points (Vesica Piscis highlights)
 *
 * Breathing behavior:
 * INHALE → circles expand outward, bloom open
 * HOLD   → resonance ripples emanate from center
 * EXHALE → long, slow contraction (the humming exhale)
 * HOLD   → rest at seed
 */
@Composable
fun FlowerOfLifeGeometry(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flower_of_life")

    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(80000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fol_rotation"
    )

    // Resonance ripple (expanding ring during hold/exhale)
    val ripplePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fol_ripple"
    )

    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fol_shimmer"
    )

    val scaleFactor = when (breathState.phase) {
        BreathPhase.INHALE -> 0.45f + easedProgress * 0.55f
        BreathPhase.HOLD_IN -> 1.0f * shimmer
        BreathPhase.EXHALE -> 1.0f - easedProgress * 0.55f
        BreathPhase.HOLD_OUT -> 0.45f
        BreathPhase.IDLE -> 0.65f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val c = center
        val baseR = size.minDimension * 0.4f
        val petalRadius = baseR * scaleFactor * 0.38f

        // ── Background Glow ──
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.12f * glowIntensity * scaleFactor),
                    Gold.copy(alpha = 0.03f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = baseR * 2f
            ),
            radius = baseR * 2f,
            center = c
        )

        // ── Resonance Ripples (during hold & exhale — the "hum" visualization) ──
        if (breathState.phase == BreathPhase.HOLD_IN ||
            breathState.phase == BreathPhase.EXHALE
        ) {
            for (wave in 0..2) {
                val waveProgress = (ripplePhase + wave * 0.33f) % 1f
                val waveR = baseR * 0.3f + waveProgress * baseR * 1.2f
                val waveAlpha = (1f - waveProgress) * 0.15f * glowIntensity
                drawCircle(
                    color = Gold.copy(alpha = waveAlpha),
                    radius = waveR,
                    center = c,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }

        // ── Outer Enclosing Circles ──
        for (i in 0..1) {
            val outerR = baseR * scaleFactor * (0.85f + i * 0.15f)
            drawCircle(
                color = Gold.copy(alpha = (0.2f - i * 0.06f) * glowIntensity),
                radius = outerR,
                center = c,
                style = Stroke(width = 0.7.dp.toPx())
            )
        }

        // ── Central Seed Circle ──
        drawCircle(
            color = Gold.copy(alpha = 0.5f * glowIntensity),
            radius = petalRadius,
            center = c,
            style = Stroke(width = 1.2.dp.toPx())
        )

        // ── First Ring: 6 Primary Petals ──
        rotate(ambientRotation * 0.1f, pivot = c) {
            drawPetalRing(
                center = c,
                ringRadius = petalRadius,
                petalRadius = petalRadius,
                count = 6,
                alpha = 0.45f * glowIntensity,
                strokeWidth = 1.dp.toPx()
            )
        }

        // ── Second Ring: 12 Secondary Petals ──
        rotate(-ambientRotation * 0.05f, pivot = c) {
            drawPetalRing(
                center = c,
                ringRadius = petalRadius * 1.73f,   // sqrt(3) ≈ hex spacing
                petalRadius = petalRadius,
                count = 12,
                alpha = 0.25f * glowIntensity,
                strokeWidth = 0.7.dp.toPx()
            )
        }

        // ── Vesica Piscis Highlight Points ──
        // Glow at the intersections of the first ring circles
        for (i in 0 until 6) {
            val angle1 = (2.0 * PI * i / 6) + Math.toRadians(ambientRotation.toDouble() * 0.1)
            val angle2 = (2.0 * PI * ((i + 1) % 6) / 6) + Math.toRadians(ambientRotation.toDouble() * 0.1)

            // Midpoint between adjacent petal centers (approximation of vesica piscis center)
            val p1x = c.x + petalRadius * cos(angle1).toFloat()
            val p1y = c.y + petalRadius * sin(angle1).toFloat()
            val p2x = c.x + petalRadius * cos(angle2).toFloat()
            val p2y = c.y + petalRadius * sin(angle2).toFloat()

            val midX = (p1x + p2x) / 2f
            val midY = (p1y + p2y) / 2f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = 0.5f * glowIntensity * shimmer),
                        Color.Transparent
                    ),
                    center = Offset(midX, midY),
                    radius = 6.dp.toPx()
                ),
                radius = 6.dp.toPx(),
                center = Offset(midX, midY)
            )
            drawCircle(
                color = Gold.copy(alpha = 0.7f * glowIntensity),
                radius = 1.5.dp.toPx(),
                center = Offset(midX, midY)
            )
        }

        // ── Central Orb ──
        val orbR = 5.dp.toPx() * scaleFactor
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.9f * glowIntensity),
                    Gold.copy(alpha = 0.3f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = orbR * 5
            ),
            radius = orbR * 5,
            center = c
        )
        drawCircle(
            color = Gold.copy(alpha = 0.95f),
            radius = orbR,
            center = c
        )
    }
}

/**
 * Draw a ring of overlapping circles — the core pattern of the Flower of Life.
 */
private fun DrawScope.drawPetalRing(
    center: Offset,
    ringRadius: Float,
    petalRadius: Float,
    count: Int,
    alpha: Float,
    strokeWidth: Float
) {
    for (i in 0 until count) {
        val angle = 2.0 * PI * i / count
        val px = center.x + ringRadius * cos(angle).toFloat()
        val py = center.y + ringRadius * sin(angle).toFloat()
        drawCircle(
            color = Gold.copy(alpha = alpha),
            radius = petalRadius,
            center = Offset(px, py),
            style = Stroke(width = strokeWidth)
        )
    }
}
