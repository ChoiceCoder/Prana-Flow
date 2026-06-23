package com.pranaflow.ui.breathing

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pranaflow.audio.PranaAudioEngine
import com.pranaflow.audio.VoiceGender
import com.pranaflow.audio.VoiceLanguage
import com.pranaflow.datastore.SettingsDataStore
import com.pranaflow.datastore.UserSettings
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.BreathingTechnique
import com.pranaflow.model.TechniqueCategory
import com.pranaflow.model.Techniques
import com.pranaflow.model.TimingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreathingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioEngine: PranaAudioEngine,
    settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _technique = MutableStateFlow(Techniques.boxBreathing)
    val technique: StateFlow<BreathingTechnique> = _technique.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _customTiming = MutableStateFlow<TimingConfig?>(null)
    val customTiming: StateFlow<TimingConfig?> = _customTiming.asStateFlow()

    private val _targetDurationMin = MutableStateFlow(0)
    val targetDurationMin: StateFlow<Int> = _targetDurationMin.asStateFlow()

    private val _sessionElapsedSec = MutableStateFlow(0L)
    val sessionElapsedSec: StateFlow<Long> = _sessionElapsedSec.asStateFlow()

    val settings: StateFlow<UserSettings> = settingsDataStore.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserSettings())

    private var lastPhase: BreathPhase = BreathPhase.IDLE
    private var timerJob: Job? = null

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun loadTechnique(id: String) {
        _technique.value = Techniques.findById(id)
        val s = settings.value
        audioEngine.initialize(s.voiceGender, s.voiceLanguage)
    }

    fun getCurrentTiming(): TimingConfig =
        _customTiming.value ?: _technique.value.defaultTiming

    fun updateCustomTiming(timing: TimingConfig) {
        _customTiming.value = timing
    }

    fun setTargetDuration(minutes: Int) {
        _targetDurationMin.value = minutes
    }

    fun start() {
        val s = settings.value
        val tech = _technique.value
        val isMusicHealing = tech.category == TechniqueCategory.MANTRA
        val ambientResource = when {
            // Music healing: always play the technique's own track
            isMusicHealing -> tech.chakraInfo?.mantraResource
            // Chakra mode: play mantra track
            tech.chakraInfo?.mantraResource?.isNotEmpty() == true -> tech.chakraInfo.mantraResource
            // Regular breathing: play selected ambient track if enabled
            else -> if (s.ambientSoundEnabled) s.selectedAmbientTrack.resourceName else null
        }

        audioEngine.startSession(
            voiceOn = s.soundEnabled && !isMusicHealing,
            ambientOn = true,
            ambientResource = ambientResource,
            isMusicHealing = isMusicHealing
        )

        lastPhase = BreathPhase.IDLE
        _sessionElapsedSec.value = 0L
        _isRunning.value = true

        // Start elapsed-time ticker
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val elapsed = _sessionElapsedSec.value + 1
                _sessionElapsedSec.value = elapsed

                // Auto-stop when target duration reached
                val target = _targetDurationMin.value
                if (target > 0 && elapsed >= target * 60L) {
                    stop()
                    break
                }
            }
        }
    }

    fun stop() {
        _isRunning.value = false
        timerJob?.cancel()
        timerJob = null
        audioEngine.stopSession()
    }

    fun toggleRunning() {
        if (_isRunning.value) stop() else start()
    }

    fun onPhaseUpdate(phase: BreathPhase) {
        if (phase != lastPhase) {
            lastPhase = phase
            audioEngine.onPhaseChanged(phase)
            if (settings.value.vibrationEnabled) vibrateSubtle()
        }
    }

    private fun vibrateSubtle() {
        val effect = VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioEngine.shutdown()
    }
}
