package com.himugupta.neontetris.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack

class GameMusicPlayer {
  private var audioTrack: AudioTrack? = null

  fun play() {
    runCatching {
      val track = audioTrack ?: createTrack().also { audioTrack = it }
      if (track.playState != AudioTrack.PLAYSTATE_PLAYING) track.play()
    }
  }

  fun pause() {
    runCatching {
      audioTrack?.takeIf { it.playState == AudioTrack.PLAYSTATE_PLAYING }?.pause()
    }
  }

  fun release() {
    runCatching { audioTrack?.release() }
    audioTrack = null
  }

  private fun createTrack(): AudioTrack {
    val samples = buildGameMusicLoop()
    return AudioTrack.Builder()
      .setAudioAttributes(
        AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_GAME)
          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
          .build(),
      )
      .setAudioFormat(
        AudioFormat.Builder()
          .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
          .setSampleRate(GameAudioSampleRate)
          .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
          .build(),
      )
      .setTransferMode(AudioTrack.MODE_STATIC)
      .setBufferSizeInBytes(samples.size * Short.SIZE_BYTES)
      .build()
      .apply {
        write(samples, 0, samples.size)
        setLoopPoints(0, samples.size, -1)
        setVolume(MusicVolume)
      }
  }

  private companion object {
    const val MusicVolume = 0.11f
  }
}
