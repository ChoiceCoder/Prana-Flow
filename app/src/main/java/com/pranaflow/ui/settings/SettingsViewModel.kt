package com.pranaflow.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pranaflow.audio.PranaAudioEngine
import com.pranaflow.audio.VoiceGender
import com.pranaflow.audio.VoiceLanguage
import com.pranaflow.datastore.SettingsDataStore
import com.pranaflow.datastore.UserSettings
import com.pranaflow.model.AmbientTrack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore,
    private val audioEngine: PranaAudioEngine
) : ViewModel() {

    val settings: StateFlow<UserSettings> = dataStore.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserSettings())

    fun toggleSound() {
        viewModelScope.launch { dataStore.setSoundEnabled(!settings.value.soundEnabled) }
    }

    fun toggleVibration() {
        viewModelScope.launch { dataStore.setVibrationEnabled(!settings.value.vibrationEnabled) }
    }

    fun setGlowIntensity(value: Float) {
        viewModelScope.launch { dataStore.setGlowIntensity(value) }
    }

    fun toggleDarkMode() {
        viewModelScope.launch { dataStore.setDarkMode(!settings.value.isDarkMode) }
    }

    fun toggleAmbientSound() {
        viewModelScope.launch { dataStore.setAmbientSoundEnabled(!settings.value.ambientSoundEnabled) }
    }

    fun setAmbientTrack(track: AmbientTrack) {
        viewModelScope.launch { dataStore.setSelectedAmbientTrack(track) }
    }

    fun setVoiceLanguage(language: VoiceLanguage) {
        viewModelScope.launch { dataStore.setVoiceLanguage(language) }
    }

    fun setVoiceGender(gender: VoiceGender) {
        viewModelScope.launch { dataStore.setVoiceGender(gender) }
    }

    // Called by SettingsScreen at line 188
    fun previewTrack(track: AmbientTrack) {
        audioEngine.startSession(
            voiceOn = false,
            ambientOn = true,
            ambientResource = track.resourceName,
            isMusicHealing = false
        )
    }

    // Alias in case both names are used
    fun previewAmbientTrack(track: AmbientTrack) = previewTrack(track)

    fun stopPreview() {
        audioEngine.stopSession()
    }

    // Called by SettingsScreen at line 214
    fun toggleHindi() {
        viewModelScope.launch { dataStore.setHindi(!settings.value.isHindi) }
    }

    // Alias — toggleLanguage and toggleHindi are the same thing
    fun toggleLanguage() = toggleHindi()

    override fun onCleared() {
        super.onCleared()
        audioEngine.stopSession()
    }
}
