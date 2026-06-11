package com.himugupta.neontetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.himugupta.neontetris.audio.AndroidGameAudio
import com.himugupta.neontetris.data.AndroidPreferencesStorage
import com.himugupta.neontetris.data.PreferencesRepository

class MainActivity : ComponentActivity() {
  private lateinit var gameAudio: AndroidGameAudio

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val preferencesRepository = PreferencesRepository(AndroidPreferencesStorage(applicationContext))
    gameAudio = AndroidGameAudio()

    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
    )
    window.isNavigationBarContrastEnforced = false
    setContent { NeonTetrisApp(preferencesRepository = preferencesRepository, gameAudio = gameAudio) }
  }

  override fun onDestroy() {
    gameAudio.release()
    super.onDestroy()
  }
}
