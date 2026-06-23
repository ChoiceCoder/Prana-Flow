package com.pranaflow.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.pranaflow.strings.S
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Hardcoded dark palette — splash is always dark for brand identity
private val SplashBg = Color(0xFF0A0A0A)
private val SplashGold = Color(0xFFD4AF37)
private val SplashGoldPale = Color(0xFFF0D98C)
private val SplashGoldDim = Color(0x80D4AF37)

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val mandalaScale = remember { Animatable(0.3f) }
    val particleBurst = remember { Animatable(0f) }

    val inf = rememberInfiniteTransition(label = "splash")
    val rotation by inf.animateFloat(0f, 360f, infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Restart), label = "rot")
    val pulse by inf.animateFloat(0.5f, 1f, infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse), label = "pulse")
    val breathe by inf.animateFloat(0.85f, 1.15f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse), label = "breathe")

    LaunchedEffect(Unit) {
        mandalaScale.animateTo(1f, tween(1200))
        particleBurst.animateTo(1f, tween(800))
        delay(200)
        titleAlpha.animateTo(1f, tween(800))
        subtitleAlpha.animateTo(1f, tween(600))
        delay(1200)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(SplashBg),
        contentAlignment = Alignment.Center
    ) {
        // Particle burst
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = center
            val bp = particleBurst.value
            for (i in 0 until 24) {
                val a = (2.0 * PI * i / 24).toFloat()
                val d = bp * size.minDimension * 0.4f
                drawCircle(SplashGold.copy(alpha = (1f - bp) * 0.4f), 2.dp.toPx() * (1f - bp * 0.5f), Offset(c.x + d * cos(a), c.y + d * sin(a)))
            }
        }

        // Breathing mandala
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = center
            val sc = mandalaScale.value * breathe
            val maxR = size.minDimension * 0.32f * sc

            rotate(rotation, pivot = c) {
                for (ring in 0..5) {
                    val r = maxR * (0.25f + ring * 0.15f)
                    val ra = (0.04f + ring * 0.015f) * pulse
                    drawCircle(SplashGold.copy(alpha = ra), r, c, style = Stroke(0.8.dp.toPx()))
                    for (j in 0 until (4 + ring * 2)) {
                        val a = (2.0 * PI * j / (4 + ring * 2)).toFloat()
                        drawCircle(SplashGold.copy(alpha = ra * 2f), 1.5.dp.toPx(), Offset(c.x + r * cos(a), c.y + r * sin(a)))
                    }
                }
            }

            rotate(-rotation * 0.5f, pivot = c) {
                for (i in 0 until 8) {
                    val a = (2.0 * PI * i / 8).toFloat()
                    val bx = c.x + maxR * 0.5f * cos(a); val by = c.y + maxR * 0.5f * sin(a)
                    val tx = c.x + maxR * 0.8f * cos(a); val ty = c.y + maxR * 0.8f * sin(a)
                    val px = -sin(a); val py = cos(a); val w = maxR * 0.08f
                    val path = Path().apply {
                        moveTo(bx, by)
                        quadraticBezierTo((bx + tx) / 2 + w * px, (by + ty) / 2 + w * py, tx, ty)
                        quadraticBezierTo((bx + tx) / 2 - w * px, (by + ty) / 2 - w * py, bx, by)
                    }
                    drawPath(path, SplashGold.copy(alpha = 0.1f * pulse * sc), style = Stroke(0.8.dp.toPx()))
                }
            }

            rotate(rotation * 0.3f, pivot = c) {
                for (i in 0 until 6) {
                    val a = (2.0 * PI * i / 6).toFloat()
                    val tr = maxR * 0.15f
                    val cx2 = c.x + maxR * 0.9f * cos(a); val cy2 = c.y + maxR * 0.9f * sin(a)
                    val path = Path().apply { moveTo(cx2, cy2 - tr); lineTo(cx2 + tr * 0.866f, cy2 + tr * 0.5f); lineTo(cx2 - tr * 0.866f, cy2 + tr * 0.5f); close() }
                    drawPath(path, SplashGold.copy(alpha = 0.08f * pulse), style = Stroke(0.6.dp.toPx()))
                }
            }

            drawCircle(Brush.radialGradient(listOf(SplashGold.copy(alpha = 0.25f * pulse), Color.Transparent), center = c, radius = maxR * 0.4f), maxR * 0.4f, c)
            drawCircle(SplashGold.copy(alpha = 0.7f * pulse), 4.dp.toPx(), c)
        }

        // Title
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = S.current.pranaFlow,
                style = MaterialTheme.typography.headlineLarge.copy(letterSpacing = 8.sp, color = SplashGoldPale),
                modifier = Modifier.alpha(titleAlpha.value)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = S.current.breatheWithRhythm,
                style = MaterialTheme.typography.titleSmall.copy(color = SplashGoldDim),
                modifier = Modifier.alpha(subtitleAlpha.value)
            )
        }
    }
}
