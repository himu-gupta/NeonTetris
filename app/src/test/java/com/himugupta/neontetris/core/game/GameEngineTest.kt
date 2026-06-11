package com.himugupta.neontetris.core.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {
  @Test
  fun sevenBag_containsEveryTetrominoExactlyOnce() {
    val bag = SevenBag(seed = 42)
    val firstBag = List(7) { bag.take() }

    assertEquals(Tetromino.entries.toSet(), firstBag.toSet())
    assertEquals(7, firstBag.distinct().size)
  }

  @Test
  fun hardDrop_locksPieceAndSpawnsNext() {
    val engine = GameEngine(seed = 1)
    val first = engine.state.activePiece

    engine.dispatch(GameAction.HardDrop)

    assertNotNull(first)
    assertNotEquals(first, engine.state.activePiece)
    assertTrue(engine.state.board.cells.flatten().any { it == first?.type })
    assertTrue(engine.state.score > 0)
  }

  @Test
  fun hold_canOnlyBeUsedOnceBeforeLock() {
    val engine = GameEngine(seed = 2)
    val first = engine.state.activePiece?.type

    engine.dispatch(GameAction.Hold)
    val afterFirstHold = engine.state.activePiece?.type
    engine.dispatch(GameAction.Hold)

    assertEquals(first, engine.state.holdPiece)
    assertEquals(afterFirstHold, engine.state.activePiece?.type)
    assertFalse(engine.state.canHold)
  }

  @Test
  fun boardClearsFullRowsAndAddsEmptyRowsAtTop() {
    val cells = Board.emptyCells().map { it.toMutableList() }.toMutableList()
    cells[BoardHeight - 1] = MutableList(BoardWidth) { Tetromino.I }
    val result = Board(cells).clearLines()

    assertEquals(listOf(BoardHeight - 1), result.rows)
    assertTrue(result.board.cells.first().all { it == null })
    assertTrue(result.board.cells.last().all { it == null })
  }

  @Test
  fun movementNeverLeavesBoard() {
    val engine = GameEngine(seed = 3)
    repeat(20) { engine.dispatch(GameAction.MoveLeft) }

    assertTrue(engine.state.activePiece.orEmptyBlocks().all { it.x >= 0 })

    repeat(30) { engine.dispatch(GameAction.MoveRight) }
    assertTrue(engine.state.activePiece.orEmptyBlocks().all { it.x < BoardWidth })
  }

  @Test
  fun pauseStopsGravity() {
    val engine = GameEngine(seed = 4)
    val before = engine.state.activePiece

    engine.dispatch(GameAction.Pause)
    engine.tick(5_000)

    assertEquals(before, engine.state.activePiece)
    assertEquals(GameStatus.Paused, engine.state.status)
  }

  private fun FallingPiece?.orEmptyBlocks(): List<CellPosition> = this?.blocks.orEmpty()
}
