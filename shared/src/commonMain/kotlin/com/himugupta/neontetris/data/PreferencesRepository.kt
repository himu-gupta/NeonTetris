package com.himugupta.neontetris.data

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

interface PreferencesStorage {
  fun getInt(key: String, defaultValue: Int): Int

  fun getBoolean(key: String, defaultValue: Boolean): Boolean

  fun putInt(key: String, value: Int)

  fun putBoolean(key: String, value: Boolean)
}

class PreferencesRepository(private val storage: PreferencesStorage) {
  private val _preferences = MutableStateFlow(readPreferences())
  val preferences: StateFlow<PlayerPreferences> = _preferences.asStateFlow()

  fun setGhostEnabled(enabled: Boolean) = update(KeyGhost, enabled) { copy(ghostEnabled = enabled) }

  fun setHapticsEnabled(enabled: Boolean) = update(KeyHaptics, enabled) { copy(hapticsEnabled = enabled) }

  fun setSoundEnabled(enabled: Boolean) = update(KeySound, enabled) { copy(soundEnabled = enabled) }

  fun setReducedMotion(enabled: Boolean) = update(KeyReducedMotion, enabled) { copy(reducedMotion = enabled) }

  fun updateHighScore(score: Int) {
    if (score <= _preferences.value.highScore) return
    storage.putInt(KeyHighScore, score)
    _preferences.value = _preferences.value.copy(highScore = score)
  }

  private fun update(
    key: String,
    value: Boolean,
    transform: PlayerPreferences.() -> PlayerPreferences,
  ) {
    storage.putBoolean(key, value)
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
    const val KeyHighScore = "high_score"
    const val KeyGhost = "ghost_enabled"
    const val KeyHaptics = "haptics_enabled"
    const val KeySound = "sound_enabled"
    const val KeyReducedMotion = "reduced_motion"
  }
}
