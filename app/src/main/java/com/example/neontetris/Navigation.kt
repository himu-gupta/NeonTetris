package com.himugupta.neontetris

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.himugupta.neontetris.ui.game.GameScreen
import com.himugupta.neontetris.ui.home.HomeScreen
import com.himugupta.neontetris.ui.settings.SettingsScreen
import com.himugupta.neontetris.data.PreferencesRepository

@Composable
fun MainNavigation() {
  val context = LocalContext.current
  val preferencesRepository = remember { PreferencesRepository(context.applicationContext) }
  val preferences by preferencesRepository.preferences.collectAsStateWithLifecycle()
  val backStack = rememberNavBackStack(HomeRoute)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<HomeRoute> {
          HomeScreen(
            highScore = preferences.highScore,
            reducedMotion = preferences.reducedMotion,
            onPlay = { backStack.add(GameRoute) },
            onSettings = { backStack.add(SettingsRoute) },
          )
        }
        entry<GameRoute> {
          GameScreen(
            preferences = preferences,
            preferencesRepository = preferencesRepository,
            onBack = { backStack.removeLastOrNull() },
          )
        }
        entry<SettingsRoute> {
          SettingsScreen(
            preferences = preferences,
            repository = preferencesRepository,
            onBack = { backStack.removeLastOrNull() },
          )
        }
      },
  )
}
