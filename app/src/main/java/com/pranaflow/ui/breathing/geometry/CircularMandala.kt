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
 * Circular Mandala — used for 4-7-8 Breathing.
 *
 * Visual structure:
 * - Radial background glow
 * - 3 concentric rings
 * - Outer ring: 12 lotus petals
 * - Middle ring: 8 lotus petals (counter-rotating)
 * - Inner ring: 6 small petals
 * - Connecting radial lines (web pattern)
 * - Central pulsing orb
 *
 * Breathing behavior:
 * INHALE (4s) → smooth expansion
 * HOLD (7s) → maintained with subtle pulsation
 * EXHALE (8s) → slow, graceful contraction
 */
@Composable
fun CircularMandala(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circular_mandala")

    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(50000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "circ_outer_rot"
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(35000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "circ_inner_rot"
    )

    // Subtle shimmer during hold phase
    val holdShimmer by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circ_shimmer"
    )

    val scaleFactor = when (breathState.phase) {
        BreathPhase.INHALE -> 0.5f + easedProgress * 0.5f
        BreathPhase.HOLD_IN -> 1.0f * holdShimmer   // Subtle breathing while holding
        BreathPhase.EXHALE -> 1.0f - easedProgress * 0.5f
        BreathPhase.HOLD_OUT -> 0.5f
        BreathPhase.IDLE -> 0.7f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val c = center
        val baseR = size.minDimension * 0.42f
        val r = baseR * scaleFactor

        // ── Background Glow ──
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.15f * glowIntensity * scaleFactor),
                    Gold.copy(alpha = 0.05f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = r * 2f
            ),
            radius = r * 2f,
            center = c
        )

        // ── Concentric Rings ──
        val ringRadii = listOf(r, r * 0.7f, r * 0.4f)
        val ringAlphas = listOf(0.4f, 0.3f, 0.25f)

        for (i in ringRadii.indices) {
            drawCircle(
                color = Gold.copy(alpha = ringAlphas[i] * glowIntensity),
                radius = ringRadii[i],
                center = c,
                style = Stroke(width = 0.7.dp.toPx())
            )
        }

        // ── Radial Web Lines ──
        val webCount = 12
        for (i in 0 until webCount) {
            val angle = (2.0 * PI * i / webCount).toFloat()
            val startP = Offset(
                c.x + ringRadii[2] * cos(angle),
                c.y + ringRadii[2] * sin(angle)
            )
            val endP = Offset(
                c.x + ringRadii[0] * cos(angle),
                c.y + ringRadii[0] * sin(angle)
            )
            drawLine(
                color = Gold.copy(alpha = 0.1f * glowIntensity),
                start = startP,
                end = endP,
                strokeWidth = 0.5.dp.toPx()
            )
        }

        // ── Outer Petals (12) ──
        rotate(outerRotation, pivot = c) {
            drawLotusPetals(
                center = c,
                radius = r,
                petalCount = 12,
                petalLength = r * 0.25f,
                petalWidth = r * 0.12f,
                color = Gold.copy(alpha = 0.5f * glowIntensity),
                strokeWidth = 1.dp.toPx()
            )
        }

        // ── Middle Petals (8, counter-rotating) ──
        rotate(innerRotation, pivot = c) {
            drawLotusPetals(
                center = c,
                radius = r * 0.7f,
                petalCount = 8,
                petalLength = r * 0.2f,
                petalWidth = r * 0.1f,
                color = Gold.copy(alpha = 0.45f * glowIntensity),
                strokeWidth = 0.8.dp.toPx()
            )
        }

        // ── Inner Petals (6) ──
        rotate(outerRotation * 0.7f, pivot = c) {
            drawLotusPetals(
                center = c,
                radius = r * 0.4f,
                petalCount = 6,
                petalLength = r * 0.15f,
                petalWidth = r * 0.08f,
                color = Gold.copy(alpha = 0.55f * glowIntensity),
                strokeWidth = 0.7.dp.toPx()
            )
        }

        // ── Junction Dots ──
        for (ring in ringRadii) {
            val dotCount = when (ring) {
                ringRadii[0] -> 12
                ringRadii[1] -> 8
                else -> 6
            }
            val rotAngle = when (ring) {
                ringRadii[0] -> outerRotation
                ringRadii[1] -> innerRotation
                else -> outerRotation * 0.7f
            }
            for (i in 0 until dotCount) {
                val angle = Math.toRadians((360.0 * i / dotCount) + rotAngle)
                val px = c.x + ring * cos(angle).toFloat()
                val py = c.y + ring * sin(angle).toFloat()
                drawCircle(
                    color = Gold.copy(alpha = 0.6f * glowIntensity),
                    radius = 1.5.dp.toPx(),
                    center = Offset(px, py)
                )
            }
        }

        // ── Central Orb ──
        val orbR = 5.dp.toPx() * scaleFactor
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.9f * glowIntensity),
                    Gold.copy(alpha = 0.2f * glowIntensity),
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
 * Draw lotus petal shapes around a circle.
 * Each petal is a pointed ellipse created with quadratic bezier curves.
 */
private fun DrawScope.drawLotusPetals(
    center: Offset,
    radius: Float,
    petalCount: Int,
    petalLength: Float,
    petalWidth: Float,
    color: Color,
    strokeWidth: Float
) {
    for (i in 0 until petalCount) {
        val angle = (2.0 * PI * i / petalCount).toFloat()
        val cos = cos(angle)
        val sin = sin(angle)

        // Petal base point (on the ring)
        val baseX = center.x + radius * cos
        val baseY = center.y + radius * sin

        // Petal tip (extends outward)
        val tipX = center.x + (radius + petalLength) * cos
        val tipY = center.y + (radius + petalLength) * sin

        // Control points for petal curves (perpendicular to radial direction)
        val perpX = -sin
        val perpY = cos

        val midRadius = radius + petalLength * 0.5f
        val cp1X = center.x + midRadius * cos + petalWidth * perpX
        val cp1Y = center.y + midRadius * sin + petalWidth * perpY
        val cp2X = center.x + midRadius * cos - petalWidth * perpX
        val cp2Y = center.y + midRadius * sin - petalWidth * perpY

        val path = Path().apply {
            moveTo(baseX, baseY)
            quadraticBezierTo(cp1X, cp1Y, tipX, tipY)
            quadraticBezierTo(cp2X, cp2Y, baseX, baseY)
        }

        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}
