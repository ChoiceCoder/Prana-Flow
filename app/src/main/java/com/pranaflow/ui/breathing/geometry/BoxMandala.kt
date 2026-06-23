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
import androidx.compose.ui.geometry.Size
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
 * Square Mandala — used for Box Breathing.
 *
 * Visual structure:
 * - Background radial glow (gold, pulsing)
 * - 4 concentric squares, each rotated progressively
 * - Corner diamond ornaments on the outermost square
 * - Particle dots that travel along square edges
 * - Central pulsing orb
 *
 * The entire geometry scales with breathing:
 * INHALE → expand, EXHALE → contract, HOLD → maintain
 */
@Composable
fun BoxMandala(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "box_mandala")

    // Slow continuous rotation for ambient motion
    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "box_ambient_rot"
    )

    // Particle travel position along edges (0..1)
    val particleTravel by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "box_particle"
    )

    // Compute scale factor based on breath phase
    val scaleFactor = when (breathState.phase) {
        BreathPhase.INHALE -> 0.55f + easedProgress * 0.45f        // 0.55 → 1.0
        BreathPhase.HOLD_IN -> 1.0f                                 // Hold expanded
        BreathPhase.EXHALE -> 1.0f - easedProgress * 0.45f         // 1.0 → 0.55
        BreathPhase.HOLD_OUT -> 0.55f                               // Hold contracted
        BreathPhase.IDLE -> 0.7f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val c = center
        val baseRadius = size.minDimension * 0.42f
        val r = baseRadius * scaleFactor

        // ── Background Glow ──
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.12f * glowIntensity * scaleFactor),
                    Gold.copy(alpha = 0.04f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = r * 1.8f
            ),
            radius = r * 1.8f,
            center = c
        )

        // ── Concentric Rotated Squares ──
        val layerCount = 4
        for (i in 0 until layerCount) {
            val layerScale = 1f - i * 0.18f
            val layerR = r * layerScale
            val rotation = ambientRotation * (if (i % 2 == 0) 1f else -0.5f) + i * 11f
            val alpha = (0.7f - i * 0.12f) * glowIntensity

            rotate(rotation, pivot = c) {
                // Square
                drawRect(
                    color = Gold.copy(alpha = alpha),
                    topLeft = Offset(c.x - layerR, c.y - layerR),
                    size = Size(layerR * 2, layerR * 2),
                    style = Stroke(width = (1.2f - i * 0.2f).dp.toPx())
                )

                // Corner ornaments on outermost two layers
                if (i < 2) {
                    drawCornerOrnaments(c, layerR, Gold.copy(alpha = alpha * 0.7f))
                }
            }
        }

        // ── Edge Particles (on outermost square) ──
        rotate(ambientRotation, pivot = c) {
            drawEdgeParticles(c, r, particleTravel, Gold.copy(alpha = 0.8f * glowIntensity))
        }

        // ── Central Orb ──
        val orbRadius = 4.dp.toPx() * scaleFactor
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.9f * glowIntensity),
                    Gold.copy(alpha = 0.3f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = orbRadius * 4
            ),
            radius = orbRadius * 4,
            center = c
        )
        drawCircle(
            color = Gold.copy(alpha = 0.9f),
            radius = orbRadius,
            center = c
        )
    }
}

/**
 * Draw small diamond shapes at each corner of a square.
 */
private fun DrawScope.drawCornerOrnaments(
    center: Offset,
    halfSize: Float,
    color: Color
) {
    val ornamentSize = 6.dp.toPx()
    val corners = listOf(
        Offset(center.x - halfSize, center.y - halfSize),
        Offset(center.x + halfSize, center.y - halfSize),
        Offset(center.x + halfSize, center.y + halfSize),
        Offset(center.x - halfSize, center.y + halfSize)
    )

    for (corner in corners) {
        val path = Path().apply {
            moveTo(corner.x, corner.y - ornamentSize)
            lineTo(corner.x + ornamentSize, corner.y)
            lineTo(corner.x, corner.y + ornamentSize)
            lineTo(corner.x - ornamentSize, corner.y)
            close()
        }
        drawPath(path, color, style = Stroke(width = 0.8.dp.toPx()))
    }
}

/**
 * Draw particles traveling along the edges of a square.
 */
private fun DrawScope.drawEdgeParticles(
    center: Offset,
    halfSize: Float,
    travel: Float,    // 0..1 position along perimeter
    color: Color
) {
    val particleCount = 4
    val particleRadius = 2.5.dp.toPx()

    for (i in 0 until particleCount) {
        val t = (travel + i.toFloat() / particleCount) % 1f
        val perimeter = halfSize * 8  // total perimeter = 4 sides × 2 × halfSize
        val pos = t * perimeter

        val point = when {
            // Top edge (left → right)
            pos < halfSize * 2 -> {
                val frac = pos / (halfSize * 2)
                Offset(center.x - halfSize + frac * halfSize * 2, center.y - halfSize)
            }
            // Right edge (top → bottom)
            pos < halfSize * 4 -> {
                val frac = (pos - halfSize * 2) / (halfSize * 2)
                Offset(center.x + halfSize, center.y - halfSize + frac * halfSize * 2)
            }
            // Bottom edge (right → left)
            pos < halfSize * 6 -> {
                val frac = (pos - halfSize * 4) / (halfSize * 2)
                Offset(center.x + halfSize - frac * halfSize * 2, center.y + halfSize)
            }
            // Left edge (bottom → top)
            else -> {
                val frac = (pos - halfSize * 6) / (halfSize * 2)
                Offset(center.x - halfSize, center.y + halfSize - frac * halfSize * 2)
            }
        }

        // Particle with glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color, Color.Transparent),
                center = point,
                radius = particleRadius * 3
            ),
            radius = particleRadius * 3,
            center = point
        )
        drawCircle(color = color, radius = particleRadius, center = point)
    }
}
