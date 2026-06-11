package com.himugupta.neontetris

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NeonTetrisJourneyTest {
  @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun startGame_pauseAndResume() {
    composeRule.onNodeWithText("START GAME").performClick()
    composeRule.onNodeWithContentDescription("Tetris playfield").assertExists()
    composeRule.onNodeWithContentDescription("Pause game").performClick()
    composeRule.onNodeWithText("PAUSED").assertExists()
    composeRule.onNodeWithText("RESUME").performClick()
    composeRule.onNodeWithContentDescription("Tetris playfield").assertExists()
  }

  @Test
  fun settings_areReachableFromHome() {
    composeRule.onNodeWithText("SETTINGS").performClick()
    composeRule.onNodeWithText("Ghost piece").assertExists()
    composeRule.onNodeWithText("Reduced motion").assertExists()
    composeRule.onNodeWithText("DONE").performClick()
    composeRule.onNodeWithText("START GAME").assertExists()
  }
}
