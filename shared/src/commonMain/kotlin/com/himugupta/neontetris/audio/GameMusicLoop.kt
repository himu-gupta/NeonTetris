package com.himugupta.neontetris.audio

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin

const val GameAudioSampleRate = 22_050

fun buildGameMusicLoop(): ShortArray {
  val stepSamples = (GameAudioSampleRate * 60f / BeatsPerMinute / 2f).toInt()
  val result = ShortArray(LeadNotes.size * stepSamples)

  LeadNotes.forEachIndexed { step, midiNote ->
    val bassNote = BassNotes[(step / 4) % BassNotes.size]
    for (sampleInStep in 0 until stepSamples) {
      val index = step * stepSamples + sampleInStep
      val time = index.toDouble() / GameAudioSampleRate
      val envelopePosition = sampleInStep.toFloat() / stepSamples
      val envelope =
        when {
          envelopePosition < 0.06f -> envelopePosition / 0.06f
          envelopePosition > 0.82f -> (1f - envelopePosition) / 0.18f
          else -> 1f
        }.coerceIn(0f, 1f)

      val lead = if (midiNote < 0) 0.0 else squareWave(time, midiToFrequency(midiNote)) * 0.56
      val bass = triangleWave(time, midiToFrequency(bassNote)) * 0.34
      val pulse = sin(2.0 * PI * 2.0 * time) * 0.04
      result[index] =
        ((lead + bass + pulse) * envelope * Short.MAX_VALUE * 0.72).toInt()
          .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
          .toShort()
    }
  }
  return result
}

private fun squareWave(time: Double, frequency: Double): Double =
  if (sin(2.0 * PI * frequency * time) >= 0.0) 1.0 else -1.0

private fun triangleWave(time: Double, frequency: Double): Double {
  val phase = (time * frequency) % 1.0
  return 1.0 - 4.0 * abs(phase - 0.5)
}

private fun midiToFrequency(note: Int): Double = 440.0 * 2.0.pow((note - 69) / 12.0)

private const val BeatsPerMinute = 142f

private val LeadNotes =
  intArrayOf(
    76, 71, 72, 74, 72, 71, 69, 69,
    72, 76, 74, 72, 71, 71, 72, 74,
    76, 72, 69, 69, -1, 74, 77, 81,
    79, 77, 76, 72, 76, 74, 72, 71,
    71, 72, 74, 76, 72, 69, 69, -1,
    74, 77, 81, 79, 77, 76, 72, 76,
    74, 72, 71, 71, 72, 74, 76, 72,
    69, 69, -1, 69, 72, 76, 74, 72,
  )

private val BassNotes = intArrayOf(40, 47, 45, 43, 40, 45, 47, 43, 40, 47, 45, 43, 40, 45, 47, 35)
