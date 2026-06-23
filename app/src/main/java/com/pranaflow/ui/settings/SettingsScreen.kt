package com.pranaflow.ui.settings

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pranaflow.audio.VoiceLanguage
import com.pranaflow.model.AmbientTrack
import com.pranaflow.ui.theme.DividerColor
import com.pranaflow.ui.theme.Gold
import com.pranaflow.ui.theme.PranaTheme
import com.pranaflow.strings.S
import com.pranaflow.strings.localizedLabel
import com.pranaflow.ui.theme.GoldBorder
import com.pranaflow.ui.theme.GoldDark
import com.pranaflow.ui.theme.GoldDimText
import com.pranaflow.ui.theme.GoldPale
import com.pranaflow.ui.theme.Void
import com.pranaflow.ui.theme.VoidElevated

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val s = S.current
    val colors = PranaTheme.colors

    val inf = rememberInfiniteTransition(label = "settings_bg")
    val settingsRot by inf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(50000, easing = LinearEasing), RepeatMode.Restart),
        label = "settings_rot"
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        // Background concentric circle art (matches HomeScreen)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width * 0.5f
            val cy = size.height * 0.3f
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
            rotate(settingsRot * 0.2f, pivot = artCenter) {
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "←",
                style = MaterialTheme.typography.headlineMedium.copy(color = PranaTheme.colors.goldDimText),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBack
                    )
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = S.current.settings,
                style = MaterialTheme.typography.headlineMedium.copy(letterSpacing = 4.sp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // ── VOICE ──
        SectionLabel(s.voice)

        SettingToggle(
            label = S.current.voiceGuidance,
            description = S.current.spokenBreathingCues,
            isChecked = settings.soundEnabled,
            onToggle = { viewModel.toggleSound() }
        )

        if (settings.soundEnabled) {
            Spacer(modifier = Modifier.height(16.dp))

            PillSelector(
                label = S.current.language,
                options = listOf(s.english, s.hindi),
                selectedIndex = VoiceLanguage.entries.indexOf(settings.voiceLanguage),
                onSelected = { viewModel.setVoiceLanguage(VoiceLanguage.entries[it]) }
            )
        }

        SettingDivider()

        // ── AMBIENT ──
        SectionLabel(s.ambient)

        SettingToggle(
            label = S.current.ambientSound,
            description = S.current.backgroundMusic,
            isChecked = settings.ambientSoundEnabled,
            onToggle = { viewModel.toggleAmbientSound() }
        )

        if (settings.ambientSoundEnabled) {
            Spacer(modifier = Modifier.height(14.dp))

            // Dropdown track selector with preview
            AmbientTrackDropdown(
                selectedTrack = settings.selectedAmbientTrack,
                onTrackSelected = { viewModel.setAmbientTrack(it) },
                onPreview = { viewModel.previewTrack(it) },
                onStopPreview = { viewModel.stopPreview() }
            )
        }

        SettingDivider()

        // ── FEEDBACK ──
        SectionLabel(S.current.feedbackSection)

        SettingToggle(
            label = S.current.hapticFeedback,
            description = S.current.gentleVibration,
            isChecked = settings.vibrationEnabled,
            onToggle = { viewModel.toggleVibration() }
        )

        SettingDivider()

        // ── VISUAL ──
        SectionLabel(S.current.visualSection)

        SettingToggle(
            label = "हिंदी / Hindi",
            description = if (settings.isHindi) "हिंदी भाषा सक्रिय" else "English active",
            isChecked = settings.isHindi,
            onToggle = { viewModel.toggleHindi() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingToggle(
            label = S.current.darkMode,
            description = if (settings.isDarkMode) S.current.darkThemeActive else S.current.lightThemeActive,
            isChecked = settings.isDarkMode,
            onToggle = { viewModel.toggleDarkMode() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = S.current.glowIntensity, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = S.current.sacredGeometryLuminance, style = MaterialTheme.typography.titleSmall)
                }
                Text(
                    text = "${(settings.glowIntensity * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium.copy(color = PranaTheme.colors.goldPale),
                    modifier = Modifier.width(48.dp),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Slider(
                value = settings.glowIntensity,
                onValueChange = { viewModel.setGlowIntensity(it) },
                valueRange = 0.1f..1f,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = PranaTheme.colors.gold,
                    activeTrackColor = PranaTheme.colors.gold.copy(alpha = 0.6f),
                    inactiveTrackColor = PranaTheme.colors.goldBorder
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = S.current.pranaFlow,
            style = MaterialTheme.typography.labelSmall.copy(color = PranaTheme.colors.goldDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )
    }
    }
}

// ── Ambient Track Selector Item ─────────────────────────────────

@Composable
private fun AmbientTrackDropdown(
    selectedTrack: AmbientTrack,
    onTrackSelected: (AmbientTrack) -> Unit,
    onPreview: (AmbientTrack) -> Unit,
    onStopPreview: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var previewing by remember { mutableStateOf<AmbientTrack?>(null) }

    Column {
        Text(
            text = S.current.backgroundTrack,
            style = MaterialTheme.typography.bodyMedium.copy(color = PranaTheme.colors.goldDimText),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        // Selected track display (tap to expand)
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PranaTheme.colors.elevated)
                    .border(0.5.dp, PranaTheme.colors.goldBorder, RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { expanded = true }
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PranaTheme.colors.gold)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selectedTrack.localizedLabel(),
                    style = MaterialTheme.typography.bodyMedium.copy(color = PranaTheme.colors.goldPale),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (expanded) "▲" else "▼",
                    style = MaterialTheme.typography.labelSmall.copy(color = PranaTheme.colors.goldDimText)
                )
            }

            // Dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    if (previewing != null) {
                        onStopPreview()
                        previewing = null
                    }
                },
                modifier = Modifier
                    .background(PranaTheme.colors.elevated)
                    .width(280.dp)
            ) {
                AmbientTrack.entries.forEach { track ->
                    val isSelected = track == selectedTrack
                    val isPlaying = track == previewing

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) PranaTheme.colors.gold else PranaTheme.colors.goldBorder
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = track.localizedLabel(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isSelected) PranaTheme.colors.goldPale else PranaTheme.colors.goldDimText
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                // Preview play/stop button
                                Text(
                                    text = if (isPlaying) "■" else "▶",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = if (isPlaying) PranaTheme.colors.gold else PranaTheme.colors.goldDimText,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                if (isPlaying) {
                                                    onStopPreview()
                                                    previewing = null
                                                } else {
                                                    onStopPreview()
                                                    onPreview(track)
                                                    previewing = track
                                                }
                                            }
                                        )
                                        .padding(8.dp)
                                )
                            }
                        },
                        onClick = {
                            onTrackSelected(track)
                            onStopPreview()
                            previewing = null
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// ── Shared Components ───────────────────────────────────────────

@Composable
private fun PillSelector(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = PranaTheme.colors.goldDimText),
            modifier = Modifier.width(80.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex
                val bg = if (isSelected) PranaTheme.colors.gold.copy(alpha = 0.15f) else PranaTheme.colors.elevated
                val borderColor = if (isSelected) PranaTheme.colors.gold.copy(alpha = 0.6f) else PranaTheme.colors.goldBorder
                val textColor = if (isSelected) PranaTheme.colors.goldPale else PranaTheme.colors.goldDimText

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(bg)
                        .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onSelected(index) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = textColor,
                            letterSpacing = 1.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

@Composable
private fun SettingToggle(
    label: String,
    description: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.titleSmall)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PranaTheme.colors.gold,
                checkedTrackColor = PranaTheme.colors.goldDark.copy(alpha = 0.4f),
                uncheckedThumbColor = PranaTheme.colors.goldDark,
                uncheckedTrackColor = PranaTheme.colors.elevated
            )
        )
    }
}

@Composable
private fun SettingDivider() {
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(PranaTheme.colors.divider)
    )
    Spacer(modifier = Modifier.height(8.dp))
}
