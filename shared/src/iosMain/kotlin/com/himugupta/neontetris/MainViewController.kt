package com.himugupta.neontetris

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.himugupta.neontetris.data.PreferencesRepository

fun MainViewController() = ComposeUIViewController {
  val preferencesRepository = remember { PreferencesRepository(IosPreferencesStorage()) }
  val gameAudio = remember { IosGameAudio() }
  DisposableEffect(gameAudio) { onDispose(gameAudio::release) }
  NeonTetrisApp(preferencesRepository = preferencesRepository, gameAudio = gameAudio)
}
