package com.himugupta.neontetris.core.game

import kotlin.math.max

class GameEngine(private val seed: Int = 0) {
  private var bag = SevenBag(seed)
  private var gravityElapsedMs = 0L
  private var lockElapsedMs = 0L
  private var lockResets = 0

  var state: GameState = GameState()
    private set

  init {
    restart()
  }

  fun dispatch(action: GameAction): GameState {
    when (action) {
      GameAction.MoveLeft -> move(-1, 0)
      GameAction.MoveRight -> move(1, 0)
      GameAction.SoftDrop -> softDrop()
      GameAction.HardDrop -> hardDrop()
      GameAction.RotateClockwise -> rotate(1)
      GameAction.RotateCounterClockwise -> rotate(-1)
      GameAction.Hold -> hold()
      GameAction.Pause -> if (state.status == GameStatus.Playing) state = state.copy(status = GameStatus.Paused)
      GameAction.Resume -> if (state.status == GameStatus.Paused) state = state.copy(status = GameStatus.Playing)
      GameAction.Restart -> restart()
    }
    return state
  }

  fun tick(deltaMs: Long): GameState {
    if (state.status != GameStatus.Playing || deltaMs <= 0) return state
    gravityElapsedMs += deltaMs.coerceAtMost(100L)
    val interval = gravityInterval(state.level)

    while (gravityElapsedMs >= interval && state.status == GameStatus.Playing) {
      gravityElapsedMs -= interval
      if (!move(0, 1)) break
    }

    val active = state.activePiece ?: return state
    if (!state.board.canPlace(active.copy(y = active.y + 1))) {
      lockElapsedMs += deltaMs.coerceAtMost(100L)
      if (lockElapsedMs >= LockDelayMs) lockActivePiece()
    } else {
      lockElapsedMs = 0L
    }
    return state
  }

  private fun restart() {
    bag = SevenBag(seed)
    gravityElapsedMs = 0L
    lockElapsedMs = 0L
    lockResets = 0
    state = GameState(status = GameStatus.Playing)
    spawnNext()
  }

  private fun move(dx: Int, dy: Int): Boolean {
    if (state.status != GameStatus.Playing) return false
    val current = state.activePiece ?: return false
    val candidate = current.copy(x = current.x + dx, y = current.y + dy)
    if (!state.board.canPlace(candidate)) return false
    state = state.copy(activePiece = candidate, ghostPiece = ghostFor(candidate), lastClear = emptyList())
    if (dx != 0 && lockResets < MaxLockResets) resetLockDelay()
    return true
  }

  private fun softDrop() {
    if (move(0, 1)) state = state.copy(score = state.score + 1)
  }

  private fun hardDrop() {
    if (state.status != GameStatus.Playing) return
    var distance = 0
    while (move(0, 1)) distance++
    state = state.copy(score = state.score + distance * 2)
    lockActivePiece()
  }

  private fun rotate(direction: Int) {
    if (state.status != GameStatus.Playing) return
    val current = state.activePiece ?: return
    val targetRotation = (current.rotation + direction).mod(4)
    val kicks = listOf(
      CellPosition(0, 0),
      CellPosition(-1, 0),
      CellPosition(1, 0),
      CellPosition(-2, 0),
      CellPosition(2, 0),
      CellPosition(0, -1),
      CellPosition(-1, -1),
      CellPosition(1, -1),
    )
    val rotated = kicks.asSequence()
      .map { current.copy(rotation = targetRotation, x = current.x + it.x, y = current.y + it.y) }
      .firstOrNull(state.board::canPlace)
      ?: return

    state = state.copy(activePiece = rotated, ghostPiece = ghostFor(rotated), lastClear = emptyList())
    if (lockResets < MaxLockResets) resetLockDelay()
  }

  private fun hold() {
    if (state.status != GameStatus.Playing || !state.canHold) return
    val current = state.activePiece ?: return
    val held = state.holdPiece
    state = state.copy(holdPiece = current.type, canHold = false, lastClear = emptyList())
    if (held == null) {
      spawnNext(allowHold = false)
    } else {
      spawn(held, allowHold = false)
    }
  }

  private fun lockActivePiece() {
    val piece = state.activePiece ?: return
    val locked = state.board.lock(piece)
    val clearResult = locked.clearLines()
    val cleared = clearResult.rows.size
    val nextCombo = if (cleared > 0) state.combo + 1 else -1
    val isDifficult = cleared == 4
    val base = lineScore(cleared, state.level)
    val backToBackBonus = if (isDifficult && state.backToBack) base / 2 else 0
    val comboBonus = if (cleared > 0 && nextCombo > 0) 50 * nextCombo * state.level else 0
    val totalLines = state.lines + cleared

    state =
      state.copy(
        board = clearResult.board,
        score = state.score + base + backToBackBonus + comboBonus,
        lines = totalLines,
        level = totalLines / 10 + 1,
        combo = nextCombo,
        backToBack = if (isDifficult) true else if (cleared > 0) false else state.backToBack,
        canHold = true,
        lastClear = clearResult.rows,
        eventId = state.eventId + 1,
      )
    spawnNext()
  }

  private fun spawnNext(allowHold: Boolean = true) {
    spawn(bag.take(), allowHold)
  }

  private fun spawn(type: Tetromino, allowHold: Boolean) {
    val piece = FallingPiece(type = type)
    gravityElapsedMs = 0L
    lockElapsedMs = 0L
    lockResets = 0
    if (!state.board.canPlace(piece)) {
      state = state.copy(activePiece = null, ghostPiece = null, nextPieces = bag.preview(3), status = GameStatus.GameOver)
      return
    }
    state =
      state.copy(
        activePiece = piece,
        ghostPiece = ghostFor(piece),
        nextPieces = bag.preview(3),
        canHold = allowHold && state.canHold,
        status = GameStatus.Playing,
      )
  }

  private fun ghostFor(piece: FallingPiece): FallingPiece {
    var ghost = piece
    while (state.board.canPlace(ghost.copy(y = ghost.y + 1))) ghost = ghost.copy(y = ghost.y + 1)
    return ghost
  }

  private fun resetLockDelay() {
    lockElapsedMs = 0L
    lockResets++
  }

  companion object {
    private const val LockDelayMs = 500L
    private const val MaxLockResets = 15

    fun gravityInterval(level: Int): Long = max(80L, 800L - (level - 1) * 60L)

    fun lineScore(lines: Int, level: Int): Int =
      when (lines) {
        1 -> 100
        2 -> 300
        3 -> 500
        4 -> 800
        else -> 0
      } * level
  }
}
