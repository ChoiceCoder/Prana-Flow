package com.pranaflow.ui.breathing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pranaflow.R
import com.pranaflow.engine.BreathingEngine
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathState
import com.pranaflow.model.GeometryType
import com.pranaflow.model.TimingConfig
import com.pranaflow.strings.S
import com.pranaflow.strings.localizedName
import com.pranaflow.strings.localizedDescription
import com.pranaflow.ui.breathing.geometry.AnulomVilomGeometry
import com.pranaflow.ui.components.bounceClick
import com.pranaflow.ui.breathing.geometry.BoxMandala
import com.pranaflow.ui.breathing.geometry.ChakraBodyVisualization
import com.pranaflow.ui.breathing.geometry.CircularMandala
import com.pranaflow.ui.breathing.geometry.FlowerOfLifeGeometry
import com.pranaflow.ui.breathing.geometry.SriYantraGeometry
import com.pranaflow.ui.breathing.geometry.TripleLotusGeometry
import com.pranaflow.ui.theme.Gold
import com.pranaflow.ui.theme.PranaTheme
import com.pranaflow.ui.theme.GoldBorder
import com.pranaflow.ui.theme.GoldDimText
import com.pranaflow.ui.theme.GoldPale
import com.pranaflow.ui.theme.Void
import com.pranaflow.ui.theme.VoidElevated
import kotlinx.coroutines.delay

@Composable
fun BreathingScreen(
    techniqueId: String,
    onBack: () -> Unit,
    viewModel: BreathingViewModel = hiltViewModel()
) {
    val engine = remember { BreathingEngine() }
    val technique by viewModel.technique.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val targetMin by viewModel.targetDurationMin.collectAsState()
    val elapsedSec by viewModel.sessionElapsedSec.collectAsState()
    val colors = PranaTheme.colors

    // UI visibility toggle (tap to show/hide controls)
    var controlsVisible by remember { mutableStateOf(true) }
    var showTimingPanel by remember { mutableStateOf(false) }

    // Frame-synced elapsed time for smooth animation
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var startTimeNano by remember { mutableLongStateOf(0L) }
    var isReady by remember { mutableStateOf(false) }

    // Load technique on entry
    LaunchedEffect(techniqueId) {
        viewModel.loadTechnique(techniqueId)
    }

    // Animation loop — runs at ~60fps via delay(16)
    LaunchedEffect(isRunning) {
        if (isRunning) {
            isReady = false
            delay(2000L) // 2-second pause so voice cues finish loading
            isReady = true
            startTimeNano = System.nanoTime()
            while (true) {
                elapsedMs = (System.nanoTime() - startTimeNano) / 1_000_000
                delay(16L)
            }
        } else {
            elapsedMs = 0L
            isReady = false
        }
    }

    // Compute current breath state — IDLE during 2-second ready period
    val timingConfig = viewModel.getCurrentTiming()
    val breathState = if (isRunning && isReady) {
        engine.computeState(elapsedMs, timingConfig)
    } else {
        BreathState(BreathPhase.IDLE, 0f, 0)
    }

    // Notify ViewModel of phase transitions for voice/haptic
    LaunchedEffect(breathState.phase) {
        if (isRunning) {
            viewModel.onPhaseUpdate(breathState.phase)
        }
    }

    // Auto-hide controls after 3 seconds when running
    LaunchedEffect(isRunning, controlsVisible) {
        if (isRunning && controlsVisible) {
            delay(3000)
            controlsVisible = false
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose { viewModel.stop() }
    }

    // Apply easing to phase progress for smooth visual movement
    val easedProgress = engine.easeInOut(breathState.phaseProgress)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PranaTheme.colors.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (isRunning) controlsVisible = !controlsVisible
                }
            )
    ) {
        // ── Meditation Silhouette Background ──
        val bgImageRes = if (colors.isDark) R.drawable.bg_meditation else R.drawable.bg_meditation_light
        Image(
            painter = painterResource(id = bgImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (colors.isDark) 0.6f else 0.25f),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // ── Sacred Geometry / Chakra Visualization (Center) ──
        val chakraInfo = technique.chakraInfo
        if (technique.geometryType == GeometryType.CHAKRA && chakraInfo != null) {
            // Chakra: full-screen body visualization
            ChakraBodyVisualization(
                chakraInfo = chakraInfo,
                breathState = breathState,
                easedProgress = easedProgress,
                glowIntensity = settings.glowIntensity,
                isDark = colors.isDark,
                modifier = Modifier.fillMaxSize()
            )
            // Chakra description ON TOP of body visualization
            val chakraInfoData = technique.chakraInfo
            val descText = technique.localizedDescription()
            if (descText.isNotEmpty()) {
                Text(
                    text = descText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 75.dp, start = 28.dp, end = 28.dp)
                )
            }
        } else {
            // Sacred geometry: centered with padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                when (technique.geometryType) {
                    GeometryType.SQUARE_MANDALA -> BoxMandala(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.CIRCULAR_MANDALA -> CircularMandala(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.IDA_PINGALA -> AnulomVilomGeometry(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.FLOWER_OF_LIFE -> FlowerOfLifeGeometry(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.SRI_YANTRA -> SriYantraGeometry(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.TRIPLE_LOTUS -> TripleLotusGeometry(
                        breathState = breathState,
                        easedProgress = easedProgress,
                        glowIntensity = settings.glowIntensity,
                        modifier = Modifier.size(300.dp)
                    )
                    GeometryType.CHAKRA -> { /* Handled above */ }
                }
            }
        }

        // ── Particle Aura (floating golden particles synced to breath) ──
        val isMusicHealing = technique.category == com.pranaflow.model.TechniqueCategory.MANTRA
        val isChakraMode = technique.geometryType == GeometryType.CHAKRA && !isMusicHealing
        if (isRunning && !isChakraMode) {
            ParticleAura(
                breathProgress = breathState.phaseProgress,
                breathPhase = breathState.phase,
                goldColor = colors.gold,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ── Breathing Cue Text with Progress Ring ──
        if (isRunning) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isMusicHealing) {
                    // Music healing: show healing message
                    Text(
                        text = S.current.thisMusicWillHealYou,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                } else if (isChakraMode) {
                    // Chakra mode: show "Repeat the mantra" instead of breathing phases
                    Text(
                        text = S.current.repeatTheMantra,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Breathing mode: animated phase label + cycle
                    val str = S.current
                    val phaseText = when (breathState.phase) {
                        BreathPhase.INHALE -> str.inhale
                        BreathPhase.HOLD_IN -> str.hold
                        BreathPhase.EXHALE -> str.exhale
                        BreathPhase.HOLD_OUT -> str.hold
                        BreathPhase.IDLE -> str.ready
                    }.uppercase()
                    AnimatedContent(
                        targetState = phaseText,
                        transitionSpec = {
                            (fadeIn(tween(300)) + slideInVertically(tween(300)) { -it / 3 })
                                .togetherWith(fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 3 })
                        },
                        label = "phase_transition"
                    ) { phaseLabel ->
                        Text(
                            text = phaseLabel,
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${S.current.cycle} ${breathState.cycleCount + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Controls Overlay (Top & Bottom) ──
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top bar: back + technique name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
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
                                onClick = {
                                    viewModel.stop()
                                    onBack()
                                }
                            )
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = technique.localizedName(),
                        style = MaterialTheme.typography.titleMedium.copy(color = colors.goldDimText)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Timing adjust button (not for chakra mode)
                    if (!isChakraMode) {
                        Text(
                            text = "⏱",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        showTimingPanel = !showTimingPanel
                                        if (isRunning) viewModel.stop()
                                    }
                                )
                                .padding(8.dp)
                        )
                    }
                }

                // Bottom: play/pause + session timer
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Duration selector pills (only when not running)
                    if (!isRunning && technique.id != "all_chakras") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(0, 5, 10, 15, 20).forEach { min ->
                                val label = if (min == 0) "∞" else "${min}m"
                                val selected = targetMin == min
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selected) colors.goldPale else colors.goldDimText,
                                        letterSpacing = 1.sp
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (selected) colors.gold.copy(alpha = 0.2f)
                                            else Color.Transparent
                                        )
                                        .border(
                                            width = 0.5.dp,
                                            color = if (selected) colors.gold.copy(alpha = 0.4f)
                                                else colors.gold.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { viewModel.setTargetDuration(min) }
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Play / Pause capsule
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(colors.gold.copy(alpha = 0.15f))
                            .border(
                                width = 0.5.dp,
                                color = colors.gold.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(25.dp)
                            )
                            .bounceClick { viewModel.toggleRunning() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isRunning) "❚❚" else "▶",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = colors.goldPale,
                                    fontSize = if (isRunning) 16.sp else 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRunning) S.current.pause else S.current.start,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = colors.goldPale,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }

                    // Elapsed / remaining time
                    if (isRunning) {
                        Spacer(modifier = Modifier.height(10.dp))
                        val mins = elapsedSec / 60
                        val secs = elapsedSec % 60
                        val timeText = if (targetMin > 0) {
                            val remSec = (targetMin * 60L - elapsedSec).coerceAtLeast(0)
                            val rm = remSec / 60; val rs = remSec % 60
                            "%d:%02d / %d:00".format(rm, rs, targetMin)
                        } else {
                            "%d:%02d".format(mins, secs)
                        }
                        Text(
                            text = timeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = colors.goldDimText,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }

        // ── Custom Timing Panel ──
        AnimatedVisibility(
            visible = showTimingPanel,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            TimingAdjustPanel(
                currentTiming = timingConfig,
                onTimingChanged = { viewModel.updateCustomTiming(it) },
                onDismiss = { showTimingPanel = false },
                onStart = {
                    showTimingPanel = false
                    viewModel.start()
                }
            )
        }
    }
}

/**
 * Timing customization panel with sliders for each phase duration.
 */
@Composable
private fun TimingAdjustPanel(
    currentTiming: TimingConfig,
    onTimingChanged: (TimingConfig) -> Unit,
    onDismiss: () -> Unit,
    onStart: () -> Unit
) {
    val colors = PranaTheme.colors
    var inhale by remember { mutableStateOf(currentTiming.inhaleMs / 1000f) }
    var holdIn by remember { mutableStateOf(currentTiming.holdInMs / 1000f) }
    var exhale by remember { mutableStateOf(currentTiming.exhaleMs / 1000f) }
    var holdOut by remember { mutableStateOf(currentTiming.holdOutMs / 1000f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colors.elevated)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = S.current.customTiming,
            style = MaterialTheme.typography.labelSmall
        )
        val s = S.current
        Spacer(modifier = Modifier.height(24.dp))

        TimingSlider(s.inhale, inhale) {
            inhale = it
            onTimingChanged(TimingConfig((inhale * 1000).toInt(), (holdIn * 1000).toInt(), (exhale * 1000).toInt(), (holdOut * 1000).toInt()))
        }
        TimingSlider("${s.hold} In", holdIn) {
            holdIn = it
            onTimingChanged(TimingConfig((inhale * 1000).toInt(), (holdIn * 1000).toInt(), (exhale * 1000).toInt(), (holdOut * 1000).toInt()))
        }
        TimingSlider(s.exhale, exhale) {
            exhale = it
            onTimingChanged(TimingConfig((inhale * 1000).toInt(), (holdIn * 1000).toInt(), (exhale * 1000).toInt(), (holdOut * 1000).toInt()))
        }
        TimingSlider("${s.hold} Out", holdOut) {
            holdOut = it
            onTimingChanged(TimingConfig((inhale * 1000).toInt(), (holdIn * 1000).toInt(), (exhale * 1000).toInt(), (holdOut * 1000).toInt()))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = S.current.cancel,
                style = MaterialTheme.typography.labelSmall.copy(color = colors.goldDimText),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    )
                    .padding(12.dp)
            )
            Text(
                text = S.current.start,
                style = MaterialTheme.typography.labelSmall.copy(color = colors.goldPale),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onStart
                    )
                    .padding(12.dp)
            )
        }
    }
}

@Composable
private fun TimingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    val colors = PranaTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = colors.goldDimText),
            modifier = Modifier.width(72.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..12f,
            steps = 23, // 0.5s increments
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = colors.gold,
                activeTrackColor = colors.gold.copy(alpha = 0.6f),
                inactiveTrackColor = colors.goldBorder
            )
        )
        Text(
            text = "${value.toInt()}s",
            style = MaterialTheme.typography.bodyMedium.copy(color = colors.goldPale),
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.End
        )
    }
}
