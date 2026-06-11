package com.himugupta.neontetris.desktop

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.himugupta.neontetris.NeonTetrisApp
import com.himugupta.neontetris.data.PreferencesRepository

fun main() = application {
  val preferencesRepository = remember { PreferencesRepository(DesktopPreferencesStorage()) }
  val gameAudio = remember { DesktopGameAudio() }
  DisposableEffect(gameAudio) { onDispose(gameAudio::release) }

  Window(
    onCloseRequest = ::exitApplication,
    title = "Neon Tetris",
    state = rememberWindowState(width = 430.dp, height = 860.dp),
  ) {
    NeonTetrisApp(preferencesRepository = preferencesRepository, gameAudio = gameAudio)
  }
}
