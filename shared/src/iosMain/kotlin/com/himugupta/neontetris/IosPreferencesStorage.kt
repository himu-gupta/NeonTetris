package com.himugupta.neontetris

import com.himugupta.neontetris.data.PreferencesStorage
import platform.Foundation.NSUserDefaults

class IosPreferencesStorage(
  private val preferences: NSUserDefaults = NSUserDefaults.standardUserDefaults,
) : PreferencesStorage {
  override fun getInt(key: String, defaultValue: Int): Int =
    if (preferences.objectForKey(key) == null) defaultValue else preferences.integerForKey(key).toInt()

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
    if (preferences.objectForKey(key) == null) defaultValue else preferences.boolForKey(key)

  override fun putInt(key: String, value: Int) = preferences.setInteger(value.toLong(), key)

  override fun putBoolean(key: String, value: Boolean) = preferences.setBool(value, key)
}
