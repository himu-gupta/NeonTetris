package com.himugupta.neontetris.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonColorScheme =
  darkColorScheme(
    primary = NeonCyan,
    onPrimary = Midnight,
    primaryContainer = PanelRaised,
    onPrimaryContainer = Ink,
    secondary = NeonViolet,
    onSecondary = Midnight,
    tertiary = NeonPink,
    background = Midnight,
    onBackground = Ink,
    surface = DeepSpace,
    onSurface = Ink,
    surfaceVariant = Panel,
    onSurfaceVariant = InkMuted,
    error = Danger,
  )

@Composable
fun NeonTetrisTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = NeonColorScheme, typography = Typography, content = content)
}
