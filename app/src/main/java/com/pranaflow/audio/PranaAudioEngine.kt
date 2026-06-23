package com.pranaflow.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.pranaflow.model.BreathPhase
import com.pranaflow.model.TechniqueCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class VoiceGender(val label: String, val tag: String) {
    MALE("Male", "male"),
    FEMALE("Female", "female")
}

enum class VoiceLanguage(val label: String, val tag: String) {
    ENGLISH("English", "en"),
    HINDI("Hindi", "hi")
}

@Singleton
class PranaAudioEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // ── Voice Cues ──────────────────────────────────────────────

    private var soundPool: SoundPool? = null
    private var breatheInId = 0
    private var holdId = 0
    private var breatheOutId = 0
    private var cuesLoaded = false
    private var loadedCount = 0
    private var voiceEnabled = true
    private var lastSpokenPhase: BreathPhase? = null

    private var loadedGender: VoiceGender? = null
    private var loadedLanguage: VoiceLanguage? = null

    // ── Ambient / Mantra / Music Healing ────────────────────────

    private var ambientPlayer: MediaPlayer? = null
    private var ambientEnabled = true

    // Tracks the resource name currently loaded in ambientPlayer
    // so we avoid recreating MediaPlayer unnecessarily
    private var loadedAmbientResource: String? = null

    companion object {
        private const val AMBIENT_VOLUME = 0.40f
        private const val MUSIC_HEALING_VOLUME = 0.85f
        private const val TOTAL_CUES = 3
    }

    // ── Initialization ──────────────────────────────────────────

    fun initialize(gender: VoiceGender, language: VoiceLanguage) {
        if (gender != loadedGender || language != loadedLanguage) {
            releaseSoundPool()
            loadVoiceCues(gender, language)
            loadedGender = gender
            loadedLanguage = language
        }
        // Ambient player is prepared lazily in startSession
    }

    private fun loadVoiceCues(gender: VoiceGender, language: VoiceLanguage) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attrs)
            .build()

        loadedCount = 0
        cuesLoaded = false

        soundPool?.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                loadedCount++
                if (loadedCount >= TOTAL_CUES) cuesLoaded = true
            }
        }

        val prefix = "voice_${gender.tag}_${language.tag}"
        breatheInId = loadRawResource("${prefix}_breathe_in")
        holdId = loadRawResource("${prefix}_hold")
        breatheOutId = loadRawResource("${prefix}_breathe_out")
    }

    private fun loadRawResource(name: String): Int {
        val resId = context.resources.getIdentifier(name, "raw", context.packageName)
        if (resId == 0) return 0
        return soundPool?.load(context, resId, 1) ?: 0
    }

    // ── Ambient / Music Healing Player ──────────────────────────

    /**
     * Prepare the looping MediaPlayer for a given resource name.
     * Skips recreation if the same resource is already loaded.
     *
     * @param resourceName  raw resource name without extension
     *                      e.g. "ambient_01", "mantra_muladhara", "music_healing_03"
     * @param isMusicHealing  true → higher volume for solfeggio tracks
     */
    private fun prepareLoopingPlayer(resourceName: String, isMusicHealing: Boolean = false) {
        if (loadedAmbientResource == resourceName && ambientPlayer != null) return

        releaseAmbientPlayer()

        try {
            val resId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
            if (resId != 0) {
                val volume = if (isMusicHealing) MUSIC_HEALING_VOLUME else AMBIENT_VOLUME
                ambientPlayer = MediaPlayer.create(context, resId)?.apply {
                    isLooping = true
                    setVolume(volume, volume)
                }
                loadedAmbientResource = resourceName
            }
        } catch (_: Exception) { }
    }

    // ── Voice Cue Playback ──────────────────────────────────────

    fun onPhaseChanged(phase: BreathPhase) {
        if (!voiceEnabled || !cuesLoaded) return
        if (phase == lastSpokenPhase) return
        lastSpokenPhase = phase

        val soundId = when (phase) {
            BreathPhase.INHALE -> breatheInId
            BreathPhase.HOLD_IN -> holdId
            BreathPhase.EXHALE -> breatheOutId
            BreathPhase.HOLD_OUT -> holdId
            BreathPhase.IDLE -> return
        }

        if (soundId != 0) soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun setVoiceEnabled(enabled: Boolean) { voiceEnabled = enabled }

    fun resetPhaseTracking() { lastSpokenPhase = null }

    // ── Ambient Playback ────────────────────────────────────────

    fun startAmbient() {
        if (!ambientEnabled) return
        try { ambientPlayer?.let { if (!it.isPlaying) it.start() } } catch (_: Exception) { }
    }

    fun stopAmbient() {
        try { ambientPlayer?.let { if (it.isPlaying) it.pause() } } catch (_: Exception) { }
    }

    fun setAmbientEnabled(enabled: Boolean) {
        ambientEnabled = enabled
        if (!enabled) stopAmbient()
    }

    // ── Session Lifecycle ───────────────────────────────────────

    /**
     * @param voiceOn          play voice cues
     * @param ambientOn        play ambient/mantra/music track
     * @param ambientResource  which file to play — null means use previously loaded one
     * @param isMusicHealing   true for solfeggio tracks (higher volume)
     */
    fun startSession(
        voiceOn: Boolean,
        ambientOn: Boolean,
        ambientResource: String? = null,
        isMusicHealing: Boolean = false
    ) {
        voiceEnabled = voiceOn
        ambientEnabled = ambientOn
        resetPhaseTracking()

        if (ambientOn && ambientResource != null) {
            prepareLoopingPlayer(ambientResource, isMusicHealing)
        }

        if (ambientOn) startAmbient()
    }

    fun stopSession() {
        stopAmbient()
        resetPhaseTracking()
    }

    private fun releaseSoundPool() {
        soundPool?.release()
        soundPool = null
        cuesLoaded = false
        loadedCount = 0
        breatheInId = 0; holdId = 0; breatheOutId = 0
    }

    private fun releaseAmbientPlayer() {
        try { ambientPlayer?.release() } catch (_: Exception) { }
        ambientPlayer = null
        loadedAmbientResource = null
    }

    fun shutdown() {
        stopSession()
        releaseSoundPool()
        loadedGender = null
        loadedLanguage = null
        releaseAmbientPlayer()
    }
}
