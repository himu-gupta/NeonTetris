package com.himugupta.neontetris.audio

enum class GameSoundEffect {
  Rotate,
  Hold,
  HardDrop,
  LineClear,
}

interface GameAudio {
  fun setMusicPlaying(playing: Boolean)

  fun playEffect(effect: GameSoundEffect)

  fun release()
}

object SilentGameAudio : GameAudio {
  override fun setMusicPlaying(playing: Boolean) = Unit

  override fun playEffect(effect: GameSoundEffect) = Unit

  override fun release() = Unit
}
