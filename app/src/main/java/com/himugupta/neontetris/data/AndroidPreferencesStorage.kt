package com.himugupta.neontetris.data

import android.content.Context

class AndroidPreferencesStorage(context: Context) : PreferencesStorage {
  private val preferences = context.getSharedPreferences(StorageName, Context.MODE_PRIVATE)

  override fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
    preferences.getBoolean(key, defaultValue)

  override fun putInt(key: String, value: Int) {
    preferences.edit().putInt(key, value).apply()
  }

  override fun putBoolean(key: String, value: Boolean) {
    preferences.edit().putBoolean(key, value).apply()
  }

  private companion object {
    const val StorageName = "neon_tetris_preferences"
  }
}
