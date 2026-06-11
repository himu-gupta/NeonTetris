package com.himugupta.neontetris.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himugupta.neontetris.theme.DeepSpace
import com.himugupta.neontetris.theme.GridLine
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.NeonTetrisTheme
import com.himugupta.neontetris.theme.NeonViolet
import com.himugupta.neontetris.ui.components.NeonButton
import com.himugupta.neontetris.ui.components.NeonPanel
import com.himugupta.neontetris.ui.components.TetrominoMark

@Composable
fun HomeScreen(
  onPlay: () -> Unit,
  onSettings: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val transition = rememberInfiniteTransition(label = "homeAmbient")
  val pulse by transition.animateFloat(
    initialValue = 0.94f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
    label = "logoPulse",
  )

  Box(
    modifier =
      modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(DeepSpace, MaterialTheme.colorScheme.background))),
  ) {
    ArcadeGrid(Modifier.fillMaxSize().alpha(0.45f))
    Column(
      modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = 24.dp, vertical = 18.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Text("BEST  0", color = InkMuted, style = MaterialTheme.typography.labelLarge)
      }
      Spacer(Modifier.weight(0.8f))
      TetrominoMark(Modifier.size(82.dp).scale(pulse))
      Spacer(Modifier.height(24.dp))
      Text(
        text = "NEON\nTETRIS",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.onBackground,
      )
      Spacer(Modifier.height(14.dp))
      Text(
        text = "STACK CLEAN. CHASE THE GLOW.",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
      )
      Spacer(Modifier.height(26.dp))
      NeonPanel(Modifier.fillMaxWidth()) {
        Text(
          "7-BAG  /  GHOST  /  HOLD  /  COMBOS",
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          color = InkMuted,
          style = MaterialTheme.typography.labelLarge,
        )
      }
      Spacer(Modifier.weight(1f))
      NeonButton(text = "Start game", onClick = onPlay)
      Spacer(Modifier.height(12.dp))
      NeonButton(text = "Settings", onClick = onSettings, accent = NeonViolet)
      Spacer(Modifier.height(12.dp))
      Text("Swipe or use precision controls", color = InkMuted, style = MaterialTheme.typography.bodyMedium)
    }
  }
}

@Composable
private fun ArcadeGrid(modifier: Modifier = Modifier) {
  Canvas(modifier) {
    val step = 32.dp.toPx()
    var x = 0f
    while (x < size.width) {
      drawLine(GridLine, Offset(x, 0f), Offset(x, size.height), 1f)
      x += step
    }
    var y = 0f
    while (y < size.height) {
      drawLine(GridLine, Offset(0f, y), Offset(size.width, y), 1f)
      y += step
    }
    drawCircle(
      brush = Brush.radialGradient(listOf(Color.Transparent, DeepSpace.copy(alpha = 0.9f))),
      radius = size.maxDimension * 0.7f,
      center = center,
    )
  }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomePreview() {
  NeonTetrisTheme { HomeScreen(onPlay = {}, onSettings = {}) }
}
