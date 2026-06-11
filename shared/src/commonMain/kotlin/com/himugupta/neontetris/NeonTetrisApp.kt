package com.himugupta.neontetris

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.himugupta.neontetris.audio.GameAudio
import com.himugupta.neontetris.audio.SilentGameAudio
import com.himugupta.neontetris.data.PreferencesRepository
import com.himugupta.neontetris.theme.NeonTetrisTheme
import com.himugupta.neontetris.ui.game.GameScreen
import com.himugupta.neontetris.ui.home.HomeScreen
import com.himugupta.neontetris.ui.settings.SettingsScreen

private enum class AppRoute {
  Home,
  Game,
  Settings,
}

@Composable
fun NeonTetrisApp(
  preferencesRepository: PreferencesRepository,
  gameAudio: GameAudio = SilentGameAudio,
  modifier: Modifier = Modifier,
) {
  val preferences by preferencesRepository.preferences.collectAsState()
  var routeName by rememberSaveable { mutableStateOf(AppRoute.Home.name) }
  val route = AppRoute.valueOf(routeName)
  val navigateHome = { routeName = AppRoute.Home.name }

  PlatformBackHandler(enabled = route != AppRoute.Home, onBack = navigateHome)

  NeonTetrisTheme {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
      AnimatedContent(route, label = "appRoute") { currentRoute ->
        when (currentRoute) {
          AppRoute.Home ->
            HomeScreen(
              highScore = preferences.highScore,
              reducedMotion = preferences.reducedMotion,
              onPlay = { routeName = AppRoute.Game.name },
              onSettings = { routeName = AppRoute.Settings.name },
            )
          AppRoute.Game ->
            GameScreen(
              preferences = preferences,
              preferencesRepository = preferencesRepository,
              gameAudio = gameAudio,
              onBack = navigateHome,
            )
          AppRoute.Settings ->
            SettingsScreen(
              preferences = preferences,
              repository = preferencesRepository,
              onBack = navigateHome,
            )
        }
      }
    }
  }
}
