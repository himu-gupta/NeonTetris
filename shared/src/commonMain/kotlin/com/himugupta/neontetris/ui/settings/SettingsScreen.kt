package com.himugupta.neontetris.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.himugupta.neontetris.theme.DeepSpace
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.NeonViolet
import com.himugupta.neontetris.data.PlayerPreferences
import com.himugupta.neontetris.data.PreferencesRepository
import com.himugupta.neontetris.ui.components.NeonButton
import com.himugupta.neontetris.ui.components.NeonPanel
import com.himugupta.neontetris.ui.components.SettingsRow

@Composable
fun SettingsScreen(
  preferences: PlayerPreferences,
  repository: PreferencesRepository,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier =
      modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(DeepSpace, MaterialTheme.colorScheme.background)))
        .safeDrawingPadding()
        .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
  ) {
    Text("SETTINGS", style = MaterialTheme.typography.headlineMedium)
    Text("Tune feedback without changing the rules.", color = InkMuted)
    NeonPanel(Modifier.fillMaxWidth()) {
      Column {
        SettingsRow("Ghost piece", "Show the projected landing position.") {
          Switch(preferences.ghostEnabled, repository::setGhostEnabled)
        }
        SettingsRow("Haptics", "Feel rotations, drops, and clears.") {
          Switch(preferences.hapticsEnabled, repository::setHapticsEnabled)
        }
        SettingsRow("Music and sound", "Play the neon soundtrack and game effects.") {
          Switch(preferences.soundEnabled, repository::setSoundEnabled)
        }
        SettingsRow("Reduced motion", "Replace spatial effects with instant feedback.") {
          Switch(preferences.reducedMotion, repository::setReducedMotion)
        }
      }
    }
    Spacer(Modifier.weight(1f))
    NeonButton("Done", onClick = onBack, accent = NeonViolet)
    Spacer(Modifier.height(2.dp))
  }
}
