package com.himugupta.neontetris.core.game

enum class Tetromino(private val spawnCells: List<CellPosition>) {
  I(listOf(CellPosition(0, 1), CellPosition(1, 1), CellPosition(2, 1), CellPosition(3, 1))),
  O(listOf(CellPosition(1, 0), CellPosition(2, 0), CellPosition(1, 1), CellPosition(2, 1))),
  T(listOf(CellPosition(1, 0), CellPosition(0, 1), CellPosition(1, 1), CellPosition(2, 1))),
  S(listOf(CellPosition(1, 0), CellPosition(2, 0), CellPosition(0, 1), CellPosition(1, 1))),
  Z(listOf(CellPosition(0, 0), CellPosition(1, 0), CellPosition(1, 1), CellPosition(2, 1))),
  J(listOf(CellPosition(0, 0), CellPosition(0, 1), CellPosition(1, 1), CellPosition(2, 1))),
  L(listOf(CellPosition(2, 0), CellPosition(0, 1), CellPosition(1, 1), CellPosition(2, 1)));

  fun cells(rotation: Int): List<CellPosition> {
    if (this == O) return spawnCells
    var result = spawnCells
    repeat(rotation.mod(4)) {
      result = result.map { CellPosition(3 - it.y, it.x) }
    }
    return result
  }
}
