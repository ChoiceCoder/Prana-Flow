package com.pranaflow.ui.category

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranaflow.model.TechniqueCategory
import com.pranaflow.model.Techniques
import com.pranaflow.ui.components.TechniqueCard
import com.pranaflow.ui.theme.PranaTheme
import com.pranaflow.strings.S
import com.pranaflow.strings.localizedLabel
import com.pranaflow.strings.localizedSubtitle

@Composable
fun CategoryScreen(
    categoryName: String,
    onTechniqueSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val category = try {
        TechniqueCategory.valueOf(categoryName)
    } catch (_: Exception) {
        TechniqueCategory.RELAX
    }
    val techniques = Techniques.all.filter { it.category == category }
    val colors = PranaTheme.colors

    val inf = rememberInfiniteTransition(label = "cat_bg")
    val bgRot by inf.animateFloat(
        0f, 360f,
        androidx.compose.animation.core.infiniteRepeatable(
            androidx.compose.animation.core.tween(40000, easing = androidx.compose.animation.core.LinearEasing),
            androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "cat_bg_rot"
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        // Background concentric circle art (matches HomeScreen)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width * 0.5f
            val cy = size.height * 0.35f
            val artCenter = Offset(cx, cy)
            val maxR = size.width * 0.85f
            val ringCount = 10

            for (i in 1..ringCount) {
                val r = maxR * (i.toFloat() / ringCount)
                val alpha = if (colors.isDark) 0.04f + (i * 0.006f) else 0.08f + (i * 0.008f)
                drawCircle(
                    colors.gold.copy(alpha = alpha), r, artCenter,
                    style = Stroke(if (i % 3 == 0) 1.2.dp.toPx() else 0.6.dp.toPx())
                )
            }
            rotate(bgRot * 0.3f, pivot = artCenter) {
                for (ring in listOf(3, 5, 7)) {
                    val r = maxR * (ring.toFloat() / ringCount)
                    val dotCount = ring * 3
                    val dotAlpha = if (colors.isDark) 0.10f else 0.12f
                    for (j in 0 until dotCount) {
                        val a = (2.0 * kotlin.math.PI * j / dotCount).toFloat()
                        drawCircle(
                            colors.gold.copy(alpha = dotAlpha), 1.5.dp.toPx(),
                            Offset(artCenter.x + r * kotlin.math.cos(a).toFloat(), artCenter.y + r * kotlin.math.sin(a).toFloat())
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                style = MaterialTheme.typography.headlineMedium.copy(color = colors.goldDimText),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBack
                    )
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = category.localizedLabel().uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 2.sp)
                )
                Text(
                    text = category.localizedSubtitle(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = colors.goldDimText,
                        fontSize = 12.sp
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(items = techniques, key = { it.id }) { technique ->
                TechniqueCard(
                    technique = technique,
                    onClick = { onTechniqueSelected(technique.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
        }
    }
}
