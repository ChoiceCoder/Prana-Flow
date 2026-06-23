package com.pranaflow.ui.breathing

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pranaflow.model.BreathPhase
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ── Particle data ──
private data class Particle(
    val x: Float,       // 0..1 normalized
    val y: Float,       // 0..1 normalized
    val radius: Float,  // dp
    val speed: Float,   // drift speed multiplier
    val phase: Float    // animation phase offset
)

// ── Floating Particle Aura ──
@Composable
fun ParticleAura(
    breathProgress: Float,   // 0..1 current phase progress
    breathPhase: BreathPhase,
    goldColor: Color,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        List(35) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 0.5f,
                speed = Random.nextFloat() * 0.6f + 0.4f,
                phase = Random.nextFloat() * 6.28f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "particles")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 628f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_time"
    )

    // Breath-synced alpha pulse
    val breathAlpha = when (breathPhase) {
        BreathPhase.INHALE -> 0.15f + breathProgress * 0.25f   // fade in
        BreathPhase.HOLD_IN -> 0.4f                             // bright hold
        BreathPhase.EXHALE -> 0.4f - breathProgress * 0.25f    // fade out
        BreathPhase.HOLD_OUT -> 0.15f                           // dim hold
        BreathPhase.IDLE -> 0.1f
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (p in particles) {
            val t = time * p.speed
            // Gentle drift: circular + vertical float
            val px = (p.x * w + sin(t * 0.01f + p.phase) * w * 0.06f) % w
            val py = (p.y * h + cos(t * 0.008f + p.phase) * h * 0.04f) % h
            val r = p.radius.dp.toPx()

            // Individual twinkle
            val twinkle = (sin(t * 0.02f + p.phase) * 0.5f + 0.5f)
            val alpha = (breathAlpha * twinkle).coerceIn(0.02f, 0.5f)

            // Soft glow
            drawCircle(
                color = goldColor.copy(alpha = alpha * 0.3f),
                radius = r * 3f,
                center = Offset(px, py)
            )
            // Core dot
            drawCircle(
                color = goldColor.copy(alpha = alpha),
                radius = r,
                center = Offset(px, py)
            )
        }
    }
}

// ── Phase Progress Ring ──
@Composable
fun PhaseProgressRing(
    progress: Float,          // 0..1 within current phase
    phase: BreathPhase,
    goldColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(160.dp)) {
        val strokeWidth = 3.dp.toPx()
        val pad = strokeWidth / 2
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

        // Background track
        drawArc(
            color = goldColor.copy(alpha = 0.08f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(pad, pad),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Active progress arc
        val sweepAngle = progress * 360f
        val phaseAlpha = when (phase) {
            BreathPhase.IDLE -> 0.1f
            else -> 0.6f
        }
        drawArc(
            color = goldColor.copy(alpha = phaseAlpha),
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(pad, pad),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Leading dot
        if (phase != BreathPhase.IDLE) {
            val angle = Math.toRadians((-90.0 + sweepAngle))
            val cx = size.width / 2 + (arcSize.width / 2) * cos(angle).toFloat()
            val cy = size.height / 2 + (arcSize.height / 2) * sin(angle).toFloat()
            drawCircle(
                color = goldColor.copy(alpha = 0.8f),
                radius = strokeWidth * 1.5f,
                center = Offset(cx, cy)
            )
        }
    }
}
