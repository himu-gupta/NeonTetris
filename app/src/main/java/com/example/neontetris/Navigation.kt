package com.himugupta.neontetris

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.himugupta.neontetris.ui.game.GameScreen
import com.himugupta.neontetris.ui.home.HomeScreen
import com.himugupta.neontetris.ui.settings.SettingsScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(HomeRoute)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<HomeRoute> {
          HomeScreen(
            onPlay = { backStack.add(GameRoute) },
            onSettings = { backStack.add(SettingsRoute) },
          )
        }
        entry<GameRoute> {
          GameScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<SettingsRoute> {
          SettingsScreen(onBack = { backStack.removeLastOrNull() })
        }
      },
  )
}
