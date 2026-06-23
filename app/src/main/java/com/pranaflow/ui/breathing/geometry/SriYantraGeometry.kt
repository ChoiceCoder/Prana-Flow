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
 * Sri Yantra — used for Ujjayi (Ocean Breath / Victorious Breath).
 *
 * The Sri Yantra is the supreme instrument of meditation in Hindu tantra.
 * It represents the cosmic creation and the union of masculine (Shiva)
 * and feminine (Shakti) energies.
 *
 * Visual structure:
 * - Bhupura (outer square gate with T-shaped openings)
 * - 3 concentric circles (triple ring)
 * - 16-petal lotus ring
 * - 4 upward triangles (Shiva — masculine fire)
 * - 5 downward triangles (Shakti — feminine water)
 * - Bindu (central point of infinite consciousness)
 *
 * Breathing behavior:
 * INHALE → triangles expand, energy rises (inner fire)
 * HOLD   → pulsing radiance from bindu
 * EXHALE → graceful contraction, triangles nest inward
 * HOLD   → stillness at bindu
 */
@Composable
fun SriYantraGeometry(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sri_yantra")

    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(90000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sy_rotation"
    )

    val binduPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sy_bindu"
    )

    val fireShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sy_fire"
    )

    val scaleFactor = when (breathState.phase) {
        BreathPhase.INHALE -> 0.5f + easedProgress * 0.5f
        BreathPhase.HOLD_IN -> 1.0f
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
                    Gold.copy(alpha = 0.1f * glowIntensity * scaleFactor),
                    Color.Transparent
                ),
                center = c,
                radius = baseR * 1.8f
            ),
            radius = baseR * 1.8f,
            center = c
        )

        // ── Bhupura (Outer Square Gate) ──
        val gateR = r * 1.05f
        drawRect(
            color = Gold.copy(alpha = 0.2f * glowIntensity),
            topLeft = Offset(c.x - gateR, c.y - gateR),
            size = androidx.compose.ui.geometry.Size(gateR * 2, gateR * 2),
            style = Stroke(width = 1.dp.toPx())
        )
        // T-gate marks on each side
        val gateMarkLen = gateR * 0.12f
        val gateSides = listOf(
            Offset(c.x, c.y - gateR),  // top
            Offset(c.x + gateR, c.y),   // right
            Offset(c.x, c.y + gateR),   // bottom
            Offset(c.x - gateR, c.y)    // left
        )
        for (gs in gateSides) {
            drawCircle(
                color = Gold.copy(alpha = 0.25f * glowIntensity),
                radius = gateMarkLen,
                center = gs,
                style = Stroke(width = 0.5.dp.toPx())
            )
        }

        // ── Triple Concentric Circles ──
        for (i in 0..2) {
            val cr = r * (0.95f - i * 0.05f)
            drawCircle(
                color = Gold.copy(alpha = (0.25f - i * 0.05f) * glowIntensity),
                radius = cr,
                center = c,
                style = Stroke(width = 0.6.dp.toPx())
            )
        }

        // ── 16-Petal Lotus Ring ──
        rotate(ambientRotation * 0.08f, pivot = c) {
            drawLotusRing(c, r * 0.88f, 16, r * 0.09f, Gold.copy(alpha = 0.3f * glowIntensity))
        }

        // ── Upward Triangles (Shiva — 4 layers) ──
        val upScales = listOf(0.78f, 0.58f, 0.40f, 0.22f)
        for ((idx, scale) in upScales.withIndex()) {
            val triR = r * scale
            val alpha = (0.5f - idx * 0.08f) * glowIntensity
            val extraAlpha = if (breathState.phase == BreathPhase.INHALE)
                fireShimmer * 0.15f * (1f - idx * 0.2f) else 0f

            drawTriangle(
                center = c,
                radius = triR,
                pointUp = true,
                color = Gold.copy(alpha = (alpha + extraAlpha).coerceAtMost(1f)),
                strokeWidth = (1.2f - idx * 0.15f).dp.toPx()
            )
        }

        // ── Downward Triangles (Shakti — 5 layers) ──
        val downScales = listOf(0.85f, 0.68f, 0.52f, 0.35f, 0.18f)
        for ((idx, scale) in downScales.withIndex()) {
            val triR = r * scale
            val alpha = (0.4f - idx * 0.06f) * glowIntensity

            drawTriangle(
                center = c,
                radius = triR,
                pointUp = false,
                color = Gold.copy(alpha = alpha),
                strokeWidth = (1f - idx * 0.12f).dp.toPx()
            )
        }

        // ── Intersection Glow Points ──
        // Highlight where upward and downward triangles cross
        val intersectionCount = 12
        for (i in 0 until intersectionCount) {
            val angle = 2.0 * PI * i / intersectionCount
            val iR = r * 0.45f
            val px = c.x + iR * cos(angle).toFloat()
            val py = c.y + iR * sin(angle).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = 0.3f * glowIntensity * binduPulse),
                        Color.Transparent
                    ),
                    center = Offset(px, py),
                    radius = 4.dp.toPx()
                ),
                radius = 4.dp.toPx(),
                center = Offset(px, py)
            )
        }

        // ── Bindu (Central Point of Consciousness) ──
        val binduR = 6.dp.toPx() * scaleFactor * binduPulse
        // Radiance
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.9f * glowIntensity),
                    Gold.copy(alpha = 0.3f * glowIntensity),
                    Gold.copy(alpha = 0.05f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = binduR * 6
            ),
            radius = binduR * 6,
            center = c
        )
        // Solid core
        drawCircle(
            color = Gold,
            radius = binduR,
            center = c
        )
    }
}

/**
 * Draw a single equilateral triangle centered at the given point.
 */
private fun DrawScope.drawTriangle(
    center: Offset,
    radius: Float,
    pointUp: Boolean,
    color: Color,
    strokeWidth: Float
) {
    val direction = if (pointUp) -1f else 1f
    val path = Path().apply {
        // Apex
        moveTo(center.x, center.y + direction * radius)
        // Base left
        lineTo(
            center.x - radius * 0.866f,
            center.y - direction * radius * 0.5f
        )
        // Base right
        lineTo(
            center.x + radius * 0.866f,
            center.y - direction * radius * 0.5f
        )
        close()
    }
    drawPath(path, color, style = Stroke(width = strokeWidth))
}

/**
 * Draw a ring of small lotus petal shapes.
 */
private fun DrawScope.drawLotusRing(
    center: Offset,
    radius: Float,
    count: Int,
    petalLen: Float,
    color: Color
) {
    for (i in 0 until count) {
        val angle = (2.0 * PI * i / count).toFloat()
        val cos = cos(angle)
        val sin = sin(angle)

        val baseX = center.x + radius * cos
        val baseY = center.y + radius * sin
        val tipX = center.x + (radius + petalLen) * cos
        val tipY = center.y + (radius + petalLen) * sin

        val perpX = -sin
        val perpY = cos
        val w = petalLen * 0.4f

        val path = Path().apply {
            moveTo(baseX, baseY)
            quadraticBezierTo(
                baseX + petalLen * 0.5f * cos + w * perpX,
                baseY + petalLen * 0.5f * sin + w * perpY,
                tipX, tipY
            )
            quadraticBezierTo(
                baseX + petalLen * 0.5f * cos - w * perpX,
                baseY + petalLen * 0.5f * sin - w * perpY,
                baseX, baseY
            )
        }
        drawPath(path, color, style = Stroke(width = 0.6.dp.toPx()))
    }
}
