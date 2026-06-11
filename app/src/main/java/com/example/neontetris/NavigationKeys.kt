package com.himugupta.neontetris

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute : NavKey

@Serializable data object GameRoute : NavKey

@Serializable data object SettingsRoute : NavKey
