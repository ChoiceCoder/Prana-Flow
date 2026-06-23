package com.pranaflow.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.pranaflow.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranaflow.model.BreathingTechnique
import com.pranaflow.model.GeometryType
import com.pranaflow.strings.localizedName
import com.pranaflow.strings.localizedSubtitle
import com.pranaflow.model.TechniqueCategory
import com.pranaflow.ui.theme.Gold
import com.pranaflow.ui.theme.PranaTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TechniqueCard(
    technique: BreathingTechnique,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_${technique.id}")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Restart),
        label = "card_rot_${technique.id}"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "card_pulse_${technique.id}"
    )

    val colors = PranaTheme.colors

    Surface(
        modifier = modifier.fillMaxWidth().bounceClick(onClick),
        shape = RoundedCornerShape(16.dp),
        color = colors.elevated,
        border = BorderStroke(0.5.dp, colors.goldBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (technique.geometryType == GeometryType.CHAKRA && technique.chakraInfo != null) {
                val chakraColor = Color(technique.chakraInfo.colorHex)
                val scaledSize = 28f * (0.9f + pulse * 0.1f)
                Box(modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(52.dp)) {
                        val c = center
                        // Rotating outer glow
                        rotate(rotation * 0.5f, pivot = c) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(chakraColor.copy(alpha = 0.3f * pulse), chakraColor.copy(alpha = 0.05f), Color.Transparent),
                                    center = c, radius = 26.dp.toPx()
                                ),
                                radius = 26.dp.toPx(), center = c
                            )
                            // Pulsing inner ring
                            drawCircle(chakraColor.copy(alpha = 0.4f * pulse), 20.dp.toPx(), c, style = Stroke(1.dp.toPx()))
                            // Outer ring
                            drawCircle(chakraColor.copy(alpha = 0.2f * pulse), 24.dp.toPx(), c, style = Stroke(0.5.dp.toPx()))
                            // Orbiting dot
                            val orbitAngle = rotation * 0.03f
                            val ox = c.x + 18.dp.toPx() * kotlin.math.cos(orbitAngle)
                            val oy = c.y + 18.dp.toPx() * kotlin.math.sin(orbitAngle)
                            drawCircle(chakraColor.copy(alpha = 0.6f * pulse), 1.5.dp.toPx(), Offset(ox, oy))
                        }
                    }
                    if (technique.id == "ajna") {
                        // Load custom Ajna symbol from drawable
                        Image(
                            painter = painterResource(id = R.drawable.ic_ajna),
                            contentDescription = "Ajna",
                            modifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(chakraColor.copy(alpha = 0.7f + pulse * 0.3f))
                        )
                    } else {
                        val isDigit = technique.chakraInfo.mantra.length >= 3
                        Text(
                            text = technique.chakraInfo.mantra,
                            style = TextStyle(
                                fontSize = if (isDigit) 13.sp else scaledSize.sp,
                                fontWeight = FontWeight.Bold,
                                color = chakraColor.copy(alpha = 0.7f + pulse * 0.3f)
                            ),
                            maxLines = 1
                        )
                    }
                }
            } else {
                Canvas(modifier = Modifier.size(52.dp)) {
                    drawPreviewGeometry(technique.geometryType, rotation, pulse)
                }
            }

            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(text = technique.localizedName(), style = MaterialTheme.typography.titleMedium)
                if (technique.category != TechniqueCategory.MANTRA) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = technique.localizedSubtitle(), style = MaterialTheme.typography.titleSmall)
                }

            }
            Text(text = technique.durationLabel, style = MaterialTheme.typography.labelSmall)
        }
    }
}

private fun DrawScope.drawPreviewGeometry(type: GeometryType, rotation: Float, pulse: Float) {
    val c = center
    val r = size.minDimension * 0.4f * pulse

    when (type) {
        GeometryType.SQUARE_MANDALA -> {
            for (i in 0..2) {
                rotate(rotation + i * 15f, pivot = c) {
                    val s = r * (1f - i * 0.2f)
                    drawRect(Gold.copy(alpha = 0.6f - i * 0.15f), Offset(c.x - s, c.y - s), Size(s * 2, s * 2), style = Stroke(1.dp.toPx()))
                }
            }
            drawCircle(Gold.copy(alpha = 0.8f), 2.dp.toPx(), c)
        }
        GeometryType.CIRCULAR_MANDALA -> {
            for (i in 0..2) { drawCircle(Gold.copy(alpha = 0.5f - i * 0.12f), r * (1f - i * 0.25f), c, style = Stroke(0.8.dp.toPx())) }
            for (i in 0 until 8) {
                val a = (2.0 * PI * i / 8) + Math.toRadians(rotation.toDouble())
                drawCircle(Gold.copy(alpha = 0.5f), 2.dp.toPx(), Offset(c.x + (r * 0.7f) * cos(a).toFloat(), c.y + (r * 0.7f) * sin(a).toFloat()))
            }
        }
        GeometryType.IDA_PINGALA -> {
            val p1 = Path(); val p2 = Path()
            for (i in 0..40) {
                val t = i / 40f; val y = c.y - r + t * r * 2
                val x = sin(t * 2 * PI + Math.toRadians(rotation.toDouble())).toFloat() * r * 0.4f
                if (i == 0) { p1.moveTo(c.x + x, y); p2.moveTo(c.x - x, y) } else { p1.lineTo(c.x + x, y); p2.lineTo(c.x - x, y) }
            }
            drawPath(p1, Gold.copy(alpha = 0.5f), style = Stroke(1.dp.toPx()))
            drawPath(p2, Gold.copy(alpha = 0.35f), style = Stroke(1.dp.toPx()))
            drawCircle(Gold.copy(alpha = 0.7f), 2.5.dp.toPx(), c)
        }
        GeometryType.FLOWER_OF_LIFE -> {
            val cr = r * 0.35f
            drawCircle(Gold.copy(alpha = 0.4f), cr, c, style = Stroke(0.8.dp.toPx()))
            for (i in 0 until 6) {
                val a = (2.0 * PI * i / 6) + Math.toRadians(rotation.toDouble() * 0.3)
                drawCircle(Gold.copy(alpha = 0.3f), cr, Offset(c.x + cr * cos(a).toFloat(), c.y + cr * sin(a).toFloat()), style = Stroke(0.6.dp.toPx()))
            }
        }
        GeometryType.SRI_YANTRA -> {
            for (i in 0..2) {
                val tr = r * (1f - i * 0.25f)
                rotate(rotation * 0.2f + i * 60f, c) { drawPath(Path().apply { moveTo(c.x, c.y - tr); lineTo(c.x + tr * 0.866f, c.y + tr * 0.5f); lineTo(c.x - tr * 0.866f, c.y + tr * 0.5f); close() }, Gold.copy(alpha = 0.4f - i * 0.08f), style = Stroke(0.8.dp.toPx())) }
                rotate(-rotation * 0.2f - i * 60f, c) { drawPath(Path().apply { moveTo(c.x, c.y + tr); lineTo(c.x + tr * 0.866f, c.y - tr * 0.5f); lineTo(c.x - tr * 0.866f, c.y - tr * 0.5f); close() }, Gold.copy(alpha = 0.3f - i * 0.06f), style = Stroke(0.8.dp.toPx())) }
            }
            drawCircle(Gold.copy(alpha = 0.7f), 2.dp.toPx(), c)
        }
        GeometryType.TRIPLE_LOTUS -> {
            for (ring in 0..2) {
                val rr = r * (0.4f + ring * 0.25f)
                for (i in 0 until (4 + ring * 2)) {
                    val a = (2.0 * PI * i / (4 + ring * 2)) + Math.toRadians(rotation.toDouble() * if (ring % 2 == 0) 0.3 else -0.2)
                    drawCircle(Gold.copy(alpha = 0.5f - ring * 0.1f), 2.dp.toPx(), Offset(c.x + rr * cos(a).toFloat(), c.y + rr * sin(a).toFloat()))
                }
                drawCircle(Gold.copy(alpha = (0.5f - ring * 0.1f) * 0.6f), rr, c, style = Stroke(0.5.dp.toPx()))
            }
            drawCircle(Gold.copy(alpha = 0.7f), 2.dp.toPx(), c)
        }
        GeometryType.CHAKRA -> { }
    }
}
