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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.himugupta.neontetris.theme.DeepSpace
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.NeonViolet
import com.himugupta.neontetris.ui.components.NeonButton
import com.himugupta.neontetris.ui.components.NeonPanel
import com.himugupta.neontetris.ui.components.SettingsRow

@Composable
fun SettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  var ghost by rememberSaveable { mutableStateOf(true) }
  var haptics by rememberSaveable { mutableStateOf(true) }
  var sound by rememberSaveable { mutableStateOf(true) }
  var reducedMotion by rememberSaveable { mutableStateOf(false) }

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
        SettingsRow("Ghost piece", "Show the projected landing position.") { Switch(ghost, { ghost = it }) }
        SettingsRow("Haptics", "Feel rotations, drops, and clears.") { Switch(haptics, { haptics = it }) }
        SettingsRow("Sound", "Enable arcade sound feedback.") { Switch(sound, { sound = it }) }
        SettingsRow("Reduced motion", "Replace spatial effects with fades.") { Switch(reducedMotion, { reducedMotion = it }) }
      }
    }
    Spacer(Modifier.weight(1f))
    NeonButton("Done", onClick = onBack, accent = NeonViolet)
    Spacer(Modifier.height(2.dp))
  }
}
