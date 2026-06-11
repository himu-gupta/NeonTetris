package com.himugupta.neontetris.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerPreferences(
  val highScore: Int = 0,
  val ghostEnabled: Boolean = true,
  val hapticsEnabled: Boolean = true,
  val soundEnabled: Boolean = true,
  val reducedMotion: Boolean = false,
)

class PreferencesRepository(context: Context) {
  private val storage = context.getSharedPreferences(StorageName, Context.MODE_PRIVATE)
  private val _preferences = MutableStateFlow(readPreferences())
  val preferences: StateFlow<PlayerPreferences> = _preferences.asStateFlow()

  fun setGhostEnabled(enabled: Boolean) = update(KeyGhost, enabled) { copy(ghostEnabled = enabled) }

  fun setHapticsEnabled(enabled: Boolean) = update(KeyHaptics, enabled) { copy(hapticsEnabled = enabled) }

  fun setSoundEnabled(enabled: Boolean) = update(KeySound, enabled) { copy(soundEnabled = enabled) }

  fun setReducedMotion(enabled: Boolean) = update(KeyReducedMotion, enabled) { copy(reducedMotion = enabled) }

  fun updateHighScore(score: Int) {
    if (score <= _preferences.value.highScore) return
    storage.edit().putInt(KeyHighScore, score).apply()
    _preferences.value = _preferences.value.copy(highScore = score)
  }

  private fun update(
    key: String,
    value: Boolean,
    transform: PlayerPreferences.() -> PlayerPreferences,
  ) {
    storage.edit().putBoolean(key, value).apply()
    _preferences.value = _preferences.value.transform()
  }

  private fun readPreferences() =
    PlayerPreferences(
      highScore = storage.getInt(KeyHighScore, 0),
      ghostEnabled = storage.getBoolean(KeyGhost, true),
      hapticsEnabled = storage.getBoolean(KeyHaptics, true),
      soundEnabled = storage.getBoolean(KeySound, true),
      reducedMotion = storage.getBoolean(KeyReducedMotion, false),
    )

  private companion object {
    const val StorageName = "neon_tetris_preferences"
    const val KeyHighScore = "high_score"
    const val KeyGhost = "ghost_enabled"
    const val KeyHaptics = "haptics_enabled"
    const val KeySound = "sound_enabled"
    const val KeyReducedMotion = "reduced_motion"
  }
}
