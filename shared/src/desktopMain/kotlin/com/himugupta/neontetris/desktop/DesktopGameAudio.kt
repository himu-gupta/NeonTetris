package com.himugupta.neontetris.desktop

import com.himugupta.neontetris.audio.GameAudio
import com.himugupta.neontetris.audio.GameAudioSampleRate
import com.himugupta.neontetris.audio.GameSoundEffect
import com.himugupta.neontetris.audio.buildGameMusicLoop
import java.awt.Toolkit
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

class DesktopGameAudio : GameAudio {
  private val musicClipDelegate = lazy(::createMusicClip)
  private val musicClip: Clip by musicClipDelegate

  override fun setMusicPlaying(playing: Boolean) {
    runCatching {
      if (playing) {
        if (!musicClip.isRunning) {
          musicClip.loop(Clip.LOOP_CONTINUOUSLY)
          musicClip.start()
        }
      } else {
        musicClip.stop()
      }
    }
  }

  override fun playEffect(effect: GameSoundEffect) {
    Toolkit.getDefaultToolkit().beep()
  }

  override fun release() {
    if (musicClipDelegate.isInitialized()) musicClip.close()
  }

  private fun createMusicClip(): Clip {
    val samples = buildGameMusicLoop()
    val bytes = ByteArray(samples.size * Short.SIZE_BYTES)
    samples.forEachIndexed { index, sample ->
      bytes[index * 2] = sample.toInt().toByte()
      bytes[index * 2 + 1] = (sample.toInt() shr 8).toByte()
    }
    return AudioSystem.getClip().apply {
      open(AudioFormat(GameAudioSampleRate.toFloat(), 16, 1, true, false), bytes, 0, bytes.size)
    }
  }
}
