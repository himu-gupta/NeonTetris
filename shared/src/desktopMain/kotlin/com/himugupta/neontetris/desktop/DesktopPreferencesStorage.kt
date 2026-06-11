package com.himugupta.neontetris.desktop

import com.himugupta.neontetris.data.PreferencesStorage
import java.util.prefs.Preferences

class DesktopPreferencesStorage : PreferencesStorage {
  private val preferences = Preferences.userRoot().node("com/himugupta/neontetris")

  override fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
    preferences.getBoolean(key, defaultValue)

  override fun putInt(key: String, value: Int) = preferences.putInt(key, value)

  override fun putBoolean(key: String, value: Boolean) = preferences.putBoolean(key, value)
}
