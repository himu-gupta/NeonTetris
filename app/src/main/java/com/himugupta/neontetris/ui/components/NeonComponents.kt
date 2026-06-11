package com.himugupta.neontetris.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.himugupta.neontetris.theme.GridLine
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.Panel

@Composable
fun NeonButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  accent: Color = MaterialTheme.colorScheme.primary,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val pressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "buttonScale")
  val container by animateColorAsState(
    if (pressed) accent.copy(alpha = 0.78f) else accent,
    label = "buttonColor",
  )

  Button(
    onClick = onClick,
    modifier = modifier.fillMaxWidth().height(58.dp).scale(scale),
    shape = RoundedCornerShape(18.dp),
    colors = ButtonDefaults.buttonColors(containerColor = container, contentColor = MaterialTheme.colorScheme.onPrimary),
    border = BorderStroke(1.dp, accent.copy(alpha = 0.8f)),
    interactionSource = interactionSource,
  ) {
    Text(text = text.uppercase(), style = MaterialTheme.typography.labelLarge)
  }
}

@Composable
fun NeonPanel(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Box(
    modifier =
      modifier
        .background(Panel.copy(alpha = 0.88f), RoundedCornerShape(22.dp))
        .border(1.dp, GridLine.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
        .padding(18.dp),
  ) {
    content()
  }
}

@Composable
fun StatPanel(label: String, value: String, modifier: Modifier = Modifier) {
  NeonPanel(modifier = modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(label.uppercase(), color = InkMuted, style = MaterialTheme.typography.labelLarge)
      Spacer(Modifier.height(4.dp))
      Text(value, style = MaterialTheme.typography.titleLarge)
    }
  }
}

@Composable
fun SettingsRow(
  title: String,
  description: String,
  trailing: @Composable RowScope.() -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(title, style = MaterialTheme.typography.titleLarge)
      Text(description, color = InkMuted, style = MaterialTheme.typography.bodyMedium)
    }
    trailing()
  }
}

@Composable
fun TetrominoMark(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
  Row(modifier, horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.Bottom) {
    Spacer(Modifier.size(15.dp).background(color, RoundedCornerShape(3.dp)))
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
      Spacer(Modifier.size(15.dp).background(color, RoundedCornerShape(3.dp)))
      Spacer(Modifier.size(15.dp).background(color, RoundedCornerShape(3.dp)))
    }
    Spacer(Modifier.size(15.dp).background(color, RoundedCornerShape(3.dp)))
  }
}
