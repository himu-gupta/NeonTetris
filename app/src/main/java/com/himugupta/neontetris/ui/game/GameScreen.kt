package com.himugupta.neontetris.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.himugupta.neontetris.theme.DeepSpace
import com.himugupta.neontetris.theme.GridLine
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.NeonPink
import com.himugupta.neontetris.ui.components.NeonButton
import com.himugupta.neontetris.ui.components.StatPanel

@Composable
fun GameScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
  Column(
    modifier =
      modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(DeepSpace, MaterialTheme.colorScheme.background)))
        .safeDrawingPadding()
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      StatPanel("Score", "0", Modifier.weight(1f))
      StatPanel("Level", "1", Modifier.weight(1f))
      StatPanel("Lines", "0", Modifier.weight(1f))
    }
    Box(
      modifier =
        Modifier.weight(1f).aspectRatio(0.5f)
          .background(DeepSpace, RoundedCornerShape(16.dp))
          .border(1.dp, GridLine, RoundedCornerShape(16.dp)),
      contentAlignment = Alignment.Center,
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("ENGINE CHECKPOINT", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
        Text("Gameplay board loading next", color = InkMuted)
      }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      repeat(5) {
        Spacer(
          Modifier.weight(1f).size(48.dp)
            .background(if (it == 2) NeonPink else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp)),
        )
      }
    }
    NeonButton("Back", onClick = onBack, accent = NeonPink)
  }
}
