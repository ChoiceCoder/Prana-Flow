package com.pranaflow.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pranaflow.audio.VoiceGender
import com.pranaflow.audio.VoiceLanguage
import com.pranaflow.model.AmbientTrack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prana_settings")

data class UserSettings(
    val soundEnabled: Boolean = true,
    val voiceGender: VoiceGender = VoiceGender.FEMALE,
    val voiceLanguage: VoiceLanguage = VoiceLanguage.ENGLISH,
    val ambientSoundEnabled: Boolean = true,
    val selectedAmbientTrack: AmbientTrack = AmbientTrack.TRACK_1,
    val vibrationEnabled: Boolean = true,
    val glowIntensity: Float = 0.7f,
    val isDarkMode: Boolean = false,
    val isHindi: Boolean = false
)

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VOICE_GENDER = stringPreferencesKey("voice_gender")
        val VOICE_LANGUAGE = stringPreferencesKey("voice_language")
        val AMBIENT_SOUND_ENABLED = booleanPreferencesKey("ambient_sound_enabled")
        val SELECTED_AMBIENT_TRACK = stringPreferencesKey("selected_ambient_track")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val GLOW_INTENSITY = floatPreferencesKey("glow_intensity")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_HINDI = booleanPreferencesKey("is_hindi")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            voiceGender = prefs[Keys.VOICE_GENDER]?.let {
                try { VoiceGender.valueOf(it) } catch (_: Exception) { VoiceGender.FEMALE }
            } ?: VoiceGender.FEMALE,
            voiceLanguage = prefs[Keys.VOICE_LANGUAGE]?.let {
                try { VoiceLanguage.valueOf(it) } catch (_: Exception) { VoiceLanguage.ENGLISH }
            } ?: VoiceLanguage.ENGLISH,
            ambientSoundEnabled = prefs[Keys.AMBIENT_SOUND_ENABLED] ?: true,
            selectedAmbientTrack = prefs[Keys.SELECTED_AMBIENT_TRACK]?.let {
                try { AmbientTrack.valueOf(it) } catch (_: Exception) { AmbientTrack.TRACK_1 }
            } ?: AmbientTrack.TRACK_1,
            vibrationEnabled = prefs[Keys.VIBRATION_ENABLED] ?: true,
            glowIntensity = prefs[Keys.GLOW_INTENSITY] ?: 0.7f,
            isDarkMode = prefs[Keys.IS_DARK_MODE] ?: false,
            isHindi = prefs[Keys.IS_HINDI] ?: false
        )
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setVoiceGender(gender: VoiceGender) {
        context.dataStore.edit { it[Keys.VOICE_GENDER] = gender.name }
    }

    suspend fun setVoiceLanguage(language: VoiceLanguage) {
        context.dataStore.edit { it[Keys.VOICE_LANGUAGE] = language.name }
    }

    suspend fun setAmbientSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AMBIENT_SOUND_ENABLED] = enabled }
    }

    suspend fun setSelectedAmbientTrack(track: AmbientTrack) {
        context.dataStore.edit { it[Keys.SELECTED_AMBIENT_TRACK] = track.name }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.VIBRATION_ENABLED] = enabled }
    }

    suspend fun setGlowIntensity(intensity: Float) {
        context.dataStore.edit { it[Keys.GLOW_INTENSITY] = intensity.coerceIn(0f, 1f) }
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { it[Keys.IS_DARK_MODE] = isDark }
    }

    suspend fun setHindi(isHindi: Boolean) {
        context.dataStore.edit { it[Keys.IS_HINDI] = isHindi }
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = true }
    }
}
