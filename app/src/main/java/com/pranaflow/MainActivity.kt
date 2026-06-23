package com.pranaflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pranaflow.datastore.SettingsDataStore
import com.pranaflow.strings.EnglishStrings
import com.pranaflow.strings.HindiStrings
import com.pranaflow.strings.LocalAppStrings
import com.pranaflow.ui.navigation.PranaNavHost
import com.pranaflow.ui.theme.PranaFlowTheme
import com.pranaflow.ui.theme.PranaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val settings by settingsDataStore.settings.collectAsState(
                initial = com.pranaflow.datastore.UserSettings()
            )
            val appStrings = if (settings.isHindi) HindiStrings else EnglishStrings

            PranaFlowTheme(isDarkMode = settings.isDarkMode) {
                CompositionLocalProvider(LocalAppStrings provides appStrings) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = PranaTheme.colors.background
                    ) {
                        PranaNavHost(settingsDataStore = settingsDataStore)
                    }
                }
            }
        }
    }
}
