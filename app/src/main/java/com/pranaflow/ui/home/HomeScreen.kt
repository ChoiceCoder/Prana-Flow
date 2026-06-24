package com.pranaflow.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pranaflow.model.TechniqueCategory
import com.pranaflow.model.Techniques
import com.pranaflow.ui.components.PranaButton
import com.pranaflow.ui.components.bounceClick
import com.pranaflow.ui.theme.PranaTheme
import com.pranaflow.strings.S
import com.pranaflow.strings.localizedLabel
import com.pranaflow.strings.localizedSubtitle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    onCategorySelected: (String) -> Unit,
    onTechniqueSelected: (String) -> Unit,
    onSettingsClicked: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val infiniteTransition = rememberInfiniteTransition(label = "home_bg")
    val bgRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(60000, easing = LinearEasing), RepeatMode.Restart),
        label = "home_bg_rot"
    )

    // Capture theme colors ONCE at composable level (safe for Canvas)
    val colors = PranaTheme.colors

    Box(modifier = Modifier.fillMaxSize()) {
        // Background concentric circle art
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Center point slightly above screen center (behind cards area)
            val cx = size.width * 0.5f
            val cy = size.height * 0.42f
            val artCenter = Offset(cx, cy)
            val maxR = size.width * 0.85f

            // Large concentric circles — prominent like reference
            val ringCount = 10
            for (i in 1..ringCount) {
                val r = maxR * (i.toFloat() / ringCount)
                val alpha = if (colors.isDark) {
                    0.04f + (i * 0.006f)  // Dark mode: subtle gold
                } else {
                    0.08f + (i * 0.008f)  // Light mode: more visible warm
                }
                drawCircle(
                    color = colors.gold.copy(alpha = alpha),
                    radius = r,
                    center = artCenter,
                    style = Stroke(width = if (i % 3 == 0) 1.2.dp.toPx() else 0.6.dp.toPx())
                )
            }

            // Slow rotating petal dots on middle rings
            rotate(bgRotation * 0.5f, pivot = artCenter) {
                for (ring in listOf(3, 5, 7)) {
                    val r = maxR * (ring.toFloat() / ringCount)
                    val dotCount = ring * 3
                    val dotAlpha = if (colors.isDark) 0.10f else 0.12f
                    for (j in 0 until dotCount) {
                        val angle = (2.0 * PI * j / dotCount).toFloat()
                        drawCircle(
                            colors.gold.copy(alpha = dotAlpha),
                            1.5.dp.toPx(),
                            Offset(artCenter.x + r * cos(angle), artCenter.y + r * sin(angle))
                        )
                    }
                }
            }

            // Center ornament
            val centerAlpha = if (colors.isDark) 0.08f else 0.15f
            drawCircle(colors.gold.copy(alpha = centerAlpha), 20.dp.toPx(), artCenter)
            drawCircle(colors.gold.copy(alpha = centerAlpha * 0.5f), 8.dp.toPx(), artCenter)
        }

        // Gradient overlays (uses captured colors)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Brush.verticalGradient(listOf(colors.background, Color.Transparent), startY = 0f, endY = size.height * 0.15f))
            drawRect(Brush.verticalGradient(listOf(Color.Transparent, colors.background), startY = size.height * 0.82f, endY = size.height))
        }

        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(80.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "⚙",
                        style = MaterialTheme.typography.titleMedium.copy(color = colors.goldDark),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onSettingsClicked)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(S.current.pranaFlow, style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text(S.current.breatheWithRhythm, style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
                PranaButton(text = S.current.quickCalm, onClick = { onTechniqueSelected(viewModel.quickCalmTechniqueId) })
                Spacer(modifier = Modifier.height(28.dp))
            }

            items(items = viewModel.categories, key = { it.name }) { category ->
                CategoryCard(
                    category = category,
                    techniqueCount = Techniques.all.count { it.category == category },
                    onClick = { onCategorySelected(category.name) },
                    colors = colors
                )
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun CategoryCard(
    category: TechniqueCategory,
    techniqueCount: Int,
    onClick: () -> Unit,
    colors: com.pranaflow.ui.theme.PranaColors
) {
    val isComingSoon = category == TechniqueCategory.MANTRA

    val inf = rememberInfiniteTransition(label = "cat_${category.name}")
    val iconRot by inf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Restart),
        label = "cat_rot"
    )
    val iconPulse by inf.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "cat_pulse"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!isComingSoon) Modifier.bounceClick(onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = colors.elevated,
        border = BorderStroke(0.5.dp, if (isComingSoon) colors.goldBorder.copy(alpha = 0.3f) else colors.goldBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated category icon
            Canvas(modifier = Modifier.size(44.dp)) {
                val c = center; val r = size.minDimension * 0.42f
                val iconAlpha = if (isComingSoon) 0.15f else (0.6f + iconPulse * 0.4f)
                val gc = colors.gold

                when (category) {
                    TechniqueCategory.RELAX -> {
                        // Lotus flower with opening/closing petals
                        val petalCount = 8
                        val petalOpen = 0.7f + iconPulse * 0.3f
                        rotate(iconRot * 0.15f, pivot = c) {
                            for (i in 0 until petalCount) {
                                val angle = (PI * 2 * i / petalCount).toFloat()
                                val tipDist = r * petalOpen
                                val baseDist = r * 0.2f
                                val bx = c.x + baseDist * cos(angle)
                                val by = c.y + baseDist * sin(angle)
                                val tx = c.x + tipDist * cos(angle)
                                val ty = c.y + tipDist * sin(angle)
                                val perpX = -sin(angle)
                                val perpY = cos(angle)
                                val w = r * 0.15f
                                val petal = Path().apply {
                                    moveTo(bx, by)
                                    quadraticBezierTo((bx + tx) / 2 + w * perpX, (by + ty) / 2 + w * perpY, tx, ty)
                                    quadraticBezierTo((bx + tx) / 2 - w * perpX, (by + ty) / 2 - w * perpY, bx, by)
                                }
                                drawPath(petal, gc.copy(alpha = iconAlpha * 0.7f), style = Stroke(0.8.dp.toPx()))
                            }
                        }
                        // Center dot with glow
                        drawCircle(gc.copy(alpha = iconAlpha * 0.3f), r * 0.25f, c)
                        drawCircle(gc.copy(alpha = iconAlpha), 2.5.dp.toPx(), c)
                    }

                    TechniqueCategory.FOCUS -> {
                        // Animated flame with inner glow
                        val flameH = r * (0.85f + iconPulse * 0.15f)
                        val flicker = sin(iconRot * 0.1f) * r * 0.08f
                        val outerFlame = Path().apply {
                            moveTo(c.x, c.y - flameH)
                            cubicTo(c.x + r * 0.5f + flicker, c.y - flameH * 0.3f, c.x + r * 0.4f, c.y + flameH * 0.3f, c.x, c.y + flameH * 0.5f)
                            cubicTo(c.x - r * 0.4f, c.y + flameH * 0.3f, c.x - r * 0.5f - flicker, c.y - flameH * 0.3f, c.x, c.y - flameH)
                        }
                        drawPath(outerFlame, gc.copy(alpha = iconAlpha * 0.6f), style = Stroke(1.dp.toPx()))
                        // Inner flame
                        val innerH = flameH * 0.55f
                        val innerFlame = Path().apply {
                            moveTo(c.x, c.y - innerH)
                            cubicTo(c.x + r * 0.25f, c.y - innerH * 0.2f, c.x + r * 0.2f, c.y + innerH * 0.3f, c.x, c.y + innerH * 0.4f)
                            cubicTo(c.x - r * 0.2f, c.y + innerH * 0.3f, c.x - r * 0.25f, c.y - innerH * 0.2f, c.x, c.y - innerH)
                        }
                        drawPath(innerFlame, gc.copy(alpha = iconAlpha * 0.4f), style = Stroke(0.6.dp.toPx()))
                        drawCircle(gc.copy(alpha = iconAlpha * 0.5f), 2.dp.toPx(), Offset(c.x, c.y + flameH * 0.15f))
                    }

                    TechniqueCategory.NADI_SHODHAN -> {
                        // Flowing double helix energy channels
                        val steps = 30
                        val p1 = Path()
                        val p2 = Path()
                        for (i in 0..steps) {
                            val t = i.toFloat() / steps
                            val y = c.y - r + t * r * 2
                            val phase = t * 2.5f * PI.toFloat() + iconRot * 0.03f
                            val xAmp = r * 0.35f * sin(phase)
                            if (i == 0) { p1.moveTo(c.x + xAmp, y); p2.moveTo(c.x - xAmp, y) }
                            else { p1.lineTo(c.x + xAmp, y); p2.lineTo(c.x - xAmp, y) }
                        }
                        drawPath(p1, gc.copy(alpha = iconAlpha * 0.7f), style = Stroke(1.dp.toPx()))
                        drawPath(p2, gc.copy(alpha = iconAlpha * 0.5f), style = Stroke(1.dp.toPx()))
                        // Energy nodes where channels cross
                        for (i in 0..2) {
                            val nodeY = c.y - r * 0.5f + i * r * 0.5f
                            drawCircle(gc.copy(alpha = iconAlpha * 0.6f), 2.dp.toPx(), Offset(c.x, nodeY))
                        }
                    }

                    TechniqueCategory.CHAKRAS -> {
                        // 7 dots pulsing in ascending wave pattern
                        for (i in 0 until 7) {
                            val dotY = c.y + r - (r * 2 * i / 6f)
                            val wavePhase = (iconRot * 0.05f + i * 0.5f)
                            val dotScale = 0.7f + 0.3f * sin(wavePhase)
                            val dotR = 2.5.dp.toPx() * dotScale
                            // Outer glow per dot
                            drawCircle(gc.copy(alpha = iconAlpha * 0.15f * dotScale), dotR * 3f, Offset(c.x, dotY))
                            drawCircle(gc.copy(alpha = iconAlpha * dotScale), dotR, Offset(c.x, dotY))
                        }
                        // Connecting line
                        drawLine(gc.copy(alpha = iconAlpha * 0.15f), Offset(c.x, c.y + r), Offset(c.x, c.y - r), strokeWidth = 0.5.dp.toPx())
                    }

                    TechniqueCategory.MANTRA -> {
                        // Om symbol outline with slow rotation
                        rotate(iconRot * 0.1f, pivot = c) {
                            drawCircle(gc.copy(alpha = iconAlpha * 0.4f), r * 0.6f, c, style = Stroke(0.8.dp.toPx()))
                            drawCircle(gc.copy(alpha = iconAlpha * 0.2f), r * 0.85f, c, style = Stroke(0.5.dp.toPx()))
                        }
                        drawCircle(gc.copy(alpha = iconAlpha * 0.6f), r * 0.15f, c)
                        drawCircle(gc.copy(alpha = iconAlpha), 2.dp.toPx(), c)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(category.localizedLabel(), style = MaterialTheme.typography.titleMedium.copy(color = if (isComingSoon) colors.goldDimText else colors.goldPale))
                Spacer(modifier = Modifier.height(4.dp))
                Text(category.localizedSubtitle(), style = MaterialTheme.typography.titleSmall.copy(color = if (isComingSoon) colors.goldDark else colors.goldDimText))
            }

            if (isComingSoon) {
                Text(S.current.soon, style = MaterialTheme.typography.labelSmall.copy(color = colors.goldDark, letterSpacing = 2.sp))
            } else {
                Text("$techniqueCount", style = MaterialTheme.typography.titleMedium.copy(color = colors.gold))
                Spacer(modifier = Modifier.width(4.dp))
                Text("→", style = MaterialTheme.typography.titleMedium.copy(color = colors.goldDimText))
            }
        }
    }
}
