package com.himugupta.neontetris.audio

import android.media.AudioManager
import android.media.ToneGenerator

class AndroidGameAudio : GameAudio {
  private val musicPlayer = GameMusicPlayer()
  private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 28)

  override fun setMusicPlaying(playing: Boolean) {
    if (playing) musicPlayer.play() else musicPlayer.pause()
  }

  override fun playEffect(effect: GameSoundEffect) {
    when (effect) {
      GameSoundEffect.Rotate,
      GameSoundEffect.Hold -> toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 25)
      GameSoundEffect.HardDrop -> toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 55)
      GameSoundEffect.LineClear -> toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 110)
    }
  }

  override fun release() {
    toneGenerator.release()
    musicPlayer.release()
  }
}
