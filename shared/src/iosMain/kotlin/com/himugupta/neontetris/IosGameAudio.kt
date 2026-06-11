package com.himugupta.neontetris

import com.himugupta.neontetris.audio.GameAudio
import com.himugupta.neontetris.audio.GameAudioSampleRate
import com.himugupta.neontetris.audio.GameSoundEffect
import com.himugupta.neontetris.audio.buildGameMusicLoop
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.set
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioFormat
import platform.AVFAudio.AVAudioPCMBuffer
import platform.AVFAudio.AVAudioPlayerNode
import platform.AVFAudio.AVAudioPlayerNodeBufferLoops
import platform.AudioToolbox.AudioServicesPlaySystemSound

@OptIn(ExperimentalForeignApi::class)
class IosGameAudio : GameAudio {
  private val engine = AVAudioEngine()
  private val playerNode = AVAudioPlayerNode()
  private var prepared = false

  override fun setMusicPlaying(playing: Boolean) {
    if (playing) {
      prepareMusic()
      playerNode.play()
    } else {
      playerNode.pause()
    }
  }

  override fun playEffect(effect: GameSoundEffect) {
    val soundId =
      when (effect) {
        GameSoundEffect.Rotate,
        GameSoundEffect.Hold -> 1104u
        GameSoundEffect.HardDrop -> 1520u
        GameSoundEffect.LineClear -> 1025u
      }
    AudioServicesPlaySystemSound(soundId)
  }

  override fun release() {
    playerNode.stop()
    engine.stop()
  }

  private fun prepareMusic() {
    if (prepared) return
    val samples = buildGameMusicLoop()
    val format = AVAudioFormat(standardFormatWithSampleRate = GameAudioSampleRate.toDouble(), channels = 1u)
    val buffer = AVAudioPCMBuffer(pCMFormat = format, frameCapacity = samples.size.toUInt())
    buffer.frameLength = samples.size.toUInt()
    val channel = requireNotNull(requireNotNull(buffer.floatChannelData)[0])
    samples.forEachIndexed { index, sample -> channel[index] = sample.toFloat() / Short.MAX_VALUE * 0.11f }

    engine.attachNode(playerNode)
    engine.connect(playerNode, to = engine.mainMixerNode, format = format)
    playerNode.scheduleBuffer(
      buffer = buffer,
      atTime = null,
      options = AVAudioPlayerNodeBufferLoops,
      completionHandler = null,
    )
    engine.startAndReturnError(null)
    prepared = true
  }
}
