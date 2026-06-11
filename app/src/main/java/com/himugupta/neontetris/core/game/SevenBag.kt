package com.himugupta.neontetris.core.game

import kotlin.random.Random

class SevenBag(seed: Int = Random.nextInt()) {
  private val random = Random(seed)
  private val queue = ArrayDeque<Tetromino>()

  fun take(): Tetromino {
    ensureSize(1)
    return queue.removeFirst()
  }

  fun preview(count: Int): List<Tetromino> {
    ensureSize(count)
    return queue.take(count)
  }

  private fun ensureSize(size: Int) {
    while (queue.size < size) {
      Tetromino.entries.shuffled(random).forEach(queue::addLast)
    }
  }
}
