package com.himugupta.neontetris.core.game

const val BoardWidth = 10
const val BoardHeight = 22
const val HiddenRows = 2

data class CellPosition(val x: Int, val y: Int)

data class FallingPiece(
  val type: Tetromino,
  val rotation: Int = 0,
  val x: Int = 3,
  val y: Int = 0,
) {
  val blocks: List<CellPosition>
    get() = type.cells(rotation).map { CellPosition(x + it.x, y + it.y) }
}

data class Board(val cells: List<List<Tetromino?>> = emptyCells()) {
  init {
    require(cells.size == BoardHeight)
    require(cells.all { it.size == BoardWidth })
  }

  fun canPlace(piece: FallingPiece): Boolean =
    piece.blocks.all { block ->
      block.x in 0 until BoardWidth &&
        block.y in 0 until BoardHeight &&
        cells[block.y][block.x] == null
    }

  fun lock(piece: FallingPiece): Board {
    val next = cells.map { it.toMutableList() }.toMutableList()
    piece.blocks.filter { it.y in 0 until BoardHeight && it.x in 0 until BoardWidth }.forEach {
      next[it.y][it.x] = piece.type
    }
    return Board(next)
  }

  fun clearLines(): LineClearResult {
    val cleared = cells.indices.filter { row -> cells[row].all { it != null } }
    if (cleared.isEmpty()) return LineClearResult(this, emptyList())

    val remaining = cells.filterIndexed { index, _ -> index !in cleared }
    val emptyRows = List(cleared.size) { List<Tetromino?>(BoardWidth) { null } }
    return LineClearResult(Board(emptyRows + remaining), cleared)
  }

  companion object {
    fun emptyCells(): List<List<Tetromino?>> =
      List(BoardHeight) { List<Tetromino?>(BoardWidth) { null } }
  }
}

data class LineClearResult(val board: Board, val rows: List<Int>)

enum class GameStatus {
  Ready,
  Playing,
  Paused,
  GameOver,
}

data class GameState(
  val board: Board = Board(),
  val activePiece: FallingPiece? = null,
  val ghostPiece: FallingPiece? = null,
  val holdPiece: Tetromino? = null,
  val nextPieces: List<Tetromino> = emptyList(),
  val score: Int = 0,
  val lines: Int = 0,
  val level: Int = 1,
  val combo: Int = -1,
  val backToBack: Boolean = false,
  val canHold: Boolean = true,
  val status: GameStatus = GameStatus.Ready,
  val lastClear: List<Int> = emptyList(),
  val eventId: Long = 0,
) {
  val visibleCells: List<List<Tetromino?>>
    get() = board.cells.drop(HiddenRows)
}

enum class GameAction {
  MoveLeft,
  MoveRight,
  SoftDrop,
  HardDrop,
  RotateClockwise,
  RotateCounterClockwise,
  Hold,
  Pause,
  Resume,
  Restart,
}
