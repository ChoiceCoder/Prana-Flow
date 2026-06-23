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
 * Triple Lotus — used for Dirga Pranayam (Three-Part Breath).
 *
 * Dirga is the complete yogic breath that fills three regions
 * of the body sequentially: belly → ribs → chest.
 * This geometry reflects that three-part ascent.
 *
 * Visual structure:
 * - Inner Lotus (Belly / Muladhara): 6 petals, tightest ring
 * - Middle Lotus (Ribs / Manipura): 10 petals, mid ring
 * - Outer Lotus (Chest / Anahata): 14 petals, widest ring
 * - Radial connecting lines (prana channels between lotuses)
 * - Ascending energy particles that rise through the three rings
 * - Central bindu with triple-ring glow
 *
 * Breathing behavior:
 * INHALE → three rings illuminate sequentially bottom-up
 *          (progress 0–0.33 = inner, 0.33–0.66 = middle, 0.66–1.0 = outer)
 * HOLD   → all three rings fully lit, pulsing
 * EXHALE → rings dim sequentially top-down
 * HOLD   → rest at seed
 */
@Composable
fun TripleLotusGeometry(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "triple_lotus")

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(45000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tl_inner_rot"
    )

    val middleRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tl_mid_rot"
    )

    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(75000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tl_outer_rot"
    )

    // Particle ascent position
    val particleAscent by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tl_particle"
    )

    val holdPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tl_hold_pulse"
    )

    // ── Compute ring activation levels (0..1) ──
    // During INHALE: rings light up sequentially
    // During EXHALE: rings dim sequentially (reverse)
    val (innerActive, middleActive, outerActive) = computeRingActivations(
        breathState.phase, easedProgress, holdPulse
    )

    val scaleFactor = when (breathState.phase) {
        BreathPhase.INHALE -> 0.6f + easedProgress * 0.4f
        BreathPhase.HOLD_IN -> 1.0f
        BreathPhase.EXHALE -> 1.0f - easedProgress * 0.4f
        BreathPhase.HOLD_OUT -> 0.6f
        BreathPhase.IDLE -> 0.75f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val c = center
        val baseR = size.minDimension * 0.42f

        val innerR = baseR * 0.28f * scaleFactor
        val middleR = baseR * 0.55f * scaleFactor
        val outerR = baseR * 0.85f * scaleFactor

        // ── Background Glow ──
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.1f * glowIntensity * scaleFactor),
                    Color.Transparent
                ),
                center = c,
                radius = baseR * 1.8f
            ),
            radius = baseR * 1.8f,
            center = c
        )

        // ── Radial Prana Channels (connecting lines between rings) ──
        val channelCount = 6
        for (i in 0 until channelCount) {
            val angle = (2.0 * PI * i / channelCount).toFloat()
            val innerPoint = Offset(
                c.x + innerR * cos(angle),
                c.y + innerR * sin(angle)
            )
            val outerPoint = Offset(
                c.x + outerR * 1.1f * cos(angle),
                c.y + outerR * 1.1f * sin(angle)
            )
            drawLine(
                color = Gold.copy(alpha = 0.08f * glowIntensity),
                start = innerPoint,
                end = outerPoint,
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // ── Outer Lotus (Chest / Anahata) — 14 petals ──
        rotate(outerRotation, pivot = c) {
            drawLotusLayer(
                center = c,
                ringRadius = outerR,
                petalCount = 14,
                petalLength = outerR * 0.18f,
                petalWidth = outerR * 0.07f,
                activation = outerActive,
                glowIntensity = glowIntensity
            )
        }
        // Ring circle
        drawCircle(
            color = Gold.copy(alpha = 0.15f * glowIntensity + outerActive * 0.25f * glowIntensity),
            radius = outerR,
            center = c,
            style = Stroke(width = 0.6.dp.toPx())
        )

        // ── Middle Lotus (Ribs / Manipura) — 10 petals ──
        rotate(middleRotation, pivot = c) {
            drawLotusLayer(
                center = c,
                ringRadius = middleR,
                petalCount = 10,
                petalLength = middleR * 0.22f,
                petalWidth = middleR * 0.09f,
                activation = middleActive,
                glowIntensity = glowIntensity
            )
        }
        drawCircle(
            color = Gold.copy(alpha = 0.15f * glowIntensity + middleActive * 0.3f * glowIntensity),
            radius = middleR,
            center = c,
            style = Stroke(width = 0.7.dp.toPx())
        )

        // ── Inner Lotus (Belly / Muladhara) — 6 petals ──
        rotate(innerRotation, pivot = c) {
            drawLotusLayer(
                center = c,
                ringRadius = innerR,
                petalCount = 6,
                petalLength = innerR * 0.3f,
                petalWidth = innerR * 0.12f,
                activation = innerActive,
                glowIntensity = glowIntensity
            )
        }
        drawCircle(
            color = Gold.copy(alpha = 0.2f * glowIntensity + innerActive * 0.35f * glowIntensity),
            radius = innerR,
            center = c,
            style = Stroke(width = 0.8.dp.toPx())
        )

        // ── Ascending Energy Particles ──
        if (breathState.phase == BreathPhase.INHALE ||
            breathState.phase == BreathPhase.HOLD_IN
        ) {
            drawAscendingParticles(
                center = c,
                innerR = innerR,
                outerR = outerR,
                ascent = particleAscent,
                glowIntensity = glowIntensity
            )
        }

        // ── Ring Activation Glow (fills between rings when active) ──
        if (innerActive > 0.5f) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = 0.06f * innerActive * glowIntensity),
                        Color.Transparent
                    ),
                    center = c,
                    radius = middleR
                ),
                radius = middleR,
                center = c
            )
        }

        // ── Central Bindu with Triple Ring ──
        val binduR = 4.dp.toPx() * scaleFactor
        // Triple micro-rings
        for (i in 0..2) {
            drawCircle(
                color = Gold.copy(alpha = (0.4f - i * 0.1f) * glowIntensity),
                radius = binduR * (2.5f + i * 1.5f),
                center = c,
                style = Stroke(width = 0.4.dp.toPx())
            )
        }
        // Bindu glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.9f * glowIntensity),
                    Gold.copy(alpha = 0.2f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = binduR * 5
            ),
            radius = binduR * 5,
            center = c
        )
        drawCircle(
            color = Gold.copy(alpha = 0.95f),
            radius = binduR,
            center = c
        )
    }
}

/**
 * Compute activation level (0..1) for each of the three lotus rings.
 * During inhale: inner fills first → middle → outer (sequential).
 * During exhale: outer dims first → middle → inner (reverse sequential).
 * During hold: all fully active with pulse.
 */
private fun computeRingActivations(
    phase: BreathPhase,
    progress: Float,
    holdPulse: Float
): Triple<Float, Float, Float> {
    return when (phase) {
        BreathPhase.INHALE -> {
            val inner = (progress * 3f).coerceIn(0f, 1f)
            val middle = ((progress - 0.33f) * 3f).coerceIn(0f, 1f)
            val outer = ((progress - 0.66f) * 3f).coerceIn(0f, 1f)
            Triple(inner, middle, outer)
        }
        BreathPhase.HOLD_IN -> {
            Triple(holdPulse, holdPulse, holdPulse)
        }
        BreathPhase.EXHALE -> {
            val outer = 1f - (progress * 3f).coerceIn(0f, 1f)
            val middle = 1f - ((progress - 0.33f) * 3f).coerceIn(0f, 1f)
            val inner = 1f - ((progress - 0.66f) * 3f).coerceIn(0f, 1f)
            Triple(inner, middle, outer)
        }
        BreathPhase.HOLD_OUT -> {
            Triple(0.1f, 0.05f, 0.02f)
        }
        BreathPhase.IDLE -> {
            Triple(0.3f, 0.2f, 0.15f)
        }
    }
}

/**
 * Draw a lotus layer with petals whose glow intensity reflects activation.
 */
private fun DrawScope.drawLotusLayer(
    center: Offset,
    ringRadius: Float,
    petalCount: Int,
    petalLength: Float,
    petalWidth: Float,
    activation: Float,
    glowIntensity: Float
) {
    val baseAlpha = 0.15f
    val activeAlpha = baseAlpha + activation * 0.5f

    for (i in 0 until petalCount) {
        val angle = (2.0 * PI * i / petalCount).toFloat()
        val cos = cos(angle)
        val sin = sin(angle)

        val baseX = center.x + ringRadius * cos
        val baseY = center.y + ringRadius * sin
        val tipX = center.x + (ringRadius + petalLength) * cos
        val tipY = center.y + (ringRadius + petalLength) * sin

        val perpX = -sin
        val perpY = cos

        val midR = ringRadius + petalLength * 0.5f
        val cp1X = center.x + midR * cos + petalWidth * perpX
        val cp1Y = center.y + midR * sin + petalWidth * perpY
        val cp2X = center.x + midR * cos - petalWidth * perpX
        val cp2Y = center.y + midR * sin - petalWidth * perpY

        val path = Path().apply {
            moveTo(baseX, baseY)
            quadraticBezierTo(cp1X, cp1Y, tipX, tipY)
            quadraticBezierTo(cp2X, cp2Y, baseX, baseY)
        }

        // Stroke
        drawPath(
            path,
            Gold.copy(alpha = activeAlpha * glowIntensity),
            style = Stroke(width = (0.8f + activation * 0.4f).dp.toPx())
        )

        // Petal tip glow when active
        if (activation > 0.3f) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = activation * 0.4f * glowIntensity),
                        Color.Transparent
                    ),
                    center = Offset(tipX, tipY),
                    radius = 4.dp.toPx()
                ),
                radius = 4.dp.toPx(),
                center = Offset(tipX, tipY)
            )
        }
    }
}

/**
 * Draw energy particles that rise from inner ring to outer ring.
 */
private fun DrawScope.drawAscendingParticles(
    center: Offset,
    innerR: Float,
    outerR: Float,
    ascent: Float,
    glowIntensity: Float
) {
    val particleCount = 6
    for (i in 0 until particleCount) {
        val baseAngle = 2.0 * PI * i / particleCount
        val t = (ascent + i * 0.15f) % 1f
        val currentR = innerR + t * (outerR * 1.1f - innerR)

        // Spiral outward slightly
        val spiralAngle = baseAngle + t * PI * 0.5
        val px = center.x + currentR * cos(spiralAngle).toFloat()
        val py = center.y + currentR * sin(spiralAngle).toFloat()

        val alpha = (1f - t) * 0.6f * glowIntensity
        val radius = (3f - t * 1.5f).dp.toPx()

        // Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = alpha * 0.5f),
                    Color.Transparent
                ),
                center = Offset(px, py),
                radius = radius * 3
            ),
            radius = radius * 3,
            center = Offset(px, py)
        )
        // Core
        drawCircle(
            color = Gold.copy(alpha = alpha),
            radius = radius,
            center = Offset(px, py)
        )
    }
}
