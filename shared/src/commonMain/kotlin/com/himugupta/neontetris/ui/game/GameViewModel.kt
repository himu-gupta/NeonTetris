package com.himugupta.neontetris.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himugupta.neontetris.core.game.GameAction
import com.himugupta.neontetris.core.game.GameEngine
import com.himugupta.neontetris.core.game.GameState
import com.himugupta.neontetris.data.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

class GameViewModel(
  private val preferencesRepository: PreferencesRepository,
  private val engine: GameEngine = GameEngine(),
) : ViewModel() {
  private val _state = MutableStateFlow(engine.state)
  val state: StateFlow<GameState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      var previousFrame = TimeSource.Monotonic.markNow()
      while (isActive) {
        delay(FrameDelayMs)
        val deltaMs = previousFrame.elapsedNow().inWholeMilliseconds
        previousFrame = TimeSource.Monotonic.markNow()
        publish(engine.tick(deltaMs))
      }
    }
  }

  fun dispatch(action: GameAction) {
    publish(engine.dispatch(action))
  }

  private fun publish(state: GameState) {
    _state.value = state
    preferencesRepository.updateHighScore(state.score)
  }

  companion object {
    private const val FrameDelayMs = 16L
  }
}
