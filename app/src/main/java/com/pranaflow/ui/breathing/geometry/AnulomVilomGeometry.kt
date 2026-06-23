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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathState
import com.pranaflow.ui.theme.Gold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Anulom Vilom Geometry — Ida & Pingala channels.
 *
 * Visual structure:
 * - Central vertical line (Sushumna Nadi)
 * - Two interweaving sinusoidal curves (Ida = left, Pingala = right)
 * - Energy orb that flows along curves with breath
 * - Chakra nodes at intersection points
 * - Particle trail behind the energy orb
 *
 * Breathing behavior:
 * INHALE → energy flows upward along left curve (Ida)
 * HOLD → energy rests at crown
 * EXHALE → energy flows downward along right curve (Pingala)
 * HOLD → energy rests at base
 *
 * The curves breathe — amplitude gently expands/contracts.
 */
@Composable
fun AnulomVilomGeometry(
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "anulom")

    // Subtle wave phase shift for ambient motion
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "anulom_wave"
    )

    // Chakra pulse
    val chakraPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "anulom_chakra"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val c = center
        val halfHeight = size.height * 0.38f
        val amplitude = size.width * 0.18f

        // Curve amplitude breathes with the cycle
        val ampScale = when (breathState.phase) {
            BreathPhase.INHALE -> 0.7f + easedProgress * 0.3f
            BreathPhase.HOLD_IN -> 1.0f
            BreathPhase.EXHALE -> 1.0f - easedProgress * 0.3f
            BreathPhase.HOLD_OUT -> 0.7f
            BreathPhase.IDLE -> 0.85f
        }
        val currentAmp = amplitude * ampScale

        // ── Background Glow ──
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Gold.copy(alpha = 0.08f * glowIntensity),
                    Color.Transparent
                ),
                center = c,
                radius = halfHeight * 1.5f
            ),
            radius = halfHeight * 1.5f,
            center = c
        )

        // ── Sushumna (Central Vertical Line) ──
        drawLine(
            color = Gold.copy(alpha = 0.15f * glowIntensity),
            start = Offset(c.x, c.y - halfHeight),
            end = Offset(c.x, c.y + halfHeight),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(8.dp.toPx(), 4.dp.toPx()), 0f
            )
        )

        // ── Ida Nadi (Left Curve) ──
        val idaPath = buildNadiPath(
            center = c,
            halfHeight = halfHeight,
            amplitude = currentAmp,
            phaseShift = wavePhase,
            mirror = false
        )
        drawPath(
            idaPath,
            color = Gold.copy(alpha = 0.45f * glowIntensity),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // ── Pingala Nadi (Right Curve — mirrored) ──
        val pingalaPath = buildNadiPath(
            center = c,
            halfHeight = halfHeight,
            amplitude = currentAmp,
            phaseShift = wavePhase,
            mirror = true
        )
        drawPath(
            pingalaPath,
            color = Gold.copy(alpha = 0.3f * glowIntensity),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // ── Chakra Nodes (at intersections) ──
        val chakraCount = 7
        for (i in 0 until chakraCount) {
            val t = i.toFloat() / (chakraCount - 1)
            val y = c.y + halfHeight - t * halfHeight * 2
            val nodeRadius = (3.dp.toPx() + i * 0.3.dp.toPx()) * chakraPulse
            val alpha = (0.3f + i * 0.07f) * glowIntensity

            // Glow ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Gold.copy(alpha = alpha * 0.5f),
                        Color.Transparent
                    ),
                    center = Offset(c.x, y),
                    radius = nodeRadius * 4
                ),
                radius = nodeRadius * 4,
                center = Offset(c.x, y)
            )
            // Solid dot
            drawCircle(
                color = Gold.copy(alpha = alpha),
                radius = nodeRadius,
                center = Offset(c.x, y)
            )
        }

        // ── Energy Orb ──
        drawEnergyOrb(
            center = c,
            halfHeight = halfHeight,
            amplitude = currentAmp,
            wavePhase = wavePhase,
            breathState = breathState,
            easedProgress = easedProgress,
            glowIntensity = glowIntensity
        )
    }
}

/**
 * Build a sinusoidal path representing one Nadi (energy channel).
 */
private fun buildNadiPath(
    center: Offset,
    halfHeight: Float,
    amplitude: Float,
    phaseShift: Float,
    mirror: Boolean
): Path {
    val path = Path()
    val steps = 80
    val sign = if (mirror) -1f else 1f

    for (i in 0..steps) {
        val t = i.toFloat() / steps
        val y = center.y + halfHeight - t * halfHeight * 2

        // Sinusoidal wave with 2 full cycles along the height
        val waveAngle = t * 2f * PI.toFloat() * 2f + phaseShift
        val x = center.x + sign * sin(waveAngle) * amplitude

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    return path
}

/**
 * Draw the flowing energy orb that travels along the nadis.
 *
 * INHALE: orb travels upward along Ida (left)
 * HOLD_IN: orb rests at top
 * EXHALE: orb travels downward along Pingala (right)
 * HOLD_OUT: orb rests at bottom
 */
private fun DrawScope.drawEnergyOrb(
    center: Offset,
    halfHeight: Float,
    amplitude: Float,
    wavePhase: Float,
    breathState: BreathState,
    easedProgress: Float,
    glowIntensity: Float
) {
    if (breathState.phase == BreathPhase.IDLE) return

    // Determine orb position
    val (orbT, useMirror) = when (breathState.phase) {
        BreathPhase.INHALE -> easedProgress to false        // Bottom → top on Ida
        BreathPhase.HOLD_IN -> 1f to false                   // Rest at top
        BreathPhase.EXHALE -> (1f - easedProgress) to true  // Top → bottom on Pingala
        BreathPhase.HOLD_OUT -> 0f to true                   // Rest at bottom
        BreathPhase.IDLE -> return
    }

    val y = center.y + halfHeight - orbT * halfHeight * 2
    val sign = if (useMirror) -1f else 1f
    val waveAngle = orbT * 2f * PI.toFloat() * 2f + wavePhase
    val x = center.x + sign * sin(waveAngle) * amplitude

    val orbRadius = 6.dp.toPx()

    // Trail particles (5 trailing dots)
    for (trail in 1..5) {
        val trailT = (orbT - trail * 0.03f).coerceIn(0f, 1f)
        val trailY = center.y + halfHeight - trailT * halfHeight * 2
        val trailWave = trailT * 2f * PI.toFloat() * 2f + wavePhase
        val trailX = center.x + sign * sin(trailWave) * amplitude
        val trailAlpha = (0.5f - trail * 0.08f).coerceAtLeast(0.05f) * glowIntensity

        drawCircle(
            color = Gold.copy(alpha = trailAlpha),
            radius = orbRadius * (1f - trail * 0.12f),
            center = Offset(trailX, trailY)
        )
    }

    // Main orb glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Gold.copy(alpha = 0.8f * glowIntensity),
                Gold.copy(alpha = 0.2f * glowIntensity),
                Color.Transparent
            ),
            center = Offset(x, y),
            radius = orbRadius * 5
        ),
        radius = orbRadius * 5,
        center = Offset(x, y)
    )

    // Solid orb
    drawCircle(
        color = Gold.copy(alpha = 0.95f),
        radius = orbRadius,
        center = Offset(x, y)
    )
}
