package com.pranaflow.ui.onboarding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pranaflow.strings.S
import com.pranaflow.ui.components.bounceClick
import com.pranaflow.ui.theme.PranaTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val description: String
)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val s = S.current
    val pages = listOf(
        OnboardingPage(
            title = s.welcome,
            subtitle = s.pranaFlow.uppercase(),
            description = s.welcomeDesc
        ),
        OnboardingPage(
            title = s.chooseYourPath,
            subtitle = s.pranaFlow.uppercase(),
            description = s.choosePathDesc
        ),
        OnboardingPage(
            title = s.justBreathe,
            subtitle = s.pranaFlow.uppercase(),
            description = s.justBreatheDesc
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val colors = PranaTheme.colors

    val infiniteTransition = rememberInfiniteTransition(label = "onboard")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(30000, easing = LinearEasing), RepeatMode.Restart),
        label = "onboard_rot"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Reverse),
        label = "onboard_pulse"
    )

    // Theme-aware text colors
    val titleColor = colors.textPrimary
    val subtitleColor = colors.goldDark
    val descColor = if (colors.isDark) colors.goldDimText else Color(0xFF5A4E3C)
    val buttonTextColor = if (colors.isDark) colors.goldPale else Color(0xFF3A3020)
    val hintColor = if (colors.isDark) colors.goldDimText else Color(0xFF8A7A60)
    val artAlpha = if (colors.isDark) 0.08f else 0.15f

    Box(
        modifier = Modifier.fillMaxSize().background(colors.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val page = pages[pageIndex]

            Box(modifier = Modifier.fillMaxSize()) {
                // Background animation per page
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val c = center
                    val r = size.minDimension * 0.25f
                    val gc = colors.gold

                    when (pageIndex) {
                        0 -> {
                            rotate(rotation * 0.5f, pivot = c) {
                                for (i in 0..3) {
                                    val cr = r * (0.5f + i * 0.2f) * pulse
                                    drawCircle(
                                        gc.copy(alpha = artAlpha + i * 0.02f),
                                        cr, c, style = Stroke(0.8.dp.toPx())
                                    )
                                }
                            }
                        }
                        1 -> {
                            for (i in 0..2) {
                                val angle = (-30f + i * 30f) * PI.toFloat() / 180f
                                val path = Path().apply {
                                    moveTo(c.x, c.y + r)
                                    quadraticBezierTo(
                                        c.x + r * 0.5f * sin(angle + rotation * 0.01f),
                                        c.y,
                                        c.x + r * sin(angle) * 1.5f,
                                        c.y - r
                                    )
                                }
                                drawPath(path, gc.copy(alpha = artAlpha * pulse), style = Stroke(1.dp.toPx()))
                            }
                        }
                        2 -> {
                            rotate(rotation * 0.3f, pivot = c) {
                                val petalCount = 8
                                for (i in 0 until petalCount) {
                                    val ang = (2.0 * PI * i / petalCount).toFloat()
                                    val bx = c.x + r * 0.3f * cos(ang)
                                    val by = c.y + r * 0.3f * sin(ang)
                                    val tx = c.x + r * 0.8f * cos(ang)
                                    val ty = c.y + r * 0.8f * sin(ang)
                                    val px = -sin(ang); val py = cos(ang)
                                    val w = r * 0.1f
                                    val p = Path().apply {
                                        moveTo(bx, by)
                                        quadraticBezierTo(
                                            (bx + tx) / 2 + w * px, (by + ty) / 2 + w * py,
                                            tx, ty
                                        )
                                        quadraticBezierTo(
                                            (bx + tx) / 2 - w * px, (by + ty) / 2 - w * py,
                                            bx, by
                                        )
                                    }
                                    drawPath(p, gc.copy(alpha = (artAlpha + 0.03f) * pulse), style = Stroke(0.8.dp.toPx()))
                                }
                            }
                            drawCircle(gc.copy(alpha = artAlpha * 2 * pulse), r * 0.15f, c)
                        }
                    }
                }

                // Text content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(80.dp))

                    Text(
                        text = page.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = subtitleColor,
                            letterSpacing = 3.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = titleColor,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = descColor,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Bottom: dot indicators + button
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot indicators
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(pages.size) { index ->
                    val isActive = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isActive) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) colors.gold else colors.goldBorder
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (pagerState.currentPage == pages.size - 1) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(colors.gold.copy(alpha = if (colors.isDark) 0.15f else 0.25f))
                        .border(
                            width = 0.5.dp,
                            color = colors.gold.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .bounceClick(onComplete),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = s.getStarted,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = buttonTextColor,
                            letterSpacing = 3.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Text(
                    text = s.swipeToContinue,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = hintColor,
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}
